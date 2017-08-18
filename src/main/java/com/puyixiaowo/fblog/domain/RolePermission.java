package com.puyixiaowo.fblog.domain;

import java.io.Serializable;

import com.puyixiaowo.core.entity.Validatable;

public class RolePermission extends Validatable implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String roleId;
	private String permissionId;


	public Long getId (){
		return id;
	}

	public void setId (Long id){
		this.id = id;
	}

	public String getRoleId (){
		return roleId;
	}

	public void setRoleId (String roleId){
		this.roleId = roleId;
	}

	public String getPermissionId (){
		return permissionId;
	}

	public void setPermissionId (String permissionId){
		this.permissionId = permissionId;
	}
}