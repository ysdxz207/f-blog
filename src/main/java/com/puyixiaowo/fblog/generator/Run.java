package com.puyixiaowo.fblog.generator;


import java.sql.*;


/**
 * @author feihong
 * @date 2017-08-13 22:17
 */
public class Run {
    public static void main(String[] args) {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        try (
                Connection conn = DriverManager.getConnection("jdbc:sqlite:f_blog.db")) {

            try (Statement statement = conn.createStatement();
                 ResultSet resultSet = statement.executeQuery("PRAGMA table_info([user])")
            ) {

                while (resultSet.next()) {
                    String tableName = resultSet.getString("name");

                    System.out.println(tableName);

                }
            }

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

    }
}
