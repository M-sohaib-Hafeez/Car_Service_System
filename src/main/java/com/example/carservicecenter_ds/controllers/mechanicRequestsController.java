package com.example.carservicecenter_ds.controllers;

import com.example.carservicecenter_ds.DAO.AdminDAO;
import com.example.carservicecenter_ds.model.mechanicRequests;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class mechanicRequestsController implements Initializable {

    private adminMechanicController controller;

    public void setMechanicController(adminMechanicController controller){
        this.controller=controller;
    }

    @FXML
    VBox mechanicRequestRoot;



    public HBox createMechanicRequestCard(mechanicRequests mr) {
        // Create main horizontal card
        HBox card = new HBox(20);
        card.getStyleClass().add("request-card");
        card.setPadding(new Insets(15));
        card.setAlignment(Pos.CENTER_LEFT);

        // 🔹 Left side: Info container
        VBox infoBox = new VBox(8);
        infoBox.getStyleClass().add("info-container");
        infoBox.setPrefWidth(300);

        // Header with name and experience
        HBox headerBox = new HBox(10);
        headerBox.getStyleClass().add("header-box");
        headerBox.setAlignment(Pos.CENTER_LEFT);

        // Profile icon/avatar
        StackPane avatar = new StackPane();
        avatar.getStyleClass().add("mechanic-avatar");

        // First letter of name as avatar text
        String firstLetter = mr.getName().substring(0, 1).toUpperCase();
        Label avatarText = new Label(firstLetter);
        avatarText.getStyleClass().add("avatar-text");
        avatar.getChildren().add(avatarText);

        // Name label with proper wrapping
        Label nameLbl = new Label(mr.getName());
        nameLbl.getStyleClass().add("mechanic-name");
        nameLbl.setWrapText(true);
        nameLbl.setMaxWidth(180);

        // Add tooltip for full name if truncated
        if (mr.getName().length() > 15) {
            nameLbl.setTooltip(new Tooltip(mr.getName()));
        }

        // Experience badge
        Label expBadge = new Label(mr.getExp() + " yrs");
        expBadge.getStyleClass().add("experience-badge");

        headerBox.getChildren().addAll(avatar, nameLbl, expBadge);

        // Contact info with icons
        VBox contactBox = new VBox(5);
        contactBox.getStyleClass().add("contact-box");

        // Email with icon
        HBox emailBox = new HBox(8);
        emailBox.setAlignment(Pos.CENTER_LEFT);

        SVGPath emailIcon = new SVGPath();
        emailIcon.getStyleClass().add("contact-icon");
        emailIcon.setContent("M20 4H4c-1.1 0-1.99.9-1.99 2L2 18c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V6c0-1.1-.9-2-2-2zm0 4l-8 5-8-5V6l8 5 8-5v2z");

        Label emailLbl = new Label(mr.getEmail());
        emailLbl.getStyleClass().add("contact-text");
        emailLbl.setWrapText(true);
        emailLbl.setMaxWidth(230);

        // Add tooltip for full email
        if (mr.getEmail().length() > 25) {
            emailLbl.setTooltip(new Tooltip(mr.getEmail()));
        }

        emailBox.getChildren().addAll(emailIcon, emailLbl);

        // Phone with icon
        HBox phoneBox = new HBox(8);
        phoneBox.setAlignment(Pos.CENTER_LEFT);

        SVGPath phoneIcon = new SVGPath();
        phoneIcon.getStyleClass().add("contact-icon");
        phoneIcon.setContent("M6.62 10.79c1.44 2.83 3.76 5.14 6.59 6.59l2.2-2.2c.27-.27.67-.36 1.02-.24 1.12.37 2.33.57 3.57.57.55 0 1 .45 1 1V20c0 .55-.45 1-1 1-9.39 0-17-7.61-17-17 0-.55.45-1 1-1h3.5c.55 0 1 .45 1 1 0 1.25.2 2.45.57 3.57.11.35.03.74-.25 1.02l-2.2 2.2z");

        Label phoneLbl = new Label(mr.getPhone());
        phoneLbl.getStyleClass().add("contact-text");
        phoneLbl.setWrapText(true);
        phoneLbl.setMaxWidth(230);

        phoneBox.getChildren().addAll(phoneIcon, phoneLbl);

        // Specialization with icon
        HBox specBox = new HBox(8);
        specBox.setAlignment(Pos.CENTER_LEFT);

        SVGPath specIcon = new SVGPath();
        specIcon.getStyleClass().add("contact-icon");
        specIcon.setContent("M9.4 16.6L4.8 12l4.6-4.6L8 6l-6 6 6 6 1.4-1.4zm5.2 0l4.6-4.6-4.6-4.6L16 6l6 6-6 6-1.4-1.4z");

        Label specLbl = new Label(mr.getSpecialization());
        specLbl.getStyleClass().add("specialization-text");
        specLbl.setWrapText(true);
        specLbl.setMaxWidth(230);

        // Add tooltip for long specialization
        if (mr.getSpecialization().length() > 20) {
            specLbl.setTooltip(new Tooltip(mr.getSpecialization()));
        }

        specBox.getChildren().addAll(specIcon, specLbl);

        // Hourly rate with icon
        HBox rateBox = new HBox(8);
        rateBox.setAlignment(Pos.CENTER_LEFT);

        SVGPath rateIcon = new SVGPath();
        rateIcon.getStyleClass().add("contact-icon");
        rateIcon.setContent("M11.8 10.9c-2.27-.59-3-1.2-3-2.15 0-1.09 1.01-1.85 2.7-1.85 1.78 0 2.44.85 2.5 2.1h2.21c-.07-1.72-1.12-3.3-3.21-3.81V3h-3v2.16c-1.94.42-3.5 1.68-3.5 3.61 0 2.31 1.91 3.46 4.7 4.13 2.5.6 3 1.48 3 2.41 0 .69-.49 1.79-2.7 1.79-2.06 0-2.87-.92-2.98-2.1h-2.2c.12 2.19 1.76 3.42 3.68 3.83V21h3v-2.15c1.95-.37 3.5-1.5 3.5-3.55 0-2.84-2.43-3.81-4.7-4.4z");

        Label hourlyRateLbl = new Label("$" + mr.getHourlyRate() + "/hour");
        hourlyRateLbl.getStyleClass().add("rate-text");
        hourlyRateLbl.setWrapText(true);
        hourlyRateLbl.setMaxWidth(230);

        rateBox.getChildren().addAll(rateIcon, hourlyRateLbl);

        contactBox.getChildren().addAll(emailBox, phoneBox, specBox, rateBox);

        // Request timestamp
        Label timestamp = new Label("Request received: Just now");
        timestamp.getStyleClass().add("timestamp");

        // Add all to info box
        infoBox.getChildren().addAll(headerBox, contactBox, timestamp);

        // 🔹 Right side: Action buttons container
        VBox actionBox = new VBox(10);
        actionBox.getStyleClass().add("action-container");
        actionBox.setAlignment(Pos.CENTER);
        actionBox.setPrefWidth(150);

        // Approve button with icon
        Button approveBtn = new Button("Approve");
        approveBtn.getStyleClass().addAll("action-btn", "approve-btn");
        approveBtn.setPrefWidth(130);

        SVGPath approveIcon = new SVGPath();
        approveIcon.setContent("M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z");
        approveIcon.getStyleClass().add("btn-icon");
        approveBtn.setGraphic(approveIcon);
        approveBtn.setContentDisplay(ContentDisplay.LEFT);
        approveBtn.setGraphicTextGap(8);

        approveBtn.setOnAction(e -> {
            AdminDAO.addMechanic(mr);
            AdminDAO.deleteMechanicRequest(mr.getRequestID());
            controller.refreshMechanics();
            referesgMechanicReuests();
        });

        // Reject button with icon
        Button rejectBtn = new Button("Reject");
        rejectBtn.getStyleClass().addAll("action-btn", "reject-btn");
        rejectBtn.setPrefWidth(130);

        SVGPath rejectIcon = new SVGPath();
        rejectIcon.setContent("M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z");
        rejectIcon.getStyleClass().add("btn-icon");
        rejectBtn.setGraphic(rejectIcon);
        rejectBtn.setContentDisplay(ContentDisplay.LEFT);
        rejectBtn.setGraphicTextGap(8);

        rejectBtn.setOnAction(e -> {
            AdminDAO.deleteMechanicRequest(mr.getRequestID());
            referesgMechanicReuests();
        });

        actionBox.getChildren().addAll(approveBtn, rejectBtn);

        // Add both containers to main card
        card.getChildren().addAll(infoBox, actionBox);

        // Set exact card dimensions
        card.setMinWidth(500);
        card.setPrefWidth(500);
        card.setMaxWidth(500);
        card.setMinHeight(200);
        card.setPrefHeight(200);
        card.setMaxHeight(200);

        // Add hover effect
        card.setOnMouseEntered(e -> {
            card.getStyleClass().add("card-hover");
        });

        card.setOnMouseExited(e -> {
            card.getStyleClass().remove("card-hover");
        });

        return card;
    }

    public void referesgMechanicReuests() {
        mechanicRequestRoot.getChildren().clear();
        List<mechanicRequests> list = AdminDAO.getAllmechanicRequests();

        if (list.isEmpty()) {
            Label noRequestsLabel = new Label("No pending mechanic requests");
            noRequestsLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #7f8c8d; -fx-padding: 40px;");
            mechanicRequestRoot.getChildren().add(noRequestsLabel);
        } else {
            for (mechanicRequests m : list) {
                mechanicRequestRoot.getChildren().add(createMechanicRequestCard(m));
            }
        }
    }

    // Helper method for interactive effects
    private void setupCardInteractivity(HBox card) {
        // Hover effect
        card.hoverProperty().addListener((obs, oldVal, isHovering) -> {
            if (isHovering) {
                card.getStyleClass().add("card-hover");
            } else {
                card.getStyleClass().remove("card-hover");
            }
        });

        // Click effect
        card.setOnMousePressed(e -> {
            card.getStyleClass().add("card-pressed");
        });

        card.setOnMouseReleased(e -> {
            card.getStyleClass().remove("card-pressed");
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mechanicRequestRoot.getChildren().clear();
        List<mechanicRequests> list= AdminDAO.getAllmechanicRequests();
        for(mechanicRequests m: list){
            mechanicRequestRoot.getChildren().add(createMechanicRequestCard(m));
        }

    }
}
