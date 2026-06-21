package com.example.carservicecenter_ds.controllers;

import com.example.carservicecenter_ds.DAO.ServiceAppointmentDAO;
import com.example.carservicecenter_ds.model.ServiceAppointment;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class MechanicJobsController {

    @FXML
    private VBox jobsContainer;

//    private int mechanicId = 1; // temporary, DB later
private int mechanicId;

    public void setMechanicId(int mechanicId) {
        this.mechanicId = mechanicId;
        loadJobs();
    }

    @FXML
    public void initialize() {
        jobsContainer.setAlignment(Pos.TOP_CENTER);
        loadJobs();
    }

    private void loadJobs() {
        jobsContainer.getChildren().clear();

        List<ServiceAppointment> jobs =
                ServiceAppointmentDAO.getByMechanic(mechanicId);

        for (ServiceAppointment job : jobs) {
            if (!job.getStatus().equals("COMPLETED")) {
                jobsContainer.getChildren().add(createJobCard(job));
            }
        }
    }

    private VBox createJobCard(ServiceAppointment job) {

        VBox card = new VBox(10);
        card.getStyleClass().add("job-card");
        card.setMaxWidth(800);

        Label service = new Label(job.getServiceName());
        service.getStyleClass().add("job-title");

        Label vehicle = new Label("Vehicle: " + job.getVehicleInfo());

        Label status = new Label(job.getStatus());
        status.setStyle("-fx-text-fill:" + job.getStatusColor());

        Button startBtn = new Button("Start");
        Button completeBtn = new Button("Complete");

        startBtn.setDisable(!job.getStatus().equals("PENDING"));
        completeBtn.setDisable(!job.getStatus().equals("IN_PROGRESS"));

        startBtn.setOnAction(e -> {
            ServiceAppointmentDAO.updateStatus(
                    job.getAppointmentId(), "IN_PROGRESS");
            loadJobs();
        });

        completeBtn.setOnAction(e -> {
            ServiceAppointmentDAO.updateStatus(
                    job.getAppointmentId(), "COMPLETED");
            loadJobs();
        });

        HBox actions = new HBox(10, startBtn, completeBtn);
        actions.setAlignment(Pos.CENTER_LEFT);

        card.getChildren().addAll(service, vehicle, status, actions);

        return card;
    }
}

