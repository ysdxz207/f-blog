package com.puyixiaowo.fblog.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.puyixiaowo.core.entity.Model;
import com.puyixiaowo.fblog.annotation.NotNull;
import com.puyixiaowo.fblog.annotation.Table;
import com.puyixiaowo.fblog.annotation.Transient;
import com.puyixiaowo.fblog.bean.admin.TagBean;
import com.puyixiaowo.generator.utils.CustomDateTimeSerializer;

import java.util.List;

@Table("article")
public class ArticleBean extends Model<ArticleBean> {
	private static final long serialVersionUID = 1L;

	private String id;
	private String creator;
	@NotNull(message = "标题不能为空")
	private String title;
	@NotNull(message = "内容不能为空")
	private String context;
	@NotNull(message = "请选择分类")
	private String categoryId;
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
	private List<TagBean> tagList;
	@Transient
	private String tagId;//搜索用
	@Transient
	private Integer accessCountAll;
	@Transient
	private Integer accessCountToday;


	public String getId (){
		return id;
	}

	public void setId (String id){
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

	public String getCategoryId (){
		return categoryId;
	}

	public void setCategoryId (String categoryId){
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

	public List<TagBean> getTagList() {
		return tagList;
	}

	public void setTagList(List<TagBean> tagList) {
		this.tagList = tagList;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
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