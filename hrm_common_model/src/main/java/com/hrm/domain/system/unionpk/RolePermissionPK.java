package com.hrm.domain.system.unionpk;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.io.Serializable;

/**
 * @Description
 * @Author LZL
 * @Date 2022/3/8-19:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolePermissionPK implements Serializable {

    private String roleId;

    private String permissionId;
}
