package com.example.carservicecenter_ds.DAO;

import com.example.carservicecenter_ds.model.Mechanic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MechanicDAO {

    // Insert new mechanic (when approved from request)
    public int insertMechanic(Mechanic mechanic) {
        String sql = """
            INSERT INTO mechanics
            (name, specialization, hourly_rate, phone, email, status)
            VALUES (?, ?, ?, ?, ?, 'AVAILABLE')
        """;

        try (Connection conn = DataBaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, mechanic.getName());
            ps.setString(2, mechanic.getSpecialization());
            ps.setDouble(3, mechanic.getHourlyRate());
            ps.setString(4, mechanic.getPhone());
            ps.setString(5, mechanic.getEmail());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1); // Return the generated mechanic_id
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Get mechanic by user_id (this was wrong in your original - mechanics table doesn't have user_id)
    // We'll need to add user_id to mechanics table OR link differently
    public Mechanic getMechanicByEmail(String email) {
        String sql = "SELECT * FROM mechanics WHERE email = ?";

        try (Connection conn = DataBaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Mechanic m = new Mechanic();
                m.setMechanicId(rs.getInt("mechanic_id"));
                m.setName(rs.getString("name"));
                m.setSpecialization(rs.getString("specialization"));
                m.setHourlyRate(rs.getDouble("hourly_rate"));
                m.setPhone(rs.getString("phone"));
                m.setEmail(rs.getString("email"));
                m.setStatus(rs.getString("status"));
                return m;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get mechanic by mechanic_id
    public Mechanic getMechanicById(int mechanicId) {
        String sql = "SELECT * FROM mechanics WHERE mechanic_id = ?";

        try (Connection conn = DataBaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, mechanicId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Mechanic m = new Mechanic();
                m.setMechanicId(rs.getInt("mechanic_id"));
                m.setName(rs.getString("name"));
                m.setSpecialization(rs.getString("specialization"));
                m.setHourlyRate(rs.getDouble("hourly_rate"));
                m.setPhone(rs.getString("phone"));
                m.setEmail(rs.getString("email"));
                m.setStatus(rs.getString("status"));
                return m;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Update method to link user_id to mechanic (if we add user_id column to mechanics)
    public boolean linkMechanicToUser(int mechanicId, int userId) {
        String sql = "UPDATE mechanics SET user_id = ? WHERE mechanic_id = ?";

        try (Connection conn = DataBaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, mechanicId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get mechanic by user_id (requires user_id column in mechanics table)
    public Mechanic getMechanicByUserId(int userId) {
        String sql = "SELECT * FROM mechanics WHERE user_id = ?";

        try (Connection conn = DataBaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Mechanic m = new Mechanic();
                m.setMechanicId(rs.getInt("mechanic_id"));
                m.setName(rs.getString("name"));
                m.setSpecialization(rs.getString("specialization"));
                m.setHourlyRate(rs.getDouble("hourly_rate"));
                m.setPhone(rs.getString("phone"));
                m.setEmail(rs.getString("email"));
                m.setStatus(rs.getString("status"));
                return m;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}