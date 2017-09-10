package com.puyixiaowo.fblog.bean.admin;

import com.puyixiaowo.core.entity.Validatable;
import com.puyixiaowo.fblog.annotation.Table;

import java.io.Serializable;

@Table("article_tag")
public class ArticleTagBean extends Validatable implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Long articleId;
	private Long tagId;


	public Long getId (){
		return id;
	}

	public void setId (Long id){
		this.id = id;
	}

	public Long getArticleId (){
		return articleId;
	}

	public void setArticleId (Long articleId){
		this.articleId = articleId;
	}

	public Long getTagId (){
		return tagId;
	}

	public void setTagId (Long tagId){
		this.tagId = tagId;
	}
}