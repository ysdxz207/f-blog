package com.puyixiaowo.fblog.bean.admin;

import com.puyixiaowo.core.entity.Validatable;
import com.puyixiaowo.fblog.annotation.Table;

import java.io.Serializable;

@Table("user_role")
public class UserRoleBean extends Validatable implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private String userId;
	private String roleId;


	public String getId (){
		return id;
	}

	public void setId (String id){
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