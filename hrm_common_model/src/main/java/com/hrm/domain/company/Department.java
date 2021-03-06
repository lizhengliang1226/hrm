package com.hrm.domain.company;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * (Department)实体类
 */
@Entity
@Table(name = "co_department")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("部门实体类")
public class Department implements Serializable {
    private static final long serialVersionUID = -9084332495284489553L;
    @Id
    @ApiModelProperty("ID")
    private String id;

    @ApiModelProperty("父级ID")
    private String pid;

    @ApiModelProperty("企业ID")
    private String companyId;

    @ApiModelProperty("部门名称")
    private String name;

    /**
     * 同级部门不可重复
     */
    @ApiModelProperty("部门编码")
    private String code;

    @ApiModelProperty("负责人ID")
    private String managerId;

    @ApiModelProperty("负责人名称")
    private String manager;

    @ApiModelProperty("介绍")
    private String introduce;

    @ApiModelProperty("创建时间")
    private Date createTime;
}
