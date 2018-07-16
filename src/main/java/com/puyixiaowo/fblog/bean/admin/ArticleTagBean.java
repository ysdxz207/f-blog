package com.puyixiaowo.fblog.bean.admin;

import com.puyixiaowo.core.entity.Validatable;
import com.puyixiaowo.fblog.annotation.Table;

import java.io.Serializable;

@Table("article_tag")
public class ArticleTagBean extends Validatable implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private String articleId;
	private String tagId;


	public String getId (){
		return id;
	}

	public void setId (String id){
		this.id = id;
	}

	public String getArticleId (){
		return articleId;
	}

	public void setArticleId (String articleId){
		this.articleId = articleId;
	}

	public String getTagId (){
		return tagId;
	}

	public void setTagId (String tagId){
		this.tagId = tagId;
	}
}