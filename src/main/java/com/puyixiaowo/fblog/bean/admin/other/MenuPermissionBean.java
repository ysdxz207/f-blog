package com.puyixiaowo.fblog.bean.admin.other;

import com.puyixiaowo.core.entity.Model;

public class MenuPermissionBean extends Model<MenuPermissionBean> {
	private static final long serialVersionUID = 1L;
	private String id;
	private String menuName;
	private String pid;
	private String permission;
	private Boolean isChecked;// 是否勾选

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public Boolean getIsChecked() {
		return isChecked;
	}

	public void setIsChecked(Boolean isChecked) {
		this.isChecked = isChecked;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

}