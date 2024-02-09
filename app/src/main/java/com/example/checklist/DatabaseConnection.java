package com.example.checklist;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection connection() throws SQLException {
        try {
            Class.forName("com.example.checklist.MainActivity");
            //String url = "http://192.168.115.4:80/vehicular";
            String url = "jdbc:mysql://192.168.1.137/vehicular";
            String user = "root";
            String password = "";
            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Error al cargar el controlador MySQL", e);
        }
    }
}
