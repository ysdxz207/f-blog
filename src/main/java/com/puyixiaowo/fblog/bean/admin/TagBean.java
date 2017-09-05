package com.puyixiaowo.fblog.bean.admin;

import com.puyixiaowo.core.entity.Validatable;
import com.puyixiaowo.fblog.annotation.NotNull;
import com.puyixiaowo.fblog.annotation.Table;

import java.io.Serializable;

@Table("tag")
public class TagBean extends Validatable implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	@NotNull(message = "标签名不能为空")
	private String name;


	public Long getId (){
		return id;
	}

	public void setId (Long id){
		this.id = id;
	}

	public String getName (){
		return name;
	}

	public void setName (String name){
		this.name = name;
	}
}