package com.puyixiaowo.fblog.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.puyixiaowo.core.entity.Validatable;
import com.puyixiaowo.fblog.annotation.NotNull;
import com.puyixiaowo.fblog.annotation.Table;
import com.puyixiaowo.fblog.annotation.Transient;
import com.puyixiaowo.generator.utils.CustomDateTimeSerializer;

import java.io.Serializable;


public class NewsBean extends Validatable implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private String source;
	private String title;
	private String html;
	@NotNull
	private String channelId;
	private Boolean havePic;
	private String pubDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public Boolean getHavePic() {
		return havePic;
	}

	public void setHavePic(Boolean havePic) {
		this.havePic = havePic;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getPubDate() {
		return pubDate;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}
}