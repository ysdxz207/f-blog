package com.puyixiaowo.fblog.bean.admin;

import com.puyixiaowo.core.entity.Validatable;
import com.puyixiaowo.fblog.annotation.Table;

import java.io.Serializable;

@Table("role_permission")
public class RolePermissionBean extends Validatable implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Long roleId;
	private Long permissionId;


	public Long getId (){
		return id;
	}

	public void setId (Long id){
		this.id = id;
	}

	public Long getRoleId (){
		return roleId;
	}

	public void setRoleId (Long roleId){
		this.roleId = roleId;
	}

	public Long getPermissionId (){
		return permissionId;
	}

	public void setPermissionId (Long permissionId){
		this.permissionId = permissionId;
	}
}