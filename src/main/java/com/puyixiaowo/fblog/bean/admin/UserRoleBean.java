package com.puyixiaowo.fblog.bean.admin;

import com.puyixiaowo.core.entity.Validatable;
import com.puyixiaowo.fblog.annotation.Table;

import java.io.Serializable;

@Table("user_role")
public class UserRoleBean extends Validatable implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Long userId;
	private Long roleId;


	public Long getId (){
		return id;
	}

	public void setId (Long id){
		this.id = id;
	}

	public Long getUserId (){
		return userId;
	}

	public void setUserId (Long userId){
		this.userId = userId;
	}

	public Long getRoleId (){
		return roleId;
	}

	public void setRoleId (Long roleId){
		this.roleId = roleId;
	}
}