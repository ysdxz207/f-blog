package com.puyixiaowo.fblog.bean.sys;

import com.puyixiaowo.fblog.domain.User;

import java.io.Serializable;

public class UserBean extends User implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long roleId;
	private String roleName;

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}