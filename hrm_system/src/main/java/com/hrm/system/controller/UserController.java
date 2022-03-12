package com.hrm.system.controller;

import cn.hutool.core.util.StrUtil;
import com.hrm.common.controller.BaseController;
import com.hrm.common.entity.PageResult;
import com.hrm.common.entity.Result;
import com.hrm.common.entity.ResultCode;
import com.hrm.common.entity.UserLevel;
import com.hrm.common.exception.CommonException;
import com.hrm.common.utils.BeanMapUtils;
import com.hrm.common.utils.JwtUtils;
import com.hrm.common.utils.PermissionConstants;
import com.hrm.domain.system.Permission;
import com.hrm.domain.system.Role;
import com.hrm.domain.system.User;
import com.hrm.domain.system.response.ProfileResult;
import com.hrm.domain.system.response.UserResult;
import com.hrm.system.service.OssService;
import com.hrm.system.service.PermissionService;
import com.hrm.system.service.UserService;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.sax.SAXTransformerFactory;
import java.util.*;

/**
 * @Description 用户管理
 * @Author LZL
 * @Date 2022/3/7-19:35
 */
@RestController
@CrossOrigin
@RequestMapping("sys")
@Api(tags = "用户管理")
public class UserController extends BaseController {

    private UserService userService;
    private OssService ossService;
    private JwtUtils jwtUtils;
    private PermissionService permissionService;

    @Autowired
    public void setPermissionService(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @Autowired
    public void setJwtUtils(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Autowired
    public void setOssService(OssService ossService) {
        this.ossService = ossService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("user")
    @ApiOperation(value = "保存用户")
    public Result save(@RequestBody User user) {
        //设置保存的企业id，目前使用固定值1，以后会解决
        user.setCompanyId(companyId);
        user.setCompanyName(companyName);
        //默认在职
        user.setInServiceStatus(1);
        //默认级别为普通用户
        user.setLevel(UserLevel.NORMAL_USER);
        userService.save(user);
        return Result.SUCCESS();
    }

    @PutMapping("user/{id}")
    @ApiOperation(value = "更新用户")
    public Result update(@PathVariable(value = "id") String id, @RequestBody User user) {
        user.setId(id);
        userService.update(user);
        return Result.SUCCESS();
    }

    @DeleteMapping(value="user/{id}",name = "API-USER-DELETE")
    @ApiOperation(value = "根据id删除用户")
    public Result delete(@PathVariable(value = "id") String id) {
        userService.deleteById(id);
        return Result.SUCCESS();
    }

    @GetMapping("user/{id}")
    @ApiOperation(value = "根据ID查找用户")
    public Result findById(@PathVariable(value = "id") String id) {
        final User byId = userService.findById(id);
        UserResult user = new UserResult(byId);
        return new Result<>(ResultCode.SUCCESS, user);
    }

    @GetMapping("user")
    @ApiOperation(value = "获取某个企业的用户列表")
    public Result findAll(@RequestParam Map map) {
        //暂时都用1企业，之后会改
        map.put("companyId", companyId);
        System.out.println(map);
        final Page<User> all = userService.findAll(map);
        final PageResult<User> pageResult = new PageResult(all.getTotalElements(), all.getContent());
        return new Result<>(ResultCode.SUCCESS, pageResult);
    }

    @GetMapping("oss")
    @ApiOperation(value = "获取文件上传的后端签名")
    public Result policy() {
        return new Result<>(ResultCode.SUCCESS, ossService.policy());
    }

    @PutMapping("user/assignRoles")
    @ApiOperation(value = "给用户分配角色")
    public Result assignRoles(@RequestBody Map map) {
        String id = (String) map.get("id");
        List<String> roles = (List<String>) map.get("roleIds");
        userService.assignRoles(id, roles);
        return Result.SUCCESS();
    }

    @PostMapping("login")
    @ApiOperation(value = "用户登录")
    public Result login(@RequestBody Map<String, String> loginMap) {
        // 获取手机号和密码
        String mobile = loginMap.get("mobile");
        String password = loginMap.get("password");
        // 通过手机号查找用户
        final User user = userService.findByMobile(mobile);
        if (user == null || !user.getPassword().equals(password)) {
            return new Result(ResultCode.LOGIN_FAIL);
        } else {
            // 登录成功后保存信息到token返回
            Map<String, Object> map = new HashMap<>();
            map.put("companyId", user.getCompanyId());
            map.put("companyName", user.getCompanyName());
             Set<Role> userRoles = user.getRoles();
            // 查询所有的api权限
            StringBuilder sb=new StringBuilder();
            userRoles.forEach(r -> {
                final Set<Permission> permissionSet = r.getPermissions();
                permissionSet.forEach(p -> {
                    if (p.getType() == PermissionConstants.PY_API) {
                        sb.append(p.getCode()).append(",");
                    }
                });
            });
            map.put("apis",sb.toString());
            // 使用工具获取token
            final String token = jwtUtils.createJwt(user.getId(), user.getUsername(), map);
            return new Result(ResultCode.SUCCESS, token);
        }
    }

    @PostMapping("profile")
    @ApiOperation(value = "用户登录成功后的返回信息")
    public Result profile(HttpServletRequest request) throws CommonException {
        // 根据id查找到当前登录用户信息
        User user = userService.findById(claims.getId());
        ProfileResult profileResult;
        // 根据用户级别构造返回结果的roles数组，其中有用户所拥有的全部权限
        if(StrUtil.isBlank(user.getLevel())){
            throw new CommonException(ResultCode.UNAUTHORISE);
        }
        if (UserLevel.NORMAL_USER.equals(user.getLevel())) {
            // 普通用户，直接根据用户拥有的角色获取权限
            profileResult = new ProfileResult(user);
        } else {
            Map<String, Object> map = new HashMap<>(1);
            if (UserLevel.COMPANY_ADMIN.equals(user.getLevel())) {
                // 企业管理员。设置企业可见性为1查询权限给用户
                map.put("enVisible", "1");
            }
            List<Permission> allPerm = permissionService.findAll(map);
            profileResult = new ProfileResult(user, allPerm);
        }
        return new Result(ResultCode.SUCCESS, profileResult);
    }
}
