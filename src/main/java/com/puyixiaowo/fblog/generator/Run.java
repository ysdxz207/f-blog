package com.puyixiaowo.fblog.generator;


/**
 * @author feihong
 * @date 2017-08-13 22:17
 */
public class Run {
    public static void main(String[] args) {
        String dbhost = "f_blog.db";
        String tables= "user,role,user_role,permission,role_permission" +
                ",menu,article,category,tag,article_tag";
        String src = "src/main/java";
        String domainPackage = "com.puyixiaowo.fblog.domain";

        DomainGenerator.generateDomains(dbhost, tables, src, domainPackage);
    }
}
