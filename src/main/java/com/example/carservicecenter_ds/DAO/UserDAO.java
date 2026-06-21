package com.example.carservicecenter_ds.DAO;

import com.example.carservicecenter_ds.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public User authenticate(String email, String password) {
        // Try with customer_id first
        String sql = """
        SELECT user_id, username, email, password, role, customer_id
        FROM users
        WHERE email = ? AND password = ?
        """;

        try (Connection conn = DataBaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));

                int customerId = rs.getInt("customer_id");
                user.setCustomerId(rs.wasNull() ? null : customerId);

                return user;
            }

        } catch (SQLException e) {
            // If customer_id column is missing, try fallback query
            if (e.getMessage().contains("Unknown column") || e.getMessage().contains("customer_id")) {
                return authenticateFallback(email, password);
            }
            e.printStackTrace();
        }
        return null;
    }

    private User authenticateFallback(String email, String password) {
        String sql = """
        SELECT user_id, username, email, password, role
        FROM users
        WHERE email = ? AND password = ?
        """;

        try (Connection conn = DataBaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                // customer_id will be null
                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Original createUser method (for mechanics and others)
    public int createUser(String username, String email, String password, String role) {
        String sql = """
        INSERT INTO users (username, email, password, role)
        VALUES (?, ?, ?, ?)
        """;

        try (Connection conn = DataBaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, role);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // New method for creating user with customer_id (for customers)
    public int createUserWithCustomerId(String username, String email, String password, String role, int customerId) {
        // Try inserting with customer_id
        String sql = """
        INSERT INTO users (username, email, password, role, customer_id)
        VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection conn = DataBaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, role);
            ps.setInt(5, customerId);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            // Fallback if column missing
            if (e.getMessage().contains("Unknown column")) {
                System.err.println("Warning: customer_id column missing in users table. Creating user without linking customer_id.");
                return createUser(username, email, password, role);
            }
            e.printStackTrace();
        }
        return -1;
    }

    // Update user's customer_id
    public boolean updateCustomerId(int userId, int customerId) {
        String sql = "UPDATE users SET customer_id = ? WHERE user_id = ?";

        try (Connection conn = DataBaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, customerId);
            ps.setInt(2, userId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            if (e.getMessage().contains("Unknown column")) {
                System.err.println("Warning: Cannot update customer_id because the column is missing.");
                return false;
            }
            e.printStackTrace();
        }
        return false;
    }
}