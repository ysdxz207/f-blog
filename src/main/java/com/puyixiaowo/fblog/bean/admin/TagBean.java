package com.puyixiaowo.fblog.bean.admin;

import com.puyixiaowo.core.entity.Model;
import com.puyixiaowo.fblog.annotation.NotNull;
import com.puyixiaowo.fblog.annotation.Table;
import com.puyixiaowo.fblog.annotation.Transient;

@Table("tag")
public class TagBean extends Model<TagBean> {
	private static final long serialVersionUID = 1L;

	private String id;
	@NotNull(message = "标签名不能为空")
	private String name;

	///////////////
	@Transient
	private String articleId;

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

	public String getArticleId() {
		return articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}
}