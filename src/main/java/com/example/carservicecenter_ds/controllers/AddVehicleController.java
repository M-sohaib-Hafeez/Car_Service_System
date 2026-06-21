package com.example.carservicecenter_ds.controllers;

import com.example.carservicecenter_ds.DAO.CustomerDAO;
import com.example.carservicecenter_ds.model.Vehicle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AddVehicleController implements Initializable {

    @FXML private TextField licensePlateField;
    @FXML private ComboBox<String> vehicleTypeComboBox;
    @FXML private TextField makeField;
    @FXML private TextField modelField;
    @FXML private TextField yearField;
    @FXML private Label errorLabel;

    private CustomerDAO customerDAO;
    private int customerId;
    private CustomerBookingController parentController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerDAO = new CustomerDAO();

        // Populate vehicle types
        vehicleTypeComboBox.getItems().addAll(
                "Sedan",
                "SUV",
                "Hatchback",
                "Truck",
                "Van",
                "Coupe",
                "Convertible",
                "Motorcycle"
        );

        // Only allow numbers in year field
        yearField.textProperty().addListener((obs, oldText, newText) -> {
            if (!newText.matches("\\d*")) {
                yearField.setText(newText.replaceAll("[^\\d]", ""));
            }
            if (newText.length() > 4) {
                yearField.setText(newText.substring(0, 4));
            }
        });
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setParentController(CustomerBookingController controller) {
        this.parentController = controller;
    }

    @FXML
    private void handleAddVehicle() {
        if (!validateForm()) {
            return;
        }

        try {
            Vehicle vehicle = new Vehicle();
            vehicle.setCustomerId(customerId);
            vehicle.setLicensePlate(licensePlateField.getText().trim().toUpperCase());
            vehicle.setVehicleType(vehicleTypeComboBox.getSelectionModel().getSelectedItem());
            vehicle.setMake(makeField.getText().trim());
            vehicle.setModel(modelField.getText().trim());
            vehicle.setYear(Integer.parseInt(yearField.getText().trim()));

            int vehicleId = customerDAO.addVehicle(vehicle);

            if (vehicleId > 0) {
                // Success - close the window
                Stage stage = (Stage) licensePlateField.getScene().getWindow();
                stage.close();
            } else {
                showError("Failed to add vehicle. License plate may already exist.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showError("An error occurred: " + e.getMessage());
        }
    }

    private boolean validateForm() {
        // License plate
        if (licensePlateField.getText().trim().isEmpty()) {
            showError("License plate is required");
            return false;
        }

        // Vehicle type
        if (vehicleTypeComboBox.getSelectionModel().getSelectedItem() == null) {
            showError("Please select a vehicle type");
            return false;
        }

        // Make
        if (makeField.getText().trim().isEmpty()) {
            showError("Make is required");
            return false;
        }

        // Model
        if (modelField.getText().trim().isEmpty()) {
            showError("Model is required");
            return false;
        }

        // Year
        if (yearField.getText().trim().isEmpty()) {
            showError("Year is required");
            return false;
        }

        try {
            int year = Integer.parseInt(yearField.getText().trim());
            int currentYear = java.time.Year.now().getValue();

            if (year < 1900 || year > currentYear + 1) {
                showError("Please enter a valid year (1900-" + (currentYear + 1) + ")");
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Year must be a valid number");
            return false;
        }

        return true;
    }

    private void showError(String message) {
        errorLabel.setText("✗ " + message);
        errorLabel.setVisible(true);
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) licensePlateField.getScene().getWindow();
        stage.close();
    }
}