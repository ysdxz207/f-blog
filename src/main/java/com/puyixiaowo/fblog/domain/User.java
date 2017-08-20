package com.puyixiaowo.fblog.domain;

import java.io.Serializable;

public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String loginname;
	private String nickname;
	private String password;
	private Long createTime;
	private Long lastLoginTime;
	private Boolean status;


	public Long getId (){
		return id;
	}

	public void setId (Long id){
		this.id = id;
	}

	public String getLoginname (){
		return loginname;
	}

	public void setLoginname (String loginname){
		this.loginname = loginname;
	}

	public String getNickname (){
		return nickname;
	}

	public void setNickname (String nickname){
		this.nickname = nickname;
	}

	public String getPassword (){
		return password;
	}

	public void setPassword (String password){
		this.password = password;
	}

	public Long getCreateTime (){
		return createTime;
	}

	public void setCreateTime (Long createTime){
		this.createTime = createTime;
	}

	public Long getLastLoginTime (){
		return lastLoginTime;
	}

	public void setLastLoginTime (Long lastLoginTime){
		this.lastLoginTime = lastLoginTime;
	}

	public Boolean getStatus (){
		return status;
	}

	public void setStatus (Boolean status){
		this.status = status;
	}
}