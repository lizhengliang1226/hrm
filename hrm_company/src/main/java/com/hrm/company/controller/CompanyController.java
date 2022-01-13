package com.hrm.company.controller;

import com.hrm.common.entity.Result;
import com.hrm.company.service.CompanyService;
import com.hrm.domain.company.Company;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description
 * @Author LZL
 * @Date 2022/1/12-10:44
 */
@CrossOrigin
@RestController
@RequestMapping("company")
@Api(tags = "企业管理")
public class CompanyController {

    private CompanyService companyService;

    @Autowired
    public void setCompanyService(CompanyService companyService) {
        this.companyService = companyService;
    }

    /**
     * 保存企业
     * @param company
     * @return
     */
    @PostMapping()
    @ApiOperation(value = "保存企业")
    public Result save(@RequestBody Company company) {
        return companyService.add(company);
    }

    /**
     * 根据id更新企业
     * @param id
     * @param company
     * @return
     */
    @PutMapping("{id}")
    @ApiOperation(value = "更新企业")
    public Result update(@PathVariable(value = "id") String id, @RequestBody Company company) {
        company.setId(id);
        return companyService.update(company);
    }

    /**
     * 根据id删除企业
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    @ApiOperation(value = "删除企业")
    public Result delete(@PathVariable(value = "id") String id) {
        return companyService.delete(id);
    }

    /**
     * 根据id查找企业
     * @param id
     * @return
     */
    @GetMapping("{id}")
    @ApiOperation(value = "根据ID查找企业")
    public Result findById(@PathVariable(value = "id") String id) {
        return companyService.findById(id);
    }

    /**
     * 查找全部企业
     * @return
     */
    @GetMapping()
    @ApiOperation(value = "获取企业列表")
    public Result findAll() {
        return companyService.findAll();
    }

}
