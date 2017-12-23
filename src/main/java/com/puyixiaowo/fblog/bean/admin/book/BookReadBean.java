package com.puyixiaowo.fblog.bean.admin.book;

import com.puyixiaowo.fblog.annotation.Table;

import java.io.Serializable;

@Table("book_read")
public class BookReadBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Long userId;
	private Long bookId;
	private String lastReadingChapter;
	private String lastReadingChapterLink;
	private String bgColor;
	private Integer fontSize;


	public Long getId (){
		return id;
	}

	public void setId (Long id){
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public String getLastReadingChapter (){
		return lastReadingChapter;
	}

	public void setLastReadingChapter (String lastReadingChapter){
		this.lastReadingChapter = lastReadingChapter;
	}

	public String getLastReadingChapterLink() {
		return lastReadingChapterLink;
	}

	public void setLastReadingChapterLink(String lastReadingChapterLink) {
		this.lastReadingChapterLink = lastReadingChapterLink;
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
}