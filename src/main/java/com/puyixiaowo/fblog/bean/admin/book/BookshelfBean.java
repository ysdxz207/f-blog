package com.puyixiaowo.fblog.bean.admin.book;

import com.puyixiaowo.fblog.annotation.Table;

import java.io.Serializable;

@Table("bookshelf")
public class BookshelfBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Long userId;
	private String bookIds;
	private Long createTime;


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

	public String getBookIds (){
		return bookIds;
	}

	public void setBookIds (String bookIds){
		this.bookIds = bookIds;
	}

	public Long getCreateTime (){
		return createTime;
	}

	public void setCreateTime (Long createTime){
		this.createTime = createTime;
	}
}