package com.example.carservicecenter_ds.DAO;

import com.example.carservicecenter_ds.model.Customer;
import com.example.carservicecenter_ds.model.ServiceAppointment;
import com.example.carservicecenter_ds.model.Vehicle;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    // Get customer by ID
    public Customer getCustomerById(int customerId) {
        String query = "SELECT * FROM customers WHERE customer_id = ?";
        try (Connection conn = DataBaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToCustomer(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get customer by phone
    public Customer getCustomerByPhone(String phone) {
        String query = "SELECT * FROM customers WHERE phone = ?";
        try (Connection conn = DataBaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, phone);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToCustomer(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get customer by email
    public Customer getCustomerByEmail(String email) {
        String query = "SELECT * FROM customers WHERE email = ?";
        try (Connection conn = DataBaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToCustomer(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get all customers
    public static List<Customer> getAllCustomers() {
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

    // Add new customer
    public int addCustomer(Customer customer) {
        String query = "INSERT INTO customers (name, email, phone, address, loyalty_points) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DataBaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, customer.getName());
            pstmt.setString(2, customer.getEmail());
            pstmt.setString(3, customer.getPhone());
            pstmt.setString(4, customer.getAddress());
            pstmt.setInt(5, customer.getLoyaltyPoints());

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Get customer's vehicles
    public List<Vehicle> getCustomerVehicles(int customerId) {
        List<Vehicle> vehicles = new ArrayList<>();
        String query = "SELECT * FROM vehicles WHERE customer_id = ?";

        try (Connection conn = DataBaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                vehicles.add(mapResultSetToVehicle(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicles;
    }

    // Get customer's appointments
    public List<ServiceAppointment> getCustomerAppointments(int customerId) {
        List<ServiceAppointment> appointments = new ArrayList<>();
        String query = "SELECT a.*, c.name as customer_name, " +
                "CONCAT(v.year, ' ', v.make, ' ', v.model) as vehicle_info, " +
                "s.service_name " +
                "FROM appointments a " +
                "JOIN customers c ON a.customer_id = c.customer_id " +
                "JOIN vehicles v ON a.vehicle_id = v.vehicle_id " +
                "JOIN services s ON a.service_id = s.service_id " +
                "WHERE a.customer_id = ? " +
                "ORDER BY a.scheduled_time DESC";

        try (Connection conn = DataBaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                appointments.add(mapResultSetToAppointment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    // Get current active appointment
    public ServiceAppointment getCurrentAppointment(int customerId) {
        String query = "SELECT a.*, c.name as customer_name, " +
                "CONCAT(v.year, ' ', v.make, ' ', v.model) as vehicle_info, " +
                "s.service_name " +
                "FROM appointments a " +
                "JOIN customers c ON a.customer_id = c.customer_id " +
                "JOIN vehicles v ON a.vehicle_id = v.vehicle_id " +
                "JOIN services s ON a.service_id = s.service_id " +
                "WHERE a.customer_id = ? AND a.status IN ('PENDING', 'IN_PROGRESS') " +
                "ORDER BY a.scheduled_time DESC LIMIT 1";

        try (Connection conn = DataBaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToAppointment(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get queue position for customer
    public int getQueuePosition(int customerId) {
        String query = "SELECT COUNT(*) + 1 as position FROM appointments " +
                "WHERE status = 'PENDING' AND scheduled_time < " +
                "(SELECT scheduled_time FROM appointments WHERE customer_id = ? AND status = 'PENDING' LIMIT 1)";

        try (Connection conn = DataBaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("position");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Book new appointment
    public boolean bookAppointment(ServiceAppointment appointment) {
        String query = "INSERT INTO appointments (customer_id, vehicle_id, service_id, mechanic_id, " +
                "scheduled_time, status, priority_level, estimated_completion, total_cost, notes) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DataBaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, appointment.getCustomerId());
            pstmt.setInt(2, appointment.getVehicleId());
            pstmt.setInt(3, appointment.getServiceId());
            pstmt.setInt(4, 1); // Default mechanic ID
            pstmt.setTimestamp(5, Timestamp.valueOf(appointment.getScheduledTime()));
            pstmt.setString(6, appointment.getStatus());
            pstmt.setInt(7, appointment.getPriorityLevel());

            if (appointment.getEstimatedCompletion() != null) {
                pstmt.setTimestamp(8, Timestamp.valueOf(appointment.getEstimatedCompletion()));
            } else {
                pstmt.setNull(8, Types.TIMESTAMP);
            }

            pstmt.setDouble(9, appointment.getTotalCost());
            pstmt.setString(10, appointment.getNotes());

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Add new vehicle
    public int addVehicle(Vehicle vehicle) {
        String query = "INSERT INTO vehicles (customer_id, license_plate, vehicle_type, make, model, year) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DataBaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, vehicle.getCustomerId());
            pstmt.setString(2, vehicle.getLicensePlate());
            pstmt.setString(3, vehicle.getVehicleType());
            pstmt.setString(4, vehicle.getMake());
            pstmt.setString(5, vehicle.getModel());
            pstmt.setInt(6, vehicle.getYear());

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Helper method to map ResultSet to Customer
    private static Customer mapResultSetToCustomer(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setCustomerId(rs.getInt("customer_id"));
        customer.setName(rs.getString("name"));
        customer.setEmail(rs.getString("email"));
        customer.setPhone(rs.getString("phone"));
        customer.setAddress(rs.getString("address"));
        customer.setLoyaltyPoints(rs.getInt("loyalty_points"));
        return customer;
    }

    // Helper method to map ResultSet to Vehicle
    private Vehicle mapResultSetToVehicle(ResultSet rs) throws SQLException {
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleId(rs.getInt("vehicle_id"));
        vehicle.setCustomerId(rs.getInt("customer_id"));
        vehicle.setLicensePlate(rs.getString("license_plate"));
        vehicle.setVehicleType(rs.getString("vehicle_type"));
        vehicle.setMake(rs.getString("make"));
        vehicle.setModel(rs.getString("model"));
        vehicle.setYear(rs.getInt("year"));

        Date lastServiceDate = rs.getDate("last_service_date");
        if (lastServiceDate != null) {
            vehicle.setLastServiceDate(lastServiceDate.toLocalDate());
        }
        return vehicle;
    }

    // Helper method to map ResultSet to ServiceAppointment
    private ServiceAppointment mapResultSetToAppointment(ResultSet rs) throws SQLException {
        ServiceAppointment appointment = new ServiceAppointment();
        appointment.setAppointmentId(rs.getInt("appointment_id"));
        appointment.setCustomerId(rs.getInt("customer_id"));
        appointment.setVehicleId(rs.getInt("vehicle_id"));
        appointment.setServiceId(rs.getInt("service_id"));

        Timestamp scheduledTime = rs.getTimestamp("scheduled_time");
        if (scheduledTime != null) {
            appointment.setScheduledTime(scheduledTime.toLocalDateTime());
        }

        appointment.setStatus(rs.getString("status"));
        appointment.setPriorityLevel(rs.getInt("priority_level"));

        Timestamp estimatedCompletion = rs.getTimestamp("estimated_completion");
        if (estimatedCompletion != null) {
            appointment.setEstimatedCompletion(estimatedCompletion.toLocalDateTime());
        }

        Timestamp actualCompletion = rs.getTimestamp("actual_completion");
        if (actualCompletion != null) {
            appointment.setActualCompletion(actualCompletion.toLocalDateTime());
        }

        appointment.setTotalCost(rs.getDouble("total_cost"));
        appointment.setDiscountApplied(rs.getDouble("discount_applied"));
        appointment.setNotes(rs.getString("notes"));

        // Additional display fields
        appointment.setCustomerName(rs.getString("customer_name"));
        appointment.setVehicleInfo(rs.getString("vehicle_info"));
        appointment.setServiceName(rs.getString("service_name"));

        return appointment;
    }
}