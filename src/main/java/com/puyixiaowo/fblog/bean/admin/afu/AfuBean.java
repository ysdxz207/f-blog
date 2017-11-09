package com.puyixiaowo.fblog.bean.admin.afu;

import com.alibaba.fastjson.annotation.JSONField;
import com.puyixiaowo.core.entity.Validatable;
import com.puyixiaowo.fblog.annotation.NotNull;
import com.puyixiaowo.fblog.annotation.Table;
import com.puyixiaowo.fblog.annotation.Transient;
import com.puyixiaowo.generator.utils.CustomDateTimeSerializer;

import java.io.Serializable;

@Table("afu")
public class AfuBean extends Validatable implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private Long type;
	@JSONField(serializeUsing = CustomDateTimeSerializer.class)
	private Long createTime;
	private String content;

	//
	@NotNull(message = "缺少参数")
	@Transient
	private String typeName;


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

	public Long getType (){
		return type;
	}

	public void setType (Long type){
		this.type = type;
	}

	public Long getCreateTime (){
		return createTime;
	}

	public void setCreateTime (Long createTime){
		this.createTime = createTime;
	}

	public String getContent (){
		return content;
	}

	public void setContent (String content){
		this.content = content;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
}