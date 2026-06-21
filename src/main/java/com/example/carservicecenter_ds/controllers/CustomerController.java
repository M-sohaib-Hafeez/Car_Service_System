package com.example.carservicecenter_ds.controllers;

import com.example.carservicecenter_ds.DAO.CustomerDAO;
import com.example.carservicecenter_ds.model.Customer;
import com.example.carservicecenter_ds.model.ServiceAppointment;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class CustomerController implements Initializable {

    @FXML private Label welcomeLabel;
    @FXML private VBox currentServiceCard;
    @FXML private Label currentServiceName;
    @FXML private Label currentVehicleInfo;
    @FXML private Label currentStatusLabel;
    @FXML private Label currentPriorityLabel;
    @FXML private Label currentCostLabel;
    @FXML private Label currentScheduledTime;
    @FXML private Label currentNotes;

    @FXML private HBox queueCard;
    @FXML private Label queuePositionLabel;

    @FXML private VBox noServiceCard;
    @FXML private VBox historyContainer;
    @FXML private Label loyaltyPointsLabel;
    @FXML private Button logoutBtn;

    private CustomerDAO customerDAO;
    private Customer currentCustomer;
    private int customerId = 1; // This should be passed from login

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerDAO = new CustomerDAO();
        loadCustomerData();
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
        loadCustomerData();
    }

    private void loadCustomerData() {
        currentCustomer = customerDAO.getCustomerById(customerId);

        if (currentCustomer != null) {
            welcomeLabel.setText("Welcome Back, " + currentCustomer.getName() + "!");
            loyaltyPointsLabel.setText(String.valueOf(currentCustomer.getLoyaltyPoints()));

            loadCurrentService();
            loadServiceHistory();
        }
    }

    private void loadCurrentService() {
        ServiceAppointment currentAppointment = customerDAO.getCurrentAppointment(customerId);

        if (currentAppointment != null) {
            // Show current service card
            currentServiceCard.setVisible(true);
            noServiceCard.setVisible(false);

            // Populate current service details
            currentServiceName.setText(currentAppointment.getServiceName());
            currentVehicleInfo.setText(currentAppointment.getVehicleInfo());

            // Status with color
            currentStatusLabel.setText("Status: " + currentAppointment.getStatus());
            currentStatusLabel.setTextFill(Color.web(currentAppointment.getStatusColor()));

            // Priority with color
            currentPriorityLabel.setText("Priority: " + currentAppointment.getPriorityText());
            currentPriorityLabel.setTextFill(Color.web(currentAppointment.getPriorityColor()));

            currentCostLabel.setText("₨ " + String.format("%.2f", currentAppointment.getTotalCost()));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, h:mm a");
            currentScheduledTime.setText("Scheduled: " +
                    currentAppointment.getScheduledTime().format(formatter));

            if (currentAppointment.getNotes() != null && !currentAppointment.getNotes().isEmpty()) {
                currentNotes.setText("Notes: " + currentAppointment.getNotes());
            } else {
                currentNotes.setText("Notes: None");
            }

            // Check if in queue
            if ("PENDING".equals(currentAppointment.getStatus())) {
                int position = customerDAO.getQueuePosition(customerId);
                if (position > 0) {
                    queueCard.setVisible(true);
                    queuePositionLabel.setText("#" + position);
                }
            } else {
                queueCard.setVisible(false);
            }
        } else {
            // No current service
            currentServiceCard.setVisible(false);
            queueCard.setVisible(false);
            noServiceCard.setVisible(true);
        }
    }

    private void loadServiceHistory() {
        historyContainer.getChildren().clear();
        List<ServiceAppointment> history = customerDAO.getCustomerAppointments(customerId);

        if (history.isEmpty()) {
            Label noHistory = new Label("No service history yet");
            noHistory.setStyle("-fx-text-fill: #6b7280;");
            historyContainer.getChildren().add(noHistory);
            return;
        }

        // Show last 5 appointments
        int count = 0;
        for (ServiceAppointment appointment : history) {
            if (count >= 5) break;

            // Skip current appointment if it's in the list
            if (appointment.getStatus().equals("PENDING") ||
                    appointment.getStatus().equals("IN_PROGRESS")) {
                continue;
            }

            historyContainer.getChildren().add(createHistoryCard(appointment));
            count++;
        }
    }

    private VBox createHistoryCard(ServiceAppointment appointment) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-background-color: #f9fafb; -fx-border-color: #e5e7eb; " +
                "-fx-border-radius: 5; -fx-background-radius: 5;");

        // Service name
        Label serviceName = new Label(appointment.getServiceName());
        serviceName.setFont(Font.font("System", FontWeight.BOLD, 14));
        serviceName.setTextFill(Color.web("#1f2933"));

        // Vehicle info
        Label vehicleInfo = new Label(appointment.getVehicleInfo());
        vehicleInfo.setStyle("-fx-text-fill: #4b5563;");
        vehicleInfo.setFont(Font.font(12));

        // Date and status row
        HBox detailsRow = new HBox(15);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
        Label dateLabel = new Label("📅 " + appointment.getScheduledTime().format(formatter));
        dateLabel.setStyle("-fx-text-fill: #6b7280;");
        dateLabel.setFont(Font.font(11));

        Label statusLabel = new Label(appointment.getStatus());
        statusLabel.setStyle("-fx-text-fill: " + appointment.getStatusColor() + "; -fx-font-weight: bold;");
        statusLabel.setFont(Font.font(11));

        Label costLabel = new Label("₨ " + String.format("%.2f", appointment.getTotalCost()));
        costLabel.setStyle("-fx-text-fill: #12372a; -fx-font-weight: bold;");
        costLabel.setFont(Font.font(12));

        detailsRow.getChildren().addAll(dateLabel, statusLabel, costLabel);

        card.getChildren().addAll(serviceName, vehicleInfo, detailsRow);

        return card;
    }

    @FXML
    public void openBookingForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/customer-booking.fxml"));
            Parent root = loader.load();

            CustomerBookingController controller = loader.getController();
            controller.setCustomerId(customerId);
            controller.setParentController(this);

            Stage stage = new Stage();
            stage.setTitle("Book Service Appointment");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void refreshDashboard() {
        loadCustomerData();
    }

    @FXML
    private void viewHistory() {
        // You can create a separate detailed history view
        System.out.println("View full history clicked");
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) logoutBtn.getScene().getWindow();
            Scene scene = new Scene(root, 1100, 650);
            scene.getStylesheets().add(
                    getClass().getResource("/css/styles.css").toExternalForm()
            );
            stage.setMaximized(false);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setTitle("Car Service Center - Login");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}