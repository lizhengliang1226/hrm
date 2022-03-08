package com.hrm.system.controller;

import com.hrm.common.controller.BaseController;
import com.hrm.common.entity.PageResult;
import com.hrm.common.entity.Result;
import com.hrm.common.entity.ResultCode;
import com.hrm.domain.system.Role;
import com.hrm.system.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Description
 * @Author LZL
 * @Date 2022/3/9-1:21
 */
@RestController
@CrossOrigin
@RequestMapping("sys")
@Api(tags = "角色管理")
public class RoleController extends BaseController {

    private RoleService roleService;

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("role")
    @ApiOperation(value = "保存角色")
    public Result save(@RequestBody Role role) {
        //设置保存的企业id，目前使用固定值1，以后会解决
        role.setCompanyId(companyId);
        roleService.save(role);
        return Result.SUCCESS();
    }

    @PutMapping("role/{id}")
    @ApiOperation(value = "更新角色")
    public Result update(@PathVariable(value = "id") String id, @RequestBody Role role) {
        role.setId(id);
        roleService.update(role);
        return Result.SUCCESS();
    }

    @DeleteMapping("role/{id}")
    @ApiOperation(value = "根据id删除角色")
    public Result delete(@PathVariable(value = "id") String id) {
        roleService.deleteById(id);
        return Result.SUCCESS();
    }

    @GetMapping("role/{id}")
    @ApiOperation(value = "根据ID查找角色")
    public Result findById(@PathVariable(value = "id") String id) {
        return new Result<>(ResultCode.SUCCESS, roleService.findById(id));
    }

    @GetMapping("role")
    @ApiOperation(value = "获取某个企业的角色列表")
    public Result findAll(@RequestParam Map map) {
        System.out.println("hshad");
        //暂时都用1企业，之后会改
        map.put("companyId",companyId);
        System.out.println(map);
        final Page<Role> all = roleService.findSearch(map);
        final PageResult<Role> pageResult = new PageResult( all.getTotalElements(),all.getContent());
        return new Result<>(ResultCode.SUCCESS, pageResult);
    }
}