/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author Jasmine
 */
public class DBConnection {
    

    private static Connection conn = null;
    private static final String URL  =
        "jdbc:sqlserver://localhost:1433;databaseName=SalonDB;" +
        "encrypt=false;trustServerCertificate=true;";
    private static final String USER = "sa";
    private static final String PASS = "tubilla";

    public static Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                conn = DriverManager.getConnection(URL, USER, PASS);
            } catch (ClassNotFoundException e) {
                throw new SQLException("JDBC Driver not found.");
            }
        }
        return conn;
    }
}