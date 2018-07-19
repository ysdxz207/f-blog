package com.puyixiaowo.fblog.bean.admin;

import com.puyixiaowo.core.entity.Model;
import com.puyixiaowo.fblog.annotation.Table;

@Table("user_role")
public class UserRoleBean extends Model<UserRoleBean> {
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