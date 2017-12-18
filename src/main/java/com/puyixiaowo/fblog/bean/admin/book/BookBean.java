package com.puyixiaowo.fblog.bean.admin.book;

import com.puyixiaowo.fblog.annotation.Table;

import java.io.Serializable;

@Table("book")
public class BookBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String aId;//api接口书ID
	private String name;
	private String url;
	private String lastUpdateChapter;
	private Long createTime;
	private Long lastUpdateTime;
	private Integer isOver;


	public Long getId (){
		return id;
	}

	public void setId (Long id){
		this.id = id;
	}

	public String getAId (){
		return aId;
	}

	public void setAId (String aId){
		this.aId = aId;
	}

	public String getName (){
		return name;
	}

	public void setName (String name){
		this.name = name;
	}

	public String getUrl (){
		return url;
	}

	public void setUrl (String url){
		this.url = url;
	}

	public String getLastUpdateChapter (){
		return lastUpdateChapter;
	}

	public void setLastUpdateChapter (String lastUpdateChapter){
		this.lastUpdateChapter = lastUpdateChapter;
	}

	public Long getCreateTime (){
		return createTime;
	}

	public void setCreateTime (Long createTime){
		this.createTime = createTime;
	}

	public Long getLastUpdateTime (){
		return lastUpdateTime;
	}

	public void setLastUpdateTime (Long lastUpdateTime){
		this.lastUpdateTime = lastUpdateTime;
	}

	public Integer getIsOver() {
		return isOver;
	}

	public void setIsOver(Integer isOver) {
		this.isOver = isOver;
	}
}