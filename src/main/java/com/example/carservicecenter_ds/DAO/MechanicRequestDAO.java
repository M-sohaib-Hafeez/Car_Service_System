package com.example.carservicecenter_ds.DAO;

import com.example.carservicecenter_ds.model.Mechanic;
import com.example.carservicecenter_ds.model.mechanicRequests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MechanicRequestDAO {

    public boolean insertRequest(Mechanic mechanic, int userId) {
        String sql = """
            INSERT INTO mechanic_requests
            (user_id, name, email, specialization, experience, phone, hourly_rate, status)
            VALUES (?,?,?,?,?,?,?,'PENDING')
        """;

        try (Connection conn = DataBaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, mechanic.getName());
            ps.setString(3, mechanic.getEmail());
            ps.setString(4, mechanic.getSpecialization());
            ps.setInt(5, mechanic.getExperience());
            ps.setString(6, mechanic.getPhone());
            ps.setDouble(7, mechanic.getHourlyRate());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get mechanic request by ID
    public mechanicRequests getRequestById(int requestId) {
        String sql = "SELECT * FROM mechanic_requests WHERE request_id = ?";

        try (Connection conn = DataBaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, requestId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                mechanicRequests request = new mechanicRequests();
                request.setRequestID(rs.getInt("request_id"));
                request.setUserId(rs.getInt("user_id"));
                request.setName(rs.getString("name"));
                request.setEmail(rs.getString("email"));
                request.setSpecialization(rs.getString("specialization"));
                request.setExp(rs.getInt("experience"));
                request.setPhone(rs.getString("phone"));
                request.setHourlyRate(rs.getDouble("hourly_rate"));
                request.setStatus(rs.getString("status"));
                return request;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Update request status (APPROVED/REJECTED)
    public boolean updateRequestStatus(int requestId, String status) {
        String sql = "UPDATE mechanic_requests SET status = ? WHERE request_id = ?";

        try (Connection conn = DataBaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, requestId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete mechanic request
    public boolean deleteRequest(int requestId) {
        String sql = "DELETE FROM mechanic_requests WHERE request_id = ?";

        try (Connection conn = DataBaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, requestId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}