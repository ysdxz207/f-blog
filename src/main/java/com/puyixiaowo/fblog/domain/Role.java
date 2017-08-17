package com.puyixiaowo.fblog.domain;

import java.io.Serializable;

import com.puyixiaowo.core.entity.Validatable;

public class Role extends Validatable implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String roleName;
	private String code;


	public Long getId (){
		return id;
	}

	public void setId (Long id){
		id = id;
	}

	public String getRoleName (){
		return roleName;
	}

	public void setRoleName (String roleName){
		roleName = roleName;
	}

	public String getCode (){
		return code;
	}

	public void setCode (String code){
		code = code;
	}
}