package com.puyixiaowo.fblog.domain;

import java.io.Serializable;

public class Article implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String creator;
	private String title;
	private String context;
	private String category;
	private String tagIds;
	private Long createDate;
	private Long lastUpdateDate;
	private Integer status;
	private Boolean isDel;


	public Long getId (){
		return id;
	}

	public void setId (Long id){
		this.id = id;
	}

	public String getCreator (){
		return creator;
	}

	public void setCreator (String creator){
		this.creator = creator;
	}

	public String getTitle (){
		return title;
	}

	public void setTitle (String title){
		this.title = title;
	}

	public String getContext (){
		return context;
	}

	public void setContext (String context){
		this.context = context;
	}

	public String getCategory (){
		return category;
	}

	public void setCategory (String category){
		this.category = category;
	}

	public String getTagIds (){
		return tagIds;
	}

	public void setTagIds (String tagIds){
		this.tagIds = tagIds;
	}

	public Long getCreateDate (){
		return createDate;
	}

	public void setCreateDate (Long createDate){
		this.createDate = createDate;
	}

	public Long getLastUpdateDate (){
		return lastUpdateDate;
	}

	public void setLastUpdateDate (Long lastUpdateDate){
		this.lastUpdateDate = lastUpdateDate;
	}

	public Integer getStatus (){
		return status;
	}

	public void setStatus (Integer status){
		this.status = status;
	}

	public Boolean getIsDel (){
		return isDel;
	}

	public void setIsDel (Boolean isDel){
		this.isDel = isDel;
	}
}