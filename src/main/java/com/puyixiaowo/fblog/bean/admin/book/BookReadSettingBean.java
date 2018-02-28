package com.puyixiaowo.fblog.bean.admin.book;

import com.puyixiaowo.core.entity.Validatable;
import com.puyixiaowo.fblog.annotation.Table;

import java.io.Serializable;

@Table("book_read_setting")
public class BookReadSettingBean extends Validatable implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Long userId;
	private Long createTime;
	private Integer pageMethod;
	private String bgColor;
	private Integer fontSize;
	private Integer lineHeight;
	private Integer sort;


	public Long getId (){
		return id;
	}

	public void setId (Long id){
		this.id = id;
	}

	public Long getUserId (){
		return userId;
	}

	public void setUserId (Long userId){
		this.userId = userId;
	}

	public Long getCreateTime (){
		return createTime;
	}

	public void setCreateTime (Long createTime){
		this.createTime = createTime;
	}

	public Integer getPageMethod (){
		return pageMethod;
	}

	public void setPageMethod (Integer pageMethod){
		this.pageMethod = pageMethod;
	}

	public String getBgColor (){
		return bgColor;
	}

	public void setBgColor (String bgColor){
		this.bgColor = bgColor;
	}

	public Integer getFontSize (){
		return fontSize;
	}

	public void setFontSize (Integer fontSize){
		this.fontSize = fontSize;
	}

	public Integer getLineHeight (){
		return lineHeight;
	}

	public void setLineHeight (Integer lineHeight){
		this.lineHeight = lineHeight;
	}

	public Integer getSort (){
		return sort;
	}

	public void setSort (Integer sort){
		this.sort = sort;
	}
}