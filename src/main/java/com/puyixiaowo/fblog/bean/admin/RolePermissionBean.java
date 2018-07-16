package com.puyixiaowo.fblog.bean.admin;

import com.puyixiaowo.core.entity.Validatable;
import com.puyixiaowo.fblog.annotation.Table;

import java.io.Serializable;

@Table("role_permission")
public class RolePermissionBean extends Validatable implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private String roleId;
	private String permissionId;


	public String getId (){
		return id;
	}

	public void setId (String id){
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