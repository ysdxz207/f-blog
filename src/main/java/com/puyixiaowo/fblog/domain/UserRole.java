package com.puyixiaowo.fblog.domain;

import java.io.Serializable;

import com.puyixiaowo.core.entity.Validatable;

public class UserRole extends Validatable implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String userId;
	private String roleId;


	public Long getId (){
		return id;
	}

	public void setId (Long id){
		this.id = id;
	}

	public String getUserId (){
		return userId;
	}

	public void setUserId (String userId){
		this.userId = userId;
	}

	public String getRoleId (){
		return roleId;
	}

	public void setRoleId (String roleId){
		this.roleId = roleId;
	}
}