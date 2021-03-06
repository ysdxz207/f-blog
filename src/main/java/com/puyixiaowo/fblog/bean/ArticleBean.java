package com.puyixiaowo.fblog.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.puyixiaowo.core.entity.Validatable;
import com.puyixiaowo.fblog.annotation.NotNull;
import com.puyixiaowo.fblog.annotation.Table;
import com.puyixiaowo.fblog.annotation.Transient;
import com.puyixiaowo.generator.utils.CustomDateTimeSerializer;

import java.io.Serializable;

@Table("article")
public class ArticleBean extends Validatable implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String creator;
	@NotNull(message = "标题不能为空")
	private String title;
	@NotNull(message = "内容不能为空")
	private String context;
	@NotNull(message = "请选择分类")
	private Long categoryId;
	@JSONField(serializeUsing = CustomDateTimeSerializer.class)
	private Long createDate;
	@JSONField(serializeUsing = CustomDateTimeSerializer.class)
	private Long lastUpdateDate;
	@NotNull(message = "请选择所属")
	private String type;
	private Integer status;


	////////////////
	@Transient
	private String category;
	@Transient
	private String tags;
	@Transient
	private Integer accessCountAll;
	@Transient
	private Integer accessCountToday;


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

	public Long getCategoryId (){
		return categoryId;
	}

	public void setCategoryId (Long categoryId){
		this.categoryId = categoryId;
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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getAccessCountAll() {
		return accessCountAll;
	}

	public void setAccessCountAll(Integer accessCountAll) {
		this.accessCountAll = accessCountAll;
	}

	public Integer getAccessCountToday() {
		return accessCountToday;
	}

	public void setAccessCountToday(Integer accessCountToday) {
		this.accessCountToday = accessCountToday;
	}
}