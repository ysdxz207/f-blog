package com.puyixiaowo.fblog.generator.enums;

/**
 * @author Moses
 * @date 2017-08-15 23:28
 */
public enum TypeEnums {

    INTEGER("INTEGER", "Integer"),
    REAL("REAL", "Double"),
    TEXT("TEXT", "String"),
    VARCHAR("VARCHAR", "String"),
    BLOB("BLOB", "String");


    TypeEnums(String jdbcType, String javaType) {
        this.jdbcType = jdbcType;
        this.javaType = javaType;
    }

    public static String getJavaType(String jdbcType){
        jdbcType = jdbcType.substring(0, jdbcType.indexOf("("));
        for (TypeEnums typeEnums : TypeEnums.values()) {
            if (jdbcType.equalsIgnoreCase(typeEnums.jdbcType)) {
                return typeEnums.javaType;
            }
        }
        return "Undefined";
    }


    public String jdbcType;
    public String javaType;
}
