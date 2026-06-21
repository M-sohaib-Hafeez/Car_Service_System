package com.example.carservicecenter_ds.DAO;

import com.example.carservicecenter_ds.model.ServiceAppointment;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServiceAppointmentDAO {
    private static final List<ServiceAppointment> appointments = new ArrayList<>();

    static {
        // ALL demo data
    }

    public static List<ServiceAppointment> getByMechanic(int mechanicId) {

        List<ServiceAppointment> list = new ArrayList<>();

        String sql = """
        SELECT a.appointment_id, a.mechanic_id, a.status,
               s.service_name,
               CONCAT(v.make, ' ', v.model, ' - ', v.license_plate) AS vehicle_info
        FROM appointments a
        JOIN services s ON a.service_id = s.service_id
        JOIN vehicles v ON a.vehicle_id = v.vehicle_id
        WHERE a.mechanic_id = ?
        AND a.status != 'COMPLETED'
    """;

        try (Connection conn = DataBaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, mechanicId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ServiceAppointment a = new ServiceAppointment();
                a.setAppointmentId(rs.getInt("appointment_id"));
                a.setMechanicId(rs.getInt("mechanic_id"));
                a.setServiceName(rs.getString("service_name"));
                a.setVehicleInfo(rs.getString("vehicle_info"));
                a.setStatus(rs.getString("status"));

                list.add(a);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }




    public static void updateStatus(int appointmentId, String status) {

        String sql = """
        UPDATE appointments
        SET status = ?, 
            actual_completion = CASE 
                WHEN ? = 'COMPLETED' THEN NOW()
                ELSE actual_completion
            END
        WHERE appointment_id = ?
    """;

        try (Connection conn = DataBaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setString(2, status);
            ps.setInt(3, appointmentId);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static List<ServiceAppointment> getCompletedByMechanic(int mechanicId) {

        List<ServiceAppointment> list = new ArrayList<>();

        String sql = """
        SELECT a.appointment_id, a.actual_completion,
               s.service_name,
               CONCAT(v.make, ' ', v.model, ' - ', v.license_plate) AS vehicle_info
        FROM appointments a
        JOIN services s ON a.service_id = s.service_id
        JOIN vehicles v ON a.vehicle_id = v.vehicle_id
        WHERE a.mechanic_id = ?
        AND a.status = 'COMPLETED'
        ORDER BY a.actual_completion DESC
    """;

        try (Connection conn = DataBaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, mechanicId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ServiceAppointment a = new ServiceAppointment();
                a.setAppointmentId(rs.getInt("appointment_id"));
                a.setServiceName(rs.getString("service_name"));
                a.setVehicleInfo(rs.getString("vehicle_info"));
                a.setStatus("COMPLETED");
                Timestamp ts = rs.getTimestamp("actual_completion");
                if (ts != null) {
                    a.setActualCompletion(ts.toLocalDateTime());
                }


                list.add(a);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static int countJobs(int mechanicId, String status) {

        String sql = """
        SELECT COUNT(*) FROM appointments
        WHERE mechanic_id = ?
        AND status = ?
    """;

        try (Connection conn = DataBaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, mechanicId);
            ps.setString(2, status);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }



}
