package com.puyixiaowo.fblog.bean.admin;

import com.puyixiaowo.core.entity.Model;
import com.puyixiaowo.fblog.annotation.NotNull;
import com.puyixiaowo.fblog.annotation.Table;

@Table("permission")
public class PermissionBean extends Model<PermissionBean> {
	private static final long serialVersionUID = 1L;

	private String id;
	@NotNull(message = "需选择所属菜单")
	private String menuId;
	@NotNull(message = "权限名不能为空")
	private String permissionName;
	@NotNull(message = "权限不能为空")
	private String permission;


	public String getId (){
		return id;
	}

	public void setId (String id){
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