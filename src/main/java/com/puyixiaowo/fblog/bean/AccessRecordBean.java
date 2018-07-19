package com.puyixiaowo.fblog.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.puyixiaowo.core.entity.Model;
import com.puyixiaowo.fblog.annotation.Table;
import com.puyixiaowo.generator.utils.CustomDateTimeSerializer;

@Table("access_record")
public class AccessRecordBean extends Model<AccessRecordBean> {
    private static final long serialVersionUID = 1L;

    private String id;
    private String articleId;
    private String link;
    @JSONField(serializeUsing = CustomDateTimeSerializer.class)
    private Long accessDate;
    private String ip;
    private String userAgent;
    private String os;
    private String browser;
    @JSONField(serializeUsing = CustomDateTimeSerializer.class)
    private Long createDate;

    public String getId() {
        return id;
    }

    public void setId (String id) {
        this.id = id;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public Long getAccessDate() {
        return accessDate;
    }

    public void setAccessDate(Long accessDate) {
        this.accessDate = accessDate;
    }
}