package com.example.carservicecenter_ds.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {
    static Connection conn = null;
    public static Connection connect(){
        try {
            String url= "jdbc:mysql://localhost/car_service_center";
            String username="your_mysql_username";
            String password="your_mysql_password";

            conn = DriverManager.getConnection(url,username,password);
            System.out.println("Database connected successfully");


        } catch (SQLException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
        return conn;
    }
}
