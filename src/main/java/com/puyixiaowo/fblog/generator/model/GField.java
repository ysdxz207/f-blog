package com.puyixiaowo.fblog.generator.model;

/**
 * @author Moses
 * @date 2017-08-15 23:09
 */
public class GField {
    private String name;
    private String javaType;
    private String jdbcType;


    public GField(String name, String javaType, String jdbcType) {
        this.name = name;
        this.javaType = javaType;
        this.jdbcType = jdbcType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getJdbcType() {
        return jdbcType;
    }

    public void setJdbcType(String jdbcType) {
        this.jdbcType = jdbcType;
    }
}
