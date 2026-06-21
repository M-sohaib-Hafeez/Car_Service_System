package com.example.carservicecenter_ds.controllers;

import com.example.carservicecenter_ds.DAO.CustomerDAO;
import com.example.carservicecenter_ds.DAO.MechanicDAO;
import com.example.carservicecenter_ds.DAO.MechanicRequestDAO;
import com.example.carservicecenter_ds.DAO.UserDAO;
import com.example.carservicecenter_ds.model.Customer;
import com.example.carservicecenter_ds.model.Mechanic;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {

    @FXML
    private ImageView carimage;

    @FXML
    private ComboBox<String> roleComboBox;
    @FXML
    private VBox mechanicOverlay;

    @FXML private ComboBox<String> specializationCombo;
    @FXML private TextField experienceField;
    @FXML private TextField phoneField;
    @FXML private TextField hourlyRateField;
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            carimage.setImage(new Image(getClass().getResourceAsStream("/images/carlogin.png")));
        } catch (Exception e) {
            System.out.println("Car image not found");
        }
        
        roleComboBox.getItems().addAll("CUSTOMER", "MECHANIC");

        specializationCombo.getItems().addAll(
                "Engine Repair",
                "Brake Services",
                "Electrical Systems",
                "Oil & Maintenance",
                "AC & Cooling",
                "General Diagnostics"
        );

        roleComboBox.setOnAction(e -> handleRoleChange());
        TranslateTransition carEnter = new TranslateTransition(Duration.seconds(1.0), carimage);
        carEnter.setToX(-150);
        carEnter.setInterpolator(Interpolator.EASE_OUT);
        carEnter.play();
    }

    private void handleRoleChange() {
        String role = roleComboBox.getValue();
        if ("MECHANIC".equals(role)) {
            showMechanicOverlay();
        } else {
            hideMechanicOverlay();
        }
    }

    @FXML
    private void showMechanicOverlay() {
        mechanicOverlay.setVisible(true);
        mechanicOverlay.setManaged(true);
    }

    @FXML
    private void hideMechanicOverlay() {
        mechanicOverlay.setVisible(false);
        mechanicOverlay.setManaged(false);
    }

    public void OpenLoginPage(MouseEvent e){
        try {
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root= loader.load();
            Scene scene=new Scene(root,1100,650);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            Stage stage= (Stage)((Node)e.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException exp) {
            throw new RuntimeException(exp);
        }
    }

    @FXML
    private void handleSignUp() {
        String role = roleComboBox.getValue();

        if (role == null) {
            showError("Please select a role");
            return;
        }

        if (!validateBasicFields()) return;

        if ("CUSTOMER".equals(role)) {
            registerCustomer();
        } else if ("MECHANIC".equals(role)) {
            if (!validateMechanicFields()) return;
            registerMechanic();
        }
    }

    private void registerCustomer() {
        try {
            // Step 1: Create customer record first
            CustomerDAO customerDAO = new CustomerDAO();
            Customer customer = new Customer();
            customer.setName(nameField.getText().trim());
            customer.setEmail(emailField.getText().trim());
            customer.setPhone(phoneField.getText().trim());
            customer.setAddress(""); // Empty address initially
            customer.setLoyaltyPoints(0);

            int customerId = customerDAO.addCustomer(customer);

            if (customerId == -1) {
                showError("Failed to create customer account. Email or Phone might already exist.");
                return;
            }

            // Step 2: Create user account linked to customer
            UserDAO userDAO = new UserDAO();
            
            // Use email as username to ensure uniqueness, or append random number
            String username = nameField.getText().trim();
            
            int userId = userDAO.createUserWithCustomerId(
                    username,
                    emailField.getText().trim(),
                    passwordField.getText().trim(),
                    "CUSTOMER",
                    customerId  // Link to customer
            );

            if (userId == -1) {
                showError("Failed to create user account. Username might be taken.");
                return;
            }

            // Success
            showInfo(
                    "Registration Successful",
                    "Your customer account has been created!\nYou can now login."
            );

            clearForm();

        } catch (Exception e) {
            e.printStackTrace();
            showError("An error occurred: " + e.getMessage());
        }
    }

    private void registerMechanic() {
        try {
            // Step 1: Create user account first
            UserDAO userDAO = new UserDAO();
            int userId = userDAO.createUser(
                    nameField.getText().trim(),
                    emailField.getText().trim(),
                    passwordField.getText().trim(),
                    "MECHANIC"
            );

            if (userId == -1) {
                showError("Failed to create user account. Username or Email might be taken.");
                return;
            }

            // Step 2: Create mechanic request
            Mechanic mechanic = new Mechanic();
            mechanic.setName(nameField.getText().trim());
            mechanic.setEmail(emailField.getText().trim());
            mechanic.setSpecialization(specializationCombo.getValue());
            mechanic.setExperience(Integer.parseInt(experienceField.getText().trim()));
            mechanic.setPhone(phoneField.getText().trim());
            mechanic.setHourlyRate(Double.parseDouble(hourlyRateField.getText().trim()));

            MechanicRequestDAO requestDAO = new MechanicRequestDAO();
            boolean success = requestDAO.insertRequest(mechanic, userId);

            if (success) {
                hideMechanicOverlay();
                showInfo(
                        "Registration Submitted",
                        "Your mechanic registration has been submitted.\nPlease wait for admin approval to login."
                );
                clearForm();
            } else {
                showError("Failed to submit mechanic request");
            }
        } catch (NumberFormatException e) {
            showError("Please enter valid numbers for experience and hourly rate");
        } catch (Exception e) {
            e.printStackTrace();
            showError("An error occurred: " + e.getMessage());
        }
    }

    private boolean validateMechanicFields() {
        if (specializationCombo.getValue() == null
                || experienceField.getText().trim().isEmpty()
                || hourlyRateField.getText().trim().isEmpty()) {

            showError("Please complete all mechanic fields");
            return false;
        }

        try {
            Integer.parseInt(experienceField.getText().trim());
            Double.parseDouble(hourlyRateField.getText().trim());
        } catch (NumberFormatException e) {
            showError("Experience and Hourly Rate must be valid numbers");
            return false;
        }

        return true;
    }

    private boolean validateBasicFields() {
        if (nameField.getText().trim().isEmpty()
                || emailField.getText().trim().isEmpty()
                || passwordField.getText().trim().isEmpty()
                || phoneField.getText().trim().isEmpty()) {

            showError("All fields are required");
            return false;
        }

        // Basic email validation
        if (!emailField.getText().trim().contains("@")) {
            showError("Please enter a valid email address");
            return false;
        }

        return true;
    }

    private void clearForm() {
        nameField.clear();
        emailField.clear();
        passwordField.clear();
        roleComboBox.getSelectionModel().clearSelection();
        specializationCombo.getSelectionModel().clearSelection();
        experienceField.clear();
        phoneField.clear();
        hourlyRateField.clear();
    }

    private void showInfo(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}