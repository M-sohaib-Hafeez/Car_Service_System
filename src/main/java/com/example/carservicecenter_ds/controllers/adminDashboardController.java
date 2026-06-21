package com.example.carservicecenter_ds.controllers;




import com.example.carservicecenter_ds.DAO.DataBaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class adminDashboardController {

    // Dashboard Labels
    @FXML private Label totalCustomersLabel;
    @FXML private Label totalVehiclesLabel;
    @FXML private Label todayAppointmentsLabel;
    @FXML private Label availableMechanicsLabel;
    @FXML private Label pendingRequestsLabel;
    @FXML private Label totalRevenueLabel;
    @FXML private Label todayIncomeLabel;
    @FXML private Label thisMonthRevenueLabel;
    @FXML private Label lastMonthRevenueLabel;
    @FXML private Label activeUsersLabel;
    @FXML private Label avgCompletionTimeLabel;
    @FXML private Label pendingAppointmentsLabel;
    @FXML private Label busyMechanicsLabel;
    @FXML private Label userRoleBreakdownLabel;

    // Trend Labels
    @FXML private Label customerTrendLabel;
    @FXML private Label vehicleTrendLabel;
    @FXML private Label appointmentTrendLabel;
    @FXML private Label mechanicStatusLabel;
    @FXML private Label requestTrendLabel;

    // Charts
    @FXML private PieChart appointmentPieChart;
    @FXML private LineChart<String, Number> revenueLineChart;

    // Tables
    @FXML private TableView<Appointment> recentAppointmentsTable;
    @FXML private TableColumn<Appointment, Integer> appointmentIdColumn;
    @FXML private TableColumn<Appointment, String> customerNameColumn;
    @FXML private TableColumn<Appointment, String> vehicleColumn;
    @FXML private TableColumn<Appointment, String> serviceColumn;
    @FXML private TableColumn<Appointment, String> mechanicColumn;
    @FXML private TableColumn<Appointment, String> statusColumn;
    @FXML private TableColumn<Appointment, Double> totalCostColumn;
    @FXML private TableColumn<Appointment, String> scheduledTimeColumn;

    @FXML private TableView<MechanicRequest> mechanicRequestsTable;
    @FXML private TableColumn<MechanicRequest, Integer> requestIdColumn;
    @FXML private TableColumn<MechanicRequest, String> applicantNameColumn;
    @FXML private TableColumn<MechanicRequest, String> specializationColumn;
    @FXML private TableColumn<MechanicRequest, Integer> experienceColumn;
    @FXML private TableColumn<MechanicRequest, Integer> expectedRateColumn;
    @FXML private TableColumn<MechanicRequest, String> phoneColumn;
    @FXML private TableColumn<MechanicRequest, String> emailColumn;
    @FXML private TableColumn<MechanicRequest, String> requestDateColumn;

    @FXML private TableView<TopService> topServicesTable;
    @FXML private TableColumn<TopService, String> serviceNameColumn;
    @FXML private TableColumn<TopService, Integer> serviceCountColumn;
    @FXML private TableColumn<TopService, Double> serviceRevenueColumn;

    private Connection connection;
    private ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
    private ObservableList<MechanicRequest> mechanicRequestList = FXCollections.observableArrayList();
    private ObservableList<TopService> topServiceList = FXCollections.observableArrayList();

    // Model Classes (Inner classes for simplicity)
    public static class Appointment {
        private final int appointmentId;
        private final String customerName;
        private final String vehicleInfo;
        private final String serviceName;
        private final String mechanicName;
        private final String status;
        private final double totalCost;
        private final String scheduledTime;

        public Appointment(int appointmentId, String customerName, String vehicleInfo,
                           String serviceName, String mechanicName, String status,
                           double totalCost, String scheduledTime) {
            this.appointmentId = appointmentId;
            this.customerName = customerName;
            this.vehicleInfo = vehicleInfo;
            this.serviceName = serviceName;
            this.mechanicName = mechanicName;
            this.status = status;
            this.totalCost = totalCost;
            this.scheduledTime = scheduledTime;
        }

        // Getters
        public int getAppointmentId() { return appointmentId; }
        public String getCustomerName() { return customerName; }
        public String getVehicleInfo() { return vehicleInfo; }
        public String getServiceName() { return serviceName; }
        public String getMechanicName() { return mechanicName; }
        public String getStatus() { return status; }
        public double getTotalCost() { return totalCost; }
        public String getScheduledTime() { return scheduledTime; }
    }

    public static class MechanicRequest {
        private final int requestId;
        private final String applicantName;
        private final String specialization;
        private final int experience;
        private final int expectedRate;
        private final String phone;
        private final String email;
        private final String requestDate;

        public MechanicRequest(int requestId, String applicantName, String specialization,
                               int experience, int expectedRate, String phone,
                               String email, String requestDate) {
            this.requestId = requestId;
            this.applicantName = applicantName;
            this.specialization = specialization;
            this.experience = experience;
            this.expectedRate = expectedRate;
            this.phone = phone;
            this.email = email;
            this.requestDate = requestDate;
        }

        // Getters
        public int getRequestId() { return requestId; }
        public String getApplicantName() { return applicantName; }
        public String getSpecialization() { return specialization; }
        public int getExperience() { return experience; }
        public int getExpectedRate() { return expectedRate; }
        public String getPhone() { return phone; }
        public String getEmail() { return email; }
        public String getRequestDate() { return requestDate; }
    }

    public static class TopService {
        private final String serviceName;
        private final int count;
        private final double revenue;

        public TopService(String serviceName, int count, double revenue) {
            this.serviceName = serviceName;
            this.count = count;
            this.revenue = revenue;
        }

        // Getters
        public String getServiceName() { return serviceName; }
        public int getCount() { return count; }
        public double getRevenue() { return revenue; }
    }

    @FXML
    public void initialize() {
        // Initialize database connection (Assuming you have a DatabaseConnection class)
        connection = DataBaseConnection.connect();

        // Initialize TableView columns
        initializeAppointmentsTable();
        initializeMechanicRequestsTable();
        initializeTopServicesTable();

        // Load all dashboard data
        loadDashboardData();

    }

    private void initializeAppointmentsTable() {
        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        vehicleColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleInfo"));
        serviceColumn.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
        mechanicColumn.setCellValueFactory(new PropertyValueFactory<>("mechanicName"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        totalCostColumn.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
        scheduledTimeColumn.setCellValueFactory(new PropertyValueFactory<>("scheduledTime"));

        // Format total cost column
        totalCostColumn.setCellFactory(column -> new TableCell<Appointment, Double>() {
            @Override
            protected void updateItem(Double amount, boolean empty) {
                super.updateItem(amount, empty);
                if (empty || amount == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", amount));
                }
            }
        });

        // Format status column with colors
        statusColumn.setCellFactory(column -> new TableCell<Appointment, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status);
                    getStyleClass().removeAll("status-pending", "status-in-progress",
                            "status-completed", "status-cancelled");

                    switch (status.toUpperCase()) {
                        case "PENDING":
                            getStyleClass().add("status-pending");
                            break;
                        case "IN_PROGRESS":
                            getStyleClass().add("status-in-progress");
                            break;
                        case "COMPLETED":
                            getStyleClass().add("status-completed");
                            break;
                        case "CANCELLED":
                            getStyleClass().add("status-cancelled");
                            break;
                    }
                }
            }
        });

        recentAppointmentsTable.setItems(appointmentList);
    }

    private void initializeMechanicRequestsTable() {
        requestIdColumn.setCellValueFactory(new PropertyValueFactory<>("requestId"));
        applicantNameColumn.setCellValueFactory(new PropertyValueFactory<>("applicantName"));
        specializationColumn.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        experienceColumn.setCellValueFactory(new PropertyValueFactory<>("experience"));
        expectedRateColumn.setCellValueFactory(new PropertyValueFactory<>("expectedRate"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        requestDateColumn.setCellValueFactory(new PropertyValueFactory<>("requestDate"));

        // Format expected rate column
        expectedRateColumn.setCellFactory(column -> new TableCell<MechanicRequest, Integer>() {
            @Override
            protected void updateItem(Integer rate, boolean empty) {
                super.updateItem(rate, empty);
                if (empty || rate == null) {
                    setText(null);
                } else {
                    setText(String.format("$%d/hr", rate));
                }
            }
        });

        // Format experience column
        experienceColumn.setCellFactory(column -> new TableCell<MechanicRequest, Integer>() {
            @Override
            protected void updateItem(Integer experience, boolean empty) {
                super.updateItem(experience, empty);
                if (empty || experience == null) {
                    setText(null);
                } else {
                    setText(experience + " years");
                }
            }
        });

        mechanicRequestsTable.setItems(mechanicRequestList);
    }

    private void initializeTopServicesTable() {
        serviceNameColumn.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
        serviceCountColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
        serviceRevenueColumn.setCellValueFactory(new PropertyValueFactory<>("revenue"));

        // Format revenue column
        serviceRevenueColumn.setCellFactory(column -> new TableCell<TopService, Double>() {
            @Override
            protected void updateItem(Double revenue, boolean empty) {
                super.updateItem(revenue, empty);
                if (empty || revenue == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", revenue));
                }
            }
        });

        topServicesTable.setItems(topServiceList);
    }

    private void loadDashboardData() {
        try {
            // Load all metrics
            loadCustomerMetrics();
            loadVehicleMetrics();
            loadAppointmentMetrics();
            loadMechanicMetrics();
            loadFinancialMetrics();
            loadUserMetrics();
            loadAppointmentStatusChart();
            loadRecentAppointments();
            loadMechanicRequests();
            loadTopServices();
            loadRevenueTrendChart();
            calculateAverageCompletionTime();

        } catch (SQLException e) {
            showAlert("Data Load Error", "Failed to load dashboard data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ============= DATA RETRIEVAL METHODS =============

    private void loadCustomerMetrics() throws SQLException {
        String query = "SELECT COUNT(*) as total_customers, " +
                "(SELECT COUNT(*) FROM customers WHERE created_at >= CURDATE() - INTERVAL 7 DAY) as weekly_new " +
                "FROM customers";

        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                int total = rs.getInt("total_customers");
                int weekly = rs.getInt("weekly_new");
                totalCustomersLabel.setText(String.valueOf(total));
                customerTrendLabel.setText("+" + weekly + " this week");
            }
        }
    }

    private void loadVehicleMetrics() throws SQLException {
        String query = "SELECT COUNT(*) as total_vehicles, " +
                "(SELECT COUNT(*) FROM vehicles WHERE created_at >= CURDATE() - INTERVAL 30 DAY) as monthly_new " +
                "FROM vehicles";

        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                int total = rs.getInt("total_vehicles");
                int monthly = rs.getInt("monthly_new");
                totalVehiclesLabel.setText(String.valueOf(total));
                vehicleTrendLabel.setText("+" + monthly + " this month");
            }
        }
    }

    private void loadAppointmentMetrics() throws SQLException {
        // Today's appointments
        String todayQuery = "SELECT COUNT(*) as today_appointments FROM appointments " +
                "WHERE DATE(scheduled_time) = CURDATE()";

        // Pending appointments
        String pendingQuery = "SELECT COUNT(*) as pending FROM appointments WHERE status = 'PENDING'";

        try (PreparedStatement pstmt1 = connection.prepareStatement(todayQuery);
             PreparedStatement pstmt2 = connection.prepareStatement(pendingQuery);
             ResultSet rs1 = pstmt1.executeQuery();
             ResultSet rs2 = pstmt2.executeQuery()) {

            if (rs1.next()) {
                int today = rs1.getInt("today_appointments");
                todayAppointmentsLabel.setText(String.valueOf(today));
                appointmentTrendLabel.setText(today + " scheduled today");
            }

            if (rs2.next()) {
                int pending = rs2.getInt("pending");
                pendingAppointmentsLabel.setText(String.valueOf(pending));
            }
        }
    }

    private void loadMechanicMetrics() throws SQLException {
        String query = "SELECT " +
                "COUNT(CASE WHEN status = 'AVAILABLE' THEN 1 END) as available, " +
                "COUNT(CASE WHEN status = 'BUSY' THEN 1 END) as busy, " +
                "COUNT(*) as total " +
                "FROM mechanics";

        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                int available = rs.getInt("available");
                int busy = rs.getInt("busy");
                int total = rs.getInt("total");

                availableMechanicsLabel.setText(String.valueOf(available));
                busyMechanicsLabel.setText(String.valueOf(busy));
                mechanicStatusLabel.setText(available + "/" + total + " available");
            }
        }
    }

    private void loadFinancialMetrics() throws SQLException {
        // Total revenue
        String totalRevenueQuery = "SELECT COALESCE(SUM(total_cost - discount_applied), 0) as total_revenue " +
                "FROM appointments WHERE status = 'COMPLETED'";

        // Today's income
        String todayIncomeQuery = "SELECT COALESCE(SUM(total_cost - discount_applied), 0) as today_income " +
                "FROM appointments WHERE DATE(actual_completion) = CURDATE() " +
                "AND status = 'COMPLETED'";

        // This month revenue
        String thisMonthQuery = "SELECT COALESCE(SUM(total_cost - discount_applied), 0) as month_revenue " +
                "FROM appointments WHERE MONTH(actual_completion) = MONTH(CURDATE()) " +
                "AND YEAR(actual_completion) = YEAR(CURDATE()) " +
                "AND status = 'COMPLETED'";

        // Last month revenue
        String lastMonthQuery = "SELECT COALESCE(SUM(total_cost - discount_applied), 0) as last_month_revenue " +
                "FROM appointments WHERE MONTH(actual_completion) = MONTH(CURDATE() - INTERVAL 1 MONTH) " +
                "AND YEAR(actual_completion) = YEAR(CURDATE() - INTERVAL 1 MONTH) " +
                "AND status = 'COMPLETED'";

        try (PreparedStatement pstmt1 = connection.prepareStatement(totalRevenueQuery);
             PreparedStatement pstmt2 = connection.prepareStatement(todayIncomeQuery);
             PreparedStatement pstmt3 = connection.prepareStatement(thisMonthQuery);
             PreparedStatement pstmt4 = connection.prepareStatement(lastMonthQuery);
             ResultSet rs1 = pstmt1.executeQuery();
             ResultSet rs2 = pstmt2.executeQuery();
             ResultSet rs3 = pstmt3.executeQuery();
             ResultSet rs4 = pstmt4.executeQuery()) {

            if (rs1.next()) {
                double totalRevenue = rs1.getDouble("total_revenue");
                totalRevenueLabel.setText(String.format("$%.2f", totalRevenue));
            }

            if (rs2.next()) {
                double todayIncome = rs2.getDouble("today_income");
                todayIncomeLabel.setText(String.format("$%.2f", todayIncome));
            }

            if (rs3.next()) {
                double thisMonthRevenue = rs3.getDouble("month_revenue");
                thisMonthRevenueLabel.setText(String.format("$%.2f", thisMonthRevenue));
            }

            if (rs4.next()) {
                double lastMonthRevenue = rs4.getDouble("last_month_revenue");
                lastMonthRevenueLabel.setText(String.format("$%.2f", lastMonthRevenue));
            }
        }
    }

    private void loadUserMetrics() throws SQLException {
        String query = "SELECT role, COUNT(*) as count FROM users GROUP BY role";

        int adminCount = 0, mechanicCount = 0, customerCount = 0;

        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String role = rs.getString("role");
                int count = rs.getInt("count");

                switch (role) {
                    case "ADMIN":
                        adminCount = count;
                        break;
                    case "MECHANIC":
                        mechanicCount = count;
                        break;
                    case "CUSTOMER":
                        customerCount = count;
                        break;
                }
            }

            int total = adminCount + mechanicCount + customerCount;
            activeUsersLabel.setText(String.valueOf(total));
            userRoleBreakdownLabel.setText(String.format("ADMIN: %d, MECHANIC: %d, CUSTOMER: %d",
                    adminCount, mechanicCount, customerCount));
        }
    }

    private void loadRecentAppointments() throws SQLException {
        appointmentList.clear();

        String query = "SELECT a.appointment_id, c.name as customer_name, " +
                "CONCAT(v.make, ' ', v.model, ' (', v.license_plate, ')') as vehicle_info, " +
                "s.service_name, COALESCE(m.name, 'Not Assigned') as mechanic_name, " +
                "a.status, a.total_cost, DATE_FORMAT(a.scheduled_time, '%d/%m/%Y %H:%i') as scheduled_time " +
                "FROM appointments a " +
                "JOIN customers c ON a.customer_id = c.customer_id " +
                "JOIN vehicles v ON a.vehicle_id = v.vehicle_id " +
                "JOIN services s ON a.service_id = s.service_id " +
                "LEFT JOIN mechanics m ON a.mechanic_id = m.mechanic_id " +
                "ORDER BY a.scheduled_time DESC " +
                "LIMIT 10";

        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Appointment appointment = new Appointment(
                        rs.getInt("appointment_id"),
                        rs.getString("customer_name"),
                        rs.getString("vehicle_info"),
                        rs.getString("service_name"),
                        rs.getString("mechanic_name"),
                        rs.getString("status"),
                        rs.getDouble("total_cost"),
                        rs.getString("scheduled_time")
                );
                appointmentList.add(appointment);
            }
        }
    }

    private void loadMechanicRequests() throws SQLException {
        mechanicRequestList.clear();

        String query = "SELECT * FROM mechanic_requests WHERE status = 'PENDING' ORDER BY created_at DESC";

        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            int pendingCount = 0;
            while (rs.next()) {
                pendingCount++;
                MechanicRequest request = new MechanicRequest(
                        rs.getInt("request_id"),
                        rs.getString("name"),
                        rs.getString("specialization"),
                        rs.getInt("experience"),
                        rs.getInt("hourly_rate"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getTimestamp("created_at").toLocalDateTime().format(
                                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                );
                mechanicRequestList.add(request);
            }

            // Update pending requests label
            pendingRequestsLabel.setText(String.valueOf(pendingCount));
            requestTrendLabel.setText(pendingCount > 0 ? "Requires review" : "No pending requests");
        }
    }

    private void loadTopServices() throws SQLException {
        topServiceList.clear();

        String query = "SELECT s.service_name, COUNT(a.service_id) as appointment_count, " +
                "COALESCE(SUM(a.total_cost - a.discount_applied), 0) as total_revenue " +
                "FROM appointments a " +
                "JOIN services s ON a.service_id = s.service_id " +
                "WHERE a.status = 'COMPLETED' " +
                "GROUP BY s.service_id, s.service_name " +
                "ORDER BY appointment_count DESC " +
                "LIMIT 5";

        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                TopService service = new TopService(
                        rs.getString("service_name"),
                        rs.getInt("appointment_count"),
                        rs.getDouble("total_revenue")
                );
                topServiceList.add(service);
            }
        }
    }

    private void loadAppointmentStatusChart() throws SQLException {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        String query = "SELECT status, COUNT(*) as count FROM appointments GROUP BY status";

        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String status = rs.getString("status");
                int count = rs.getInt("count");

                // Format status for display
                String displayStatus = status.replace("_", " ");
                displayStatus = displayStatus.substring(0, 1).toUpperCase() +
                        displayStatus.substring(1).toLowerCase();

                pieChartData.add(new PieChart.Data(displayStatus + " (" + count + ")", count));
            }

            appointmentPieChart.setData(pieChartData);
            appointmentPieChart.setLabelsVisible(true);
            appointmentPieChart.setLegendVisible(false);
        }
    }

    private void loadRevenueTrendChart() throws SQLException {
        revenueLineChart.getData().clear();

        String query = "SELECT DATE(actual_completion) as date, " +
                "COALESCE(SUM(total_cost - discount_applied), 0) as daily_revenue " +
                "FROM appointments " +
                "WHERE status = 'COMPLETED' " +
                "AND actual_completion >= DATE_SUB(CURDATE(), INTERVAL 30 DAY) " +
                "GROUP BY DATE(actual_completion) " +
                "ORDER BY date";

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Daily Revenue");

        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String date = rs.getDate("date").toString();
                double revenue = rs.getDouble("daily_revenue");
                series.getData().add(new XYChart.Data<>(date, revenue));
            }

            revenueLineChart.getData().add(series);
        }
    }

    private void calculateAverageCompletionTime() throws SQLException {
        String query = "SELECT AVG(TIMESTAMPDIFF(HOUR, scheduled_time, actual_completion)) as avg_hours " +
                "FROM appointments WHERE status = 'COMPLETED' AND actual_completion IS NOT NULL";

        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                double avgHours = rs.getDouble("avg_hours");
                int hours = (int) avgHours;
                int minutes = (int) ((avgHours - hours) * 60);
                avgCompletionTimeLabel.setText(hours + "h " + minutes + "m");
            }
        }
    }

    @FXML
    private void refreshDashboard() {
        loadDashboardData();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}