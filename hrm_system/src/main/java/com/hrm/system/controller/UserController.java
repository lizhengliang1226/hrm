package com.hrm.system.controller;

import com.hrm.common.controller.BaseController;
import com.hrm.common.entity.PageResult;
import com.hrm.common.entity.Result;
import com.hrm.common.entity.ResultCode;
import com.hrm.common.utils.BeanMapUtils;
import com.hrm.domain.system.User;
import com.hrm.domain.system.response.UserResult;
import com.hrm.system.service.OssService;
import com.hrm.system.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.xml.transform.sax.SAXTransformerFactory;
import java.util.List;
import java.util.Map;

/**
 * @Description  用户管理
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

    @DeleteMapping("user/{id}")
    @ApiOperation(value = "根据id删除用户")
    public Result delete(@PathVariable(value = "id") String id) {
        userService.deleteById(id);
        return Result.SUCCESS();
    }

    @GetMapping("user/{id}")
    @ApiOperation(value = "根据ID查找用户")
    public Result findById(@PathVariable(value = "id") String id) {
        final User byId = userService.findById(id);
        UserResult user=new UserResult(byId);
        return new Result<>(ResultCode.SUCCESS, user);
    }

    @GetMapping("user")
    @ApiOperation(value = "获取某个企业的用户列表")
    public Result findAll(@RequestParam Map map) {
        //暂时都用1企业，之后会改
        map.put("companyId",companyId);
        System.out.println(map);
        final Page<User> all = userService.findAll(map);
        final PageResult<User> pageResult = new PageResult( all.getTotalElements(),all.getContent());
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
        String  id = (String) map.get("id");
        List<String> roles = (List<String>) map.get("roleIds");
        userService.assignRoles(id, roles);
        return Result.SUCCESS();
    }
}
