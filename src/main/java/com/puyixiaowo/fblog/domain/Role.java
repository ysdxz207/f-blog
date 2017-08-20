package com.puyixiaowo.fblog.domain;

import java.io.Serializable;

public class Role implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String roleName;
	private String code;


	public Long getId (){
		return id;
	}

	public void setId (Long id){
		this.id = id;
	}

	public String getRoleName (){
		return roleName;
	}

	public void setRoleName (String roleName){
		this.roleName = roleName;
	}

	public String getCode (){
		return code;
	}

	public void setCode (String code){
		this.code = code;
	}
}