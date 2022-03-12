package com.hrm.domain.system.response;

import com.hrm.domain.system.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author LZL
 * @Date 2022/3/11-6:33
 */
@Data
@NoArgsConstructor
@ApiModel("用户返回结果实体")
public class UserResult implements Serializable {
    private static final long serialVersionUID = 4297464181093070302L;

    @ApiModelProperty("ID")
    private String id;

    @ApiModelProperty("手机号码")
    private String mobile;

    @ApiModelProperty("用户名称")
    private String username;

    @ApiModelProperty("密码")
    private String password;


    @ApiModelProperty("启用状态 0为禁用 1为启用")
    private Integer enableState;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("企业ID")
    private String companyId;

    @ApiModelProperty("企业名称")
    private String companyName;

    @ApiModelProperty("部门ID")
    private String departmentId;

    @ApiModelProperty("部门名称")
    private String departmentName;

    @ApiModelProperty("入职时间")
    private Date timeOfEntry;

    @ApiModelProperty("离职时间")
    private Date timeOfDimission;

    @ApiModelProperty("聘用形式")
    private Integer formOfEmployment;

    @ApiModelProperty("工号")
    private String workNumber;

    @ApiModelProperty("管理形式")
    private String formOfManagement;

    @ApiModelProperty("工作城市")
    private String workingCity;

    @ApiModelProperty("转正时间")
    private Date correctionTime;

    @ApiModelProperty("在职状态 1.在职  2.离职")
    private Integer inServiceStatus;

    @ApiModelProperty("用户级别 saasAdmin,user")
    private String level;

    @ApiModelProperty("员工照片")
    private String staffPhoto;
    @ApiModelProperty("角色id数组")
    private List<String> roleIds = new ArrayList<>();

    public UserResult(User user) {
        BeanUtils.copyProperties(user, this);
        user.getRoles().forEach(role -> {
            roleIds.add(role.getId());
        });
    }
}
