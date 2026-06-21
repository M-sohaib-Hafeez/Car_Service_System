package com.example.carservicecenter_ds.controllers;

import com.example.carservicecenter_ds.DAO.ServiceAppointmentDAO;
import com.example.carservicecenter_ds.model.ServiceAppointment;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class MechanicCompletedJobsController {
    @FXML
    private VBox completedJobsContainer;

//    private final int mechanicId = 1; // temporary

    @FXML
    public void initialize() {
//        loadCompletedJobs();
//        addCompletedCard("Service Name", "Vehicle Info", "Completed");
    }
    private int mechanicId;

    public void setMechanicId(int mechanicId) {
        this.mechanicId = mechanicId;
        loadCompletedJobs();
    }


    private void loadCompletedJobs() {
        completedJobsContainer.getChildren().clear();

        List<ServiceAppointment> jobs =
                ServiceAppointmentDAO.getCompletedByMechanic(mechanicId);

        for (ServiceAppointment job : jobs) {
            completedJobsContainer.getChildren().add(createCompletedCard(job));
        }
    }

    private VBox createCompletedCard(ServiceAppointment job) {

        Label service = new Label(job.getServiceName());
        service.setStyle("-fx-font-weight:bold;");

        Label vehicle = new Label("Vehicle: " + job.getVehicleInfo());

        Label date = new Label(
                "Completed: " +
                        (job.getActualCompletion() != null
                                ? job.getActualCompletion().format(
                                DateTimeFormatter.ofPattern("MMM dd, yyyy"))
                                : "N/A")
        );


        Label cost = new Label("Cost: ₹" + job.getTotalCost());

        VBox card = new VBox(6, service, vehicle, date, cost);
        card.setStyle("""
            -fx-padding:12;
            -fx-border-color:#ddd;
            -fx-border-radius:8;
        """);

        return card;
    }

//    private void addCompletedCard(String service, String vehicle, String status) {
//
//        VBox card = new VBox(8);
//        card.getStyleClass().addAll("job-card", "completed");
//
//        Label s = new Label(service);
//        Label v = new Label(vehicle);
//        Label st = new Label(status);
//
//        card.getChildren().addAll(s, v, st);
//        completedJobsContainer.getChildren().add(card);
//    }
}
