package com.example.carservicecenter_ds.controllers;

import com.example.carservicecenter_ds.DAO.AdminDAO;
import com.example.carservicecenter_ds.model.ServiceAppointment;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class adminAppointmentController implements Initializable {
    @FXML
    private FlowPane AppointmentRoot;

    public static VBox createAppointmentCard(ServiceAppointment appointment) {
        VBox card = new VBox();
        card.getStyleClass().add("appointment-card");

        // Get formatted status string
        String status = appointment.getStatus();
        String formattedStatus = formatStatus(status);
        String statusClass = status.toLowerCase().replace("_", "-");

        // Apply status-based styling
        card.getStyleClass().add("status-" + statusClass);

        // Add status indicator as the first element
        Pane statusIndicator = new Pane();
        statusIndicator.getStyleClass().addAll("status-indicator", "status-" + statusClass);

        // Main content container
        VBox content = new VBox();
        content.getStyleClass().add("card-content");

        // Header section with customer and cost
        HBox headerBox = new HBox();
        headerBox.getStyleClass().add("card-header");

        Label customerLabel = new Label(appointment.getCustomerName());
        customerLabel.getStyleClass().add("customer-name");

        Label costLabel = new Label(String.format("$%.2f", appointment.getTotalCost()));
        costLabel.getStyleClass().add("total-cost");

        headerBox.getChildren().addAll(customerLabel, costLabel);
        HBox.setHgrow(customerLabel, Priority.ALWAYS);

        // Vehicle and service section
        VBox serviceDetails = new VBox();
        serviceDetails.getStyleClass().add("service-details");

        // Vehicle info with status badge
        HBox vehicleSection = new HBox();
        vehicleSection.setSpacing(8);

        Label vehicleLabel = new Label(appointment.getVehicleInfo());
        vehicleLabel.getStyleClass().add("vehicle-info");

        Label statusLabel = new Label(formattedStatus);
        statusLabel.getStyleClass().addAll("status-badge", "status-" + statusClass);

        vehicleSection.getChildren().addAll(vehicleLabel, statusLabel);

        Label serviceLabel = new Label(appointment.getServiceName());
        serviceLabel.getStyleClass().add("service-name");

        serviceDetails.getChildren().addAll(vehicleSection, serviceLabel);

        // Mechanic, time, and priority section
        HBox footerBox = new HBox();
        footerBox.getStyleClass().add("card-footer");

        Label mechanicLabel = new Label(appointment.getMechanicName());
        mechanicLabel.getStyleClass().add("mechanic-name");

        Label timeLabel = new Label(formatTime(appointment.getScheduledTime()));
        timeLabel.getStyleClass().add("scheduled-time");

        // Priority badge
        String priority = appointment.getPriorityText();
        if (priority != null && !priority.isEmpty()) {
            String priorityClass = priority.toLowerCase().replace(" ", "-");
            Label priorityLabel = new Label(priority);
            priorityLabel.getStyleClass().addAll("priority-badge", "priority-" + priorityClass);
            footerBox.getChildren().addAll(mechanicLabel, timeLabel, priorityLabel);
        } else {
            footerBox.getChildren().addAll(mechanicLabel, timeLabel);
        }

        HBox.setHgrow(mechanicLabel, Priority.ALWAYS);

        // Notes section (if any)
        if (appointment.getNotes() != null && !appointment.getNotes().isEmpty()) {
            VBox notesContainer = new VBox();
            notesContainer.getStyleClass().add("notes-container");

            Label notesTitle = new Label("Notes:");
            notesTitle.getStyleClass().add("notes-title");

            // Use TextArea for notes with scrolling if needed
            TextArea notesContent = new TextArea(appointment.getNotes());
            notesContent.getStyleClass().add("notes-content");
            notesContent.setEditable(false);
            notesContent.setWrapText(true);
            notesContent.setPrefRowCount(2);
            notesContent.setMaxHeight(50);

            notesContainer.getChildren().addAll(notesTitle, notesContent);
            content.getChildren().addAll(headerBox, serviceDetails, footerBox, notesContainer);
        } else {
            content.getChildren().addAll(headerBox, serviceDetails, footerBox);
        }

        // Add all elements to card
        card.getChildren().addAll(statusIndicator, content);

        // Add hover effect
        setupHoverEffect(card);

        return card;
    }

    private static String formatTime(LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy • hh:mm a");
        return time.format(formatter);
    }

    private static String formatStatus(String status) {
        if (status == null) return "Unknown";

        // Convert from enum style to display style
        return status.replace("_", " ").toLowerCase()
                .replace("pending", "Pending")
                .replace("scheduled", "Scheduled")
                .replace("in-progress", "In Progress")
                .replace("in_progress", "In Progress")
                .replace("completed", "Completed")
                .replace("cancelled", "Cancelled");
    }

    private static void setupHoverEffect(VBox card) {
        card.setOnMouseEntered(e -> {
            card.getStyleClass().add("card-hover");
            card.setStyle("-fx-border-color: #4dabf7;");
        });

        card.setOnMouseExited(e -> {
            card.getStyleClass().remove("card-hover");
            card.setStyle("");
        });

        // Click effect
        card.setOnMousePressed(e -> {
            card.setStyle("-fx-border-color: #339af0; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.15), 8, 0, 0, 1);");
        });

        card.setOnMouseReleased(e -> {
            if (!card.getStyleClass().contains("card-hover")) {
                card.setStyle("");
            } else {
                card.setStyle("-fx-border-color: #4dabf7;");
            }
        });
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AppointmentRoot.getChildren().clear();
        List<ServiceAppointment> serviceAppointmentList= AdminDAO.getAllAppointments();
        for(ServiceAppointment x : serviceAppointmentList){
            AppointmentRoot.getChildren().add(createAppointmentCard(x));
        }
    }
}
