package com.hrm.domain.system.response;

import com.hrm.domain.system.Role;
import com.hrm.domain.system.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
@Getter
@Setter
@NoArgsConstructor
@ApiModel("角色返回结果实体")
public class RoleResult implements Serializable {

    @ApiModelProperty("ID")
    private String id;

    @ApiModelProperty("角色名")
    private String name;

    @ApiModelProperty("说明")
    private String description;

    @ApiModelProperty("企业id")
    private String companyId;

    @ApiModelProperty("权限id数组")
    private List<String> permIds = new ArrayList<>();

    public RoleResult(Role role) {
        BeanUtils.copyProperties(role, this);
        role.getPermissions().forEach(perm -> {
            permIds.add(perm.getId());
        });
    }
}
