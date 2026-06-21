package com.example.carservicecenter_ds.controllers;

import com.example.carservicecenter_ds.DAO.AdminDAO;
import com.example.carservicecenter_ds.DAO.CustomerDAO;
import com.example.carservicecenter_ds.model.Service;
import com.example.carservicecenter_ds.model.ServiceAppointment;
import com.example.carservicecenter_ds.model.Vehicle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

public class CustomerBookingController implements Initializable {

    @FXML private ComboBox<Vehicle> vehicleComboBox;
    @FXML private ComboBox<Service> serviceComboBox;
    @FXML private DatePicker appointmentDatePicker;
    @FXML private ComboBox<String> timeComboBox;
    @FXML private ComboBox<String> priorityComboBox;
    @FXML private TextArea notesTextArea;
    @FXML private Label messageLabel;
    @FXML private Label serviceDetailsLabel;
    @FXML private Label servicePriceLabel;
    @FXML private Button bookButton;

    private CustomerDAO customerDAO;
    private int customerId;
    private CustomerController parentController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerDAO = new CustomerDAO();

        setupTimeSlots();
        setupPriorityLevels();
        setupServiceListener();

        // Set minimum date to today
        appointmentDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
        loadCustomerVehicles();
        loadAvailableServices();
    }

    public void setParentController(CustomerController controller) {
        this.parentController = controller;
    }

    private void loadCustomerVehicles() {
        List<Vehicle> vehicles = customerDAO.getCustomerVehicles(customerId);
        vehicleComboBox.getItems().clear();
        vehicleComboBox.getItems().addAll(vehicles);

        if (!vehicles.isEmpty()) {
            vehicleComboBox.getSelectionModel().selectFirst();
        }
    }

    private void loadAvailableServices() {
        List<Service> services = AdminDAO.getAllServices();
        serviceComboBox.getItems().clear();
        serviceComboBox.getItems().addAll(services);
    }

    private void setupTimeSlots() {
        timeComboBox.getItems().addAll(
                "09:00 AM", "10:00 AM", "11:00 AM",
                "12:00 PM", "01:00 PM", "02:00 PM",
                "03:00 PM", "04:00 PM", "05:00 PM"
        );
    }

    private void setupPriorityLevels() {
        priorityComboBox.getItems().addAll(
                "Emergency (Priority 1)",
                "High (Priority 2)",
                "Normal (Priority 3)",
                "Low (Priority 4)"
        );
        priorityComboBox.getSelectionModel().select(2); // Default to Normal
    }

    private void setupServiceListener() {
        serviceComboBox.setOnAction(event -> {
            Service selected = serviceComboBox.getSelectionModel().getSelectedItem();
            if (selected != null) {
                serviceDetailsLabel.setText(selected.getDescription());
                servicePriceLabel.setText("Base Price: ₨ " + String.format("%.2f", selected.getBasePrice()) +
                        " | Est. Time: " + selected.getEstimatedTime() + " mins");
            }
        });
    }

    @FXML
    private void openAddVehicleForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/add-vehicle-form.fxml"));
            Parent root = loader.load();

            AddVehicleController controller = loader.getController();
            controller.setCustomerId(customerId);
            controller.setParentController(this);

            Stage stage = new Stage();
            stage.setTitle("Add New Vehicle");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Reload vehicles after adding
            loadCustomerVehicles();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBookAppointment() {
        // Validation
        if (!validateForm()) {
            return;
        }

        try {
            ServiceAppointment appointment = new ServiceAppointment();

            Vehicle selectedVehicle = vehicleComboBox.getSelectionModel().getSelectedItem();
            Service selectedService = serviceComboBox.getSelectionModel().getSelectedItem();
            LocalDate selectedDate = appointmentDatePicker.getValue();
            String selectedTime = timeComboBox.getSelectionModel().getSelectedItem();

            // Parse time
            LocalTime time = parseTimeString(selectedTime);
            LocalDateTime scheduledDateTime = LocalDateTime.of(selectedDate, time);

            // Get priority level
            int priority = priorityComboBox.getSelectionModel().getSelectedIndex() + 1;

            // Set appointment details
            appointment.setCustomerId(customerId);
            appointment.setVehicleId(selectedVehicle.getVehicleId());
            appointment.setServiceId(selectedService.getServiceId());
            appointment.setScheduledTime(scheduledDateTime);
            appointment.setStatus("PENDING");
            appointment.setPriorityLevel(priority);
            appointment.setTotalCost(selectedService.getBasePrice());
            appointment.setNotes(notesTextArea.getText());

            // Calculate estimated completion
            LocalDateTime estimatedCompletion = scheduledDateTime.plusMinutes(selectedService.getEstimatedTime());
            appointment.setEstimatedCompletion(estimatedCompletion);

            // Book the appointment
            boolean success = customerDAO.bookAppointment(appointment);

            if (success) {
                showSuccessMessage("Appointment booked successfully!");

                // Refresh parent dashboard
                if (parentController != null) {
                    parentController.refreshDashboard();
                }

                // Close window after 2 seconds
                new Thread(() -> {
                    try {
                        Thread.sleep(2000);
                        javafx.application.Platform.runLater(() -> {
                            Stage stage = (Stage) bookButton.getScene().getWindow();
                            stage.close();
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            } else {
                showErrorMessage("Failed to book appointment. Please try again.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showErrorMessage("An error occurred: " + e.getMessage());
        }
    }

    private boolean validateForm() {
        if (vehicleComboBox.getSelectionModel().getSelectedItem() == null) {
            showErrorMessage("Please select a vehicle");
            return false;
        }

        if (serviceComboBox.getSelectionModel().getSelectedItem() == null) {
            showErrorMessage("Please select a service");
            return false;
        }

        if (appointmentDatePicker.getValue() == null) {
            showErrorMessage("Please select a date");
            return false;
        }

        if (timeComboBox.getSelectionModel().getSelectedItem() == null) {
            showErrorMessage("Please select a time");
            return false;
        }

        // Check if date is not in the past
        LocalDate selectedDate = appointmentDatePicker.getValue();
        if (selectedDate.isBefore(LocalDate.now())) {
            showErrorMessage("Cannot book appointments in the past");
            return false;
        }

        return true;
    }

    private LocalTime parseTimeString(String timeString) {
        // Parse "09:00 AM" format
        timeString = timeString.replace(" ", "");
        boolean isPM = timeString.contains("PM");
        timeString = timeString.replace("AM", "").replace("PM", "");

        String[] parts = timeString.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);

        if (isPM && hour != 12) {
            hour += 12;
        } else if (!isPM && hour == 12) {
            hour = 0;
        }

        return LocalTime.of(hour, minute);
    }

    private void showSuccessMessage(String message) {
        messageLabel.setText("✓ " + message);
        messageLabel.setStyle("-fx-background-color: #d1fae5; -fx-background-radius: 5; " +
                "-fx-padding: 10; -fx-text-fill: #065f46;");
        messageLabel.setVisible(true);
    }

    private void showErrorMessage(String message) {
        messageLabel.setText("✗ " + message);
        messageLabel.setStyle("-fx-background-color: #fee2e2; -fx-background-radius: 5; " +
                "-fx-padding: 10; -fx-text-fill: #dc2626;");
        messageLabel.setVisible(true);
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) bookButton.getScene().getWindow();
        stage.close();
    }
}