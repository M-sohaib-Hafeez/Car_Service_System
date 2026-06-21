package com.example.carservicecenter_ds.DAO;

import com.example.carservicecenter_ds.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {
    // Services
    // method to insert service
    public static void addServices(Service service){

        String query="INSERT INTO services (service_name, description, base_price, estimated_time, category)" +
                "Values(?,?,?,?,?)";
        try(Connection conn=DataBaseConnection.connect();
            PreparedStatement pstmt= conn.prepareStatement(query)){
            pstmt.setString(1, service.getServiceName());
            pstmt.setString(2, service.getDescription());
            pstmt.setDouble(3,service.getBasePrice());
            pstmt.setInt(4,service.getEstimatedTime());
            pstmt.setString(5,service.getCategory());
            pstmt.executeUpdate();
        }catch (Exception e){
            System.out.println("Failed to insert service");
            e.printStackTrace();
        }
    }

    // method to get all service

    public static List<Service> getAllServices() {
        List<Service> services = new ArrayList<>();
        String query = "SELECT * FROM services ORDER BY service_name";

        try (Connection conn = DataBaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                services.add(mapResultSetToService(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return services;
    }

    public static void deleteService(int serviceId){
        String query="DELETE FROM services WHERE service_id=?;";
        try(Connection conn=DataBaseConnection.connect();
            PreparedStatement pstmt= conn.prepareStatement(query)){
            pstmt.setInt(1,serviceId);
            pstmt.executeUpdate();
        }catch (Exception e){
            System.out.println("Failed to delete service");
            e.printStackTrace();
        }
    }

    // method to assign values to service object

    private static Service mapResultSetToService(ResultSet rs) throws SQLException {
        Service service = new Service();
        service.setServiceId(rs.getInt("service_id"));
        service.setServiceName(rs.getString("service_name"));
        service.setDescription(rs.getString("description"));
        service.setBasePrice(rs.getDouble("base_price"));
        service.setEstimatedTime(rs.getInt("estimated_time"));
        service.setCategory(rs.getString("category"));
        return service;
    }

    public static Mechanic mapResultSetToMechanic(ResultSet result) throws SQLException {
        Mechanic mechanic=new Mechanic();
        mechanic.setMechanicId(result.getInt("mechanic_id"));
        mechanic.setName(result.getString("name"));
        mechanic.setEmail(result.getString("email"));
        mechanic.setPhone(result.getString("phone"));
        mechanic.setStatus(result.getString("status"));
        mechanic.setHourlyRate(result.getDouble("hourly_rate"));
        mechanic.setSpecialization(result.getString("specialization"));

        return mechanic;
    }

    //mechanic requests

    public static List<mechanicRequests> getAllmechanicRequests(){
        List<mechanicRequests> list=new ArrayList<>();
        String query="SELECT * FROM mechanic_requests";

        try(Connection conn=DataBaseConnection.connect();
            PreparedStatement pstmt= conn.prepareStatement(query)){

            ResultSet result=pstmt.executeQuery();

            while(result.next()){
                list.add(mapResultSetToMechanicRequest(result));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public static mechanicRequests mapResultSetToMechanicRequest(ResultSet result) throws SQLException {
        mechanicRequests mr=new mechanicRequests();
        mr.setRequestID(result.getInt(1));
        mr.setUserId(result.getInt(2));
        mr.setName(result.getString(3));
        mr.setEmail(result.getString(4));
        mr.setSpecialization(result.getString(5));
        mr.setExp(result.getInt(6));
        mr.setPhone(result.getString(7));
        mr.setStatus(result.getString(8));
        mr.setHourlyRate(result.getInt(10));

        return mr;
    }

    public static void deleteMechanicRequest(int id){
        String query="DELETE FROM mechanic_requests WHERE request_id=?";
        try(Connection conn=DataBaseConnection.connect();
        PreparedStatement pstmt= conn.prepareStatement(query)){
            pstmt.setInt(1,id);
            pstmt.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteMechanic(int mechanicId) {
        String query = "DELETE FROM mechanics WHERE mechanic_id=?";
        try (Connection conn = DataBaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, mechanicId);
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Failed to delete mechanic");
            e.printStackTrace();
        }
    }

    public static List<Mechanic> getAllMechanic() {
        List<Mechanic> mechanics = new ArrayList<>();
        String query = "SELECT * FROM mechanics";

        try (Connection conn = DataBaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                mechanics.add(mapResultSetToMechanic(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mechanics;
    }

    //Customer

    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String query = "SELECT * FROM customers ORDER BY name";

        try (Connection conn = DataBaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                customers.add(mapResultSetToCustomer(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    private Customer mapResultSetToCustomer(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setCustomerId(rs.getInt("customer_id"));
        customer.setName(rs.getString("name"));
        customer.setEmail(rs.getString("email"));
        customer.setPhone(rs.getString("phone"));
        customer.setAddress(rs.getString("address"));
        customer.setLoyaltyPoints(rs.getInt("loyalty_points"));
        customer.setJoinDate(rs.getDate("join_date").toLocalDate());
        return customer;
    }

    //Appointment

//    public List<ServiceAppointment> getAllAppointments() {
//        List<ServiceAppointment> appointments = new ArrayList<>();
//        String query = "SELECT a.*, c.name as customer_name, c.phone as customer_phone, " +
//                "v.license_plate, v.make, v.model, v.year, " +
//                "s.service_name " +
//                "FROM appointments a " +
//                "LEFT JOIN customers c ON a.customer_id = c.customer_id " +
//                "LEFT JOIN vehicles v ON a.vehicle_id = v.vehicle_id " +
//                "LEFT JOIN services s ON a.service_id = s.service_id " +
//                "ORDER BY a.scheduled_time DESC";
//
//        try (Connection conn = DataBaseConnection.connect();
//             Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery(query)) {
//
//            while (rs.next()) {
//                appointments.add(mapResultSetToAppointment(rs));
//            }
//        } catch (SQLException e) {
//            System.err.println("Error getting all appointments: " + e.getMessage());
//            e.printStackTrace();
//        }
//        return appointments;
//    }
//
//    private ServiceAppointment mapResultSetToAppointment(ResultSet rs) throws SQLException {
//        ServiceAppointment appointment = new ServiceAppointment();
//
//        appointment.setAppointmentId(rs.getInt("appointment_id"));
//        appointment.setCustomerId(rs.getInt("customer_id"));
//        appointment.setVehicleId(rs.getInt("vehicle_id"));
//        appointment.setServiceId(rs.getInt("service_id"));
//
//        // Handle timestamps
//        Timestamp appointmentDate = rs.getTimestamp("appointment_date");
//        if (appointmentDate != null) {
//            appointment.setAppointmentDate(appointmentDate.toLocalDateTime());
//        }
//
//        appointment.setScheduledTime(rs.getTimestamp("scheduled_time").toLocalDateTime());
//        appointment.setStatus(rs.getString("status"));
//        appointment.setPriorityLevel(rs.getInt("priority_level"));
//
//        Timestamp estimatedCompletion = rs.getTimestamp("estimated_completion");
//        if (estimatedCompletion != null) {
//            appointment.setEstimatedCompletion(estimatedCompletion.toLocalDateTime());
//        }
//
//        Timestamp actualCompletion = rs.getTimestamp("actual_completion");
//        if (actualCompletion != null) {
//            appointment.setActualCompletion(actualCompletion.toLocalDateTime());
//        }
//
//        appointment.setTotalCost(rs.getDouble("total_cost"));
//        appointment.setDiscountApplied(rs.getDouble("discount_applied"));
//        appointment.setNotes(rs.getString("notes"));
//
//        Timestamp createdAt = rs.getTimestamp("created_at");
//        if (createdAt != null) {
//            appointment.setCreatedAt(createdAt.toLocalDateTime());
//        }
//
//        // Additional display fields
//        appointment.setCustomerName(rs.getString("customer_name"));
//        appointment.setServiceName(rs.getString("service_name"));
//
//        // Create vehicle info string
//        String make = rs.getString("make");
//        String model = rs.getString("model");
//        String licensePlate = rs.getString("license_plate");
//        int year = rs.getInt("year");
//        if (make != null && model != null && licensePlate != null) {
//            appointment.setVehicleInfo(year + " " + make + " " + model + " (" + licensePlate + ")");
//        }
//
//        return appointment;
//    }

    // 1️⃣ Get all appointments with customer, vehicle, service info
    public static List<ServiceAppointment> getAllAppointments() {
        List<ServiceAppointment> appointments = new ArrayList<>();
        String query = "SELECT a.*, c.name AS customer_name, c.phone AS customer_phone, " +
                "v.license_plate, v.make, v.model, v.year, " +
                "s.service_name, m.name AS mechanic_name " +
                "FROM appointments a " +
                "LEFT JOIN customers c ON a.customer_id = c.customer_id " +
                "LEFT JOIN vehicles v ON a.vehicle_id = v.vehicle_id " +
                "LEFT JOIN services s ON a.service_id = s.service_id " +
                "LEFT JOIN mechanics m ON a.mechanic_id = m.mechanic_id " +
                "ORDER BY a.scheduled_time DESC";

        try (Connection conn = DataBaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                appointments.add(mapResultSetToAppointment(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching appointments: " + e.getMessage());
            e.printStackTrace();
        }
        return appointments;
    }

    //  Map ResultSet to ServiceAppointment object
    private static ServiceAppointment mapResultSetToAppointment(ResultSet rs) throws SQLException {
        ServiceAppointment appointment = new ServiceAppointment();

        appointment.setAppointmentId(rs.getInt("appointment_id"));
        appointment.setMechanicId(rs.getInt("mechanic_id"));
        appointment.setCustomerId(rs.getInt("customer_id"));
        appointment.setVehicleId(rs.getInt("vehicle_id"));
        appointment.setServiceId(rs.getInt("service_id"));

        Timestamp st = rs.getTimestamp("scheduled_time");
        if (st != null) appointment.setScheduledTime(st.toLocalDateTime());

        appointment.setStatus(rs.getString("status"));
        appointment.setPriorityLevel(rs.getInt("priority_level"));

        Timestamp estComp = rs.getTimestamp("estimated_completion");
        if (estComp != null) appointment.setEstimatedCompletion(estComp.toLocalDateTime());

        Timestamp actComp = rs.getTimestamp("actual_completion");
        if (actComp != null) appointment.setActualCompletion(actComp.toLocalDateTime());

        appointment.setTotalCost(rs.getDouble("total_cost"));
        appointment.setDiscountApplied(rs.getDouble("discount_applied"));
        appointment.setNotes(rs.getString("notes"));

        Timestamp created = rs.getTimestamp("created_at");
        if (created != null) appointment.setCreatedAt(created.toLocalDateTime());

        // Additional display fields
        appointment.setCustomerName(rs.getString("customer_name"));
        appointment.setMechanicName(rs.getString("mechanic_name"));
        appointment.setServiceName(rs.getString("service_name"));

        // Vehicle info
        String make = rs.getString("make");
        String model = rs.getString("model");
        String license = rs.getString("license_plate");
        int year = rs.getInt("year");
        if (make != null && model != null && license != null) {
            appointment.setVehicleInfo(year + " " + make + " " + model + " (" + license + ")");
        }

        return appointment;
    }

    public static boolean CheckMechanicAppoinment(int id){
        String query = "SELECT COUNT(*) FROM appointments WHERE mechanic_id=?";

        try(Connection conn=DataBaseConnection.connect();
        PreparedStatement pstmt= conn.prepareStatement(query)){
            pstmt.setInt(1,id);
            ResultSet rs=pstmt.executeQuery();
            rs.next();
            if(rs.getInt(1)>0){
                return true;
            }else {
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Add this method to your AdminDAO.java class

    public static boolean addMechanic(mechanicRequests request) {
        Connection conn = null;
        try {
            conn = DataBaseConnection.connect();
            conn.setAutoCommit(false); // Start transaction

            // Step 1: Insert into mechanics table
            String insertMechanicSql = """
            INSERT INTO mechanics (user_id, name, specialization, hourly_rate, phone, email, status)
            VALUES (?, ?, ?, ?, ?, ?, 'AVAILABLE')
        """;

            PreparedStatement mechanicStmt = conn.prepareStatement(insertMechanicSql, Statement.RETURN_GENERATED_KEYS);
            mechanicStmt.setInt(1, request.getUserId());
            mechanicStmt.setString(2, request.getName());
            mechanicStmt.setString(3, request.getSpecialization());
            mechanicStmt.setDouble(4, request.getHourlyRate());
            mechanicStmt.setString(5, request.getPhone());
            mechanicStmt.setString(6, request.getEmail());

            int rowsAffected = mechanicStmt.executeUpdate();

            if (rowsAffected == 0) {
                conn.rollback();
                return false;
            }

            // Get the generated mechanic_id
            ResultSet generatedKeys = mechanicStmt.getGeneratedKeys();
            int mechanicId = -1;
            if (generatedKeys.next()) {
                mechanicId = generatedKeys.getInt(1);
            }

            // Step 2: Update mechanic_requests status to APPROVED
            String updateRequestSql = "UPDATE mechanic_requests SET status = 'APPROVED' WHERE request_id = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateRequestSql);
            updateStmt.setInt(1, request.getRequestID());
            updateStmt.executeUpdate();

            // Commit transaction
            conn.commit();

            System.out.println("✅ Mechanic approved successfully! Mechanic ID: " + mechanicId);
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
