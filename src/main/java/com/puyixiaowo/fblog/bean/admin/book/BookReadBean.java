package com.puyixiaowo.fblog.bean.admin.book;

import com.puyixiaowo.core.entity.Validatable;
import com.puyixiaowo.fblog.annotation.NotNull;
import com.puyixiaowo.fblog.annotation.Table;

import java.io.Serializable;

@Table("book_read")
public class BookReadBean extends Validatable implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Long userId;
	@NotNull
	private Long bookId;
	private String source;
	@NotNull
	private String lastReadingChapter;
	@NotNull
	private String lastReadingChapterLink;
	private String bgColor;
	private Integer fontSize;
	private Integer lineHeight;
	private Integer pageMethod;
	private Integer sort;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getLastReadingChapter() {
		return lastReadingChapter;
	}

	public void setLastReadingChapter(String lastReadingChapter) {
		this.lastReadingChapter = lastReadingChapter;
	}

	public String getLastReadingChapterLink() {
		return lastReadingChapterLink;
	}

	public void setLastReadingChapterLink(String lastReadingChapterLink) {
		this.lastReadingChapterLink = lastReadingChapterLink;
	}

	public String getBgColor() {
		return bgColor;
	}

	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}

	public Integer getFontSize() {
		return fontSize;
	}

	public void setFontSize(Integer fontSize) {
		this.fontSize = fontSize;
	}

	public Integer getLineHeight() {
		return lineHeight;
	}

	public void setLineHeight(Integer lineHeight) {
		this.lineHeight = lineHeight;
	}

	public Integer getPageMethod() {
		return pageMethod;
	}

	public void setPageMethod(Integer pageMethod) {
		this.pageMethod = pageMethod;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
}