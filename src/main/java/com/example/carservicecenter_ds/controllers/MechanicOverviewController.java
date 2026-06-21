package com.example.carservicecenter_ds.controllers;

import com.example.carservicecenter_ds.DAO.ServiceAppointmentDAO;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

public class MechanicOverviewController {

    @FXML
    private Label totalLabel, activeLabel, completedLabel;

    private int mechanicId;
    @FXML
    private BarChart<String, Number> weeklyChart;


    public void setMechanicId(int mechanicId) {
        this.mechanicId = mechanicId;
        loadStats();
        loadWeeklyChart();
    }

    private void loadWeeklyChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        NumberAxis yAxis = (NumberAxis) weeklyChart.getYAxis();
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(10);
        yAxis.setTickUnit(1);
        series.setName("Jobs");

        series.getData().add(new XYChart.Data<>("Mon", 3));
        series.getData().add(new XYChart.Data<>("Tue", 5));
        series.getData().add(new XYChart.Data<>("Wed", 2));
        series.getData().add(new XYChart.Data<>("Thu", 4));
        series.getData().add(new XYChart.Data<>("Fri", 6));

        weeklyChart.getData().clear();
        weeklyChart.getData().add(series);
    }


    @FXML
    public void initialize() {
        totalLabel.setText("-");
        activeLabel.setText("-");
        completedLabel.setText("-");
    }

    private void loadStats() {
        int pending = ServiceAppointmentDAO.countJobs(mechanicId, "PENDING");
        int progress = ServiceAppointmentDAO.countJobs(mechanicId, "IN_PROGRESS");
        int completed = ServiceAppointmentDAO.countJobs(mechanicId, "COMPLETED");

        totalLabel.setText(String.valueOf(pending + progress + completed));
        activeLabel.setText(String.valueOf(pending + progress));
        completedLabel.setText(String.valueOf(completed));
    }
}


