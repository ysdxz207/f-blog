package com.puyixiaowo.fblog.domain;

import com.puyixiaowo.core.entity.Validatable;

import java.io.Serializable;

public class Permission extends Validatable implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String menuId;
	private String permissionName;
	private String permission;


	public Long getId (){
		return id;
	}

	public void setId (Long id){
		this.id = id;
	}

	public String getMenuId (){
		return menuId;
	}

	public void setMenuId (String menuId){
		this.menuId = menuId;
	}

	public String getPermissionName (){
		return permissionName;
	}

	public void setPermissionName (String permissionName){
		this.permissionName = permissionName;
	}

	public String getPermission (){
		return permission;
	}

	public void setPermission (String permission){
		this.permission = permission;
	}
}