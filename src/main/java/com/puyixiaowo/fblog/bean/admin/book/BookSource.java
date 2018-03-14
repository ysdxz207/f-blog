package com.puyixiaowo.fblog.bean.admin.book;

import com.puyixiaowo.fblog.service.book.BookService;

import java.io.Serializable;
import java.util.Date;

/**
 * 仅用于前端展示，不写入数据库
 */
public class BookSource implements Serializable,Comparable<BookSource>{
    private static final long serialVersionUID = 1L;

    private String _id;
    private String lastChapter;
    private String source;
    private String name;
    private String updated;


    private boolean currentSource;


    

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getLastChapter() {
        return lastChapter;
    }

    public void setLastChapter(String lastChapter) {
        this.lastChapter = lastChapter;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public boolean isCurrentSource() {
        return currentSource;
    }

    public void setCurrentSource(boolean currentSource) {
        this.currentSource = currentSource;
    }

    @Override
    public int compareTo(BookSource o) {
        Date thisUpdated = BookService.getBookDate(this.getUpdated());
        Date objUpdated = BookService.getBookDate(o.getUpdated());

        thisUpdated = thisUpdated == null ? new Date() : thisUpdated;
        objUpdated = objUpdated== null ? new Date() : objUpdated;
        return thisUpdated.compareTo(objUpdated) >= 0 ? 1 : 0;
    }
}
