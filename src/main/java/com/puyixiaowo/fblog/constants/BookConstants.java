package com.puyixiaowo.fblog.constants;

/**
 * 
 * @author Moses
 * @date 2017-12-18
 * 
 */
public class BookConstants {
    private static final String HOST_API = "http://api.zhuishushenqi.com";

    /**
     * 模糊搜索
     */
    public static final String URL_SEARCH = HOST_API + "/book/fuzzy-search";

    /**
     * 章节列表
     */
    public static final String URL_CHAPTERS = HOST_API + "/mix-atoc/";

    /**
     * 根据链接获取章节内容，链接需encodeurl
     */
    public static final String URL_CHAPTER_CONTENT = "http://chapterup.zhuishushenqi.com/chapter/";
}
