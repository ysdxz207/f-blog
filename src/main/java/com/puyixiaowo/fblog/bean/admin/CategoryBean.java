package com.puyixiaowo.fblog.bean.admin;

import com.puyixiaowo.core.entity.Model;
import com.puyixiaowo.fblog.annotation.NotNull;
import com.puyixiaowo.fblog.annotation.Table;

@Table("category")
public class CategoryBean extends Model<CategoryBean> {
	private static final long serialVersionUID = 1L;

	private String id;
	@NotNull(message = "分类名不能为空")
	private String name;


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
}