package com.puyixiaowo.fblog.domain;

import java.io.Serializable;

public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private String username;
	private String password;
	private String nickname;


	public String getId (){
		return id;
	}

	public void setId (String id){
		this.id = id;
	}

	public String getUsername (){
		return username;
	}

	public void setUsername (String username){
		this.username = username;
	}

	public String getPassword (){
		return password;
	}

	public void setPassword (String password){
		this.password = password;
	}

	public String getNickname (){
		return nickname;
	}

	public void setNickname (String nickname){
		this.nickname = nickname;
	}
}