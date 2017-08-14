package com.puyixiaowo.fblog.generator;


import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;


/**
 * @author feihong
 * @date 2017-08-13 22:17
 */
public class Run {
    public static void main(String[] args) {
        String tableName = "user";
        String src = "src/main/java";
        String domainPackage = "com.puyixiaowo.fblog.domain";


        Map<String, String> tables = new HashMap<>();
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        try (
                Connection conn = DriverManager.getConnection("jdbc:sqlite:f_blog.db")) {

            try (Statement statement = conn.createStatement();
                 ResultSet resultSet = statement.executeQuery("PRAGMA table_info([" + tableName + "])")
            ) {

                while (resultSet.next()) {
                    String columnName = resultSet.getString("name");
                    String type = resultSet.getString("type");
                    tables.put(columnName, type);
                }


                String basePath = System.getProperty("user.dir").replaceAll("\\\\", "/");

                String domainPath = basePath + "/"
                        + src.replaceAll("\\\\",
                        "/") + "/" + domainPackage
                        .replaceAll("\\.", "/");

                File file = new File(domainPath);
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ":" + e.getMessage());
            System.exit(0);
        }

    }
}
