package com.puyixiaowo.fblog.bean.admin.afu;

import com.alibaba.fastjson.annotation.JSONField;
import com.puyixiaowo.core.entity.Model;
import com.puyixiaowo.fblog.annotation.Table;
import com.puyixiaowo.fblog.annotation.Transient;
import com.puyixiaowo.generator.utils.CustomDateTimeSerializer;

@Table("afu")
public class AfuBean extends Model<AfuBean> {
	private static final long serialVersionUID = 1L;

	private String id;
	private String name;
	private String type;
	@JSONField(serializeUsing = CustomDateTimeSerializer.class)
	private Long createTime;
	private String content;

	//
	@Transient
	private String typeName;


	public String getId (){
		return id;
	}

	public void setId (String id){
		this.id = id;
	}

	public String getName (){
		return name;
	}

	public void setName (String name){
		this.name = name;
	}

	public String getType (){
		return type;
	}

	public void setType (String type){
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