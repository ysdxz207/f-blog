package com.puyixiaowo.fblog.generator;


import com.puyixiaowo.fblog.generator.enums.TypeEnums;
import com.puyixiaowo.fblog.generator.model.GField;
import com.puyixiaowo.fblog.generator.utils.FileUtils;
import com.puyixiaowo.fblog.utils.CamelCaseUtils;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * @author feihong
 * @date 2017-08-13 22:17
 */
public class Run {
    public static void main(String[] args) {
        String tableName = "permission";
        String src = "src/main/java";
        String domainPackage = "com.puyixiaowo.fblog.domain";


        List<GField> fieldList = new ArrayList<>();
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
                    String jdbcType = resultSet.getString("type");

                    String javaType = TypeEnums.getJavaType(jdbcType);
                    fieldList.add(new GField(columnName, javaType, jdbcType));
                }


                String basePath = System.getProperty("user.dir").replaceAll("\\\\", "/");

                String domainPath = basePath + "/"
                        + src.replaceAll("\\\\",
                        "/") + "/" + domainPackage
                        .replaceAll("\\.", "/") + "/";

                String filename = domainPath + tableName + ".java";
                File file = new File(filename);

                file.createNewFile();

                for (GField gField :
                        fieldList) {
                    String str = "private "
                            + gField.getJavaType() + " "
                            + CamelCaseUtils.toCamelCase(gField.getName())
                            + ";\n";

                    FileUtils.appendToFile(filename, str);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ":" + e.getMessage());
            System.exit(0);
        }

    }
}
