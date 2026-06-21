package com.example.carservicecenter_ds.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseInitializer {
    public static void initialize(){
        try(Connection con = DataBaseConnection.connect();
            Statement stmt = con.createStatement()){
            String[] createTables = {
                    // Customers table
                    "CREATE TABLE IF NOT EXISTS customers (" +
                            "customer_id INT AUTO_INCREMENT PRIMARY KEY," +
                            "name VARCHAR(100) NOT NULL," +
                            "email VARCHAR(100)," +
                            "phone VARCHAR(20) NOT NULL UNIQUE," +
                            "address TEXT," +
                            "loyalty_points INT DEFAULT 0," +
                            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                            ");",

                    // Vehicles table
                    "CREATE TABLE IF NOT EXISTS vehicles (" +
                            "vehicle_id INT AUTO_INCREMENT PRIMARY KEY," +
                            "customer_id INT NOT NULL," +
                            "license_plate VARCHAR(20) NOT NULL UNIQUE," +
                            "vehicle_type VARCHAR(50)," +
                            "make VARCHAR(50)," +
                            "model VARCHAR(50)," +
                            "year INT," +
                            "last_service_date DATE," +
                            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                            "FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE" +
                            ");",

                    // Services table
                    "CREATE TABLE IF NOT EXISTS services (" +
                            "service_id INT AUTO_INCREMENT PRIMARY KEY," +
                            "service_name VARCHAR(100) NOT NULL," +
                            "description TEXT," +
                            "base_price DECIMAL(10, 2) NOT NULL," +
                            "estimated_time INT NOT NULL," +
                            "category VARCHAR(50)," +
                            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                            ");",

                    // Mechanics table - UPDATED WITH user_id
                    "CREATE TABLE IF NOT EXISTS mechanics (" +
                            "mechanic_id INT AUTO_INCREMENT PRIMARY KEY," +
                            "user_id INT NULL," +
                            "name VARCHAR(100) NOT NULL," +
                            "specialization VARCHAR(100)," +
                            "status ENUM('AVAILABLE', 'BUSY', 'OFF_DUTY') DEFAULT 'AVAILABLE'," +
                            "hourly_rate DECIMAL(10, 2)," +
                            "phone VARCHAR(20)," +
                            "email VARCHAR(100)," +
                            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                            ");",

                    // Appointments table
                    "CREATE TABLE IF NOT EXISTS appointments (" +
                            "appointment_id INT AUTO_INCREMENT PRIMARY KEY," +
                            "mechanic_id INT NOT NULL,"+
                            "customer_id INT NOT NULL," +
                            "vehicle_id INT NOT NULL," +
                            "service_id INT NOT NULL," +
                            "scheduled_time TIMESTAMP NOT NULL," +
                            "status ENUM('PENDING', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED') DEFAULT 'PENDING'," +
                            "priority_level INT DEFAULT 3," +
                            "estimated_completion TIMESTAMP," +
                            "actual_completion TIMESTAMP," +
                            "total_cost DECIMAL(10, 2) DEFAULT 0," +
                            "discount_applied DECIMAL(10, 2) DEFAULT 0," +
                            "notes TEXT," +
                            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                            "FOREIGN KEY (customer_id) REFERENCES customers(customer_id)," +
                            "FOREIGN KEY (vehicle_id) REFERENCES vehicles(vehicle_id)," +
                            "FOREIGN KEY (service_id) REFERENCES services(service_id)," +
                            "FOREIGN KEY (mechanic_id) REFERENCES mechanics(mechanic_id)"+
                            ");",

                    // Users table
                    "CREATE TABLE IF NOT EXISTS users (" +
                            "user_id INT AUTO_INCREMENT PRIMARY KEY," +
                            "username VARCHAR(50) NOT NULL UNIQUE," +
                            "password VARCHAR(255) NOT NULL," +
                            "role ENUM('ADMIN', 'MECHANIC', 'CUSTOMER') NOT NULL," +
                            "customer_id INT NULL," +
                            "email VARCHAR(100) NOT NULL UNIQUE,"+
                            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                            "FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE" +
                            ");",

                    // Mechanic Requests table
                    "CREATE TABLE IF NOT EXISTS mechanic_requests ("+
                            "request_id INT AUTO_INCREMENT PRIMARY KEY,"+
                            "user_id INT,"+
                            "name VARCHAR(100),"+
                            "email VARCHAR(100),"+
                            "specialization VARCHAR(100),"+
                            "experience INT,"+
                            "phone VARCHAR(20),"+
                            "status ENUM('PENDING','APPROVED','REJECTED') DEFAULT 'PENDING',"+
                            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"+
                            "hourly_rate DOUBLE"+
                            ");"
            };

            for(String x: createTables){
                stmt.executeUpdate(x);
            }

            // Add user_id column to mechanics if it doesn't exist (for existing databases)
            try {
                stmt.executeUpdate(
                        "ALTER TABLE mechanics ADD COLUMN IF NOT EXISTS user_id INT NULL"
                );
            } catch (SQLException e) {
                // Column might already exist, ignore
            }

        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}