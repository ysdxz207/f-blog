package com.puyixiaowo.fblog.bean.admin.book;

import com.puyixiaowo.fblog.annotation.Table;

import java.io.Serializable;

@Table("book_chapter")
public class BookChapterBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Long bookId;
	private String name;
	private String content;
	private String link;
	private Integer status;


	public Long getId (){
		return id;
	}

	public void setId (Long id){
		this.id = id;
	}

	public Long getBookId (){
		return bookId;
	}

	public void setBookId (Long bookId){
		this.bookId = bookId;
	}

	public String getName (){
		return name;
	}

	public void setName (String name){
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getLink (){
		return link;
	}

	public void setLink (String link){
		this.link = link;
	}

	public Integer getStatus (){
		return status;
	}

	public void setStatus (Integer status){
		this.status = status;
	}
}