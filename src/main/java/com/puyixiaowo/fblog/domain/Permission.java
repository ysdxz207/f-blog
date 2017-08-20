package com.puyixiaowo.fblog.domain;

import java.io.Serializable;

public class Permission implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Long menuId;
	private String permissionName;
	private String permission;


	public Long getId (){
		return id;
	}

	public void setId (Long id){
		this.id = id;
	}

	public Long getMenuId (){
		return menuId;
	}

	public void setMenuId (Long menuId){
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