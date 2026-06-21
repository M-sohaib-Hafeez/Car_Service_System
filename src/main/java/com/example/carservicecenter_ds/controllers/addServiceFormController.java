package com.example.carservicecenter_ds.controllers;

import com.example.carservicecenter_ds.DAO.AdminDAO;
import com.example.carservicecenter_ds.model.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class addServiceFormController {
    private adminServiceController adminServiceController;

    public void setAdminServiceController(adminServiceController controller) {
        this.adminServiceController = controller;
    }

    @FXML
    Label error;
    @FXML
    TextField serviceName;
    @FXML
    TextArea serviceDescription;
    @FXML
    TextField servicePrice;
    @FXML
    TextField serviceTime;
    @FXML
    TextField serviceCategory;

    @FXML
    public void initialize() {

        // PRICE – sirf numbers allow
        servicePrice.textProperty().addListener((obs, oldText, newText) -> {
            if (!newText.matches("\\d*")) {
                servicePrice.setText(newText.replaceAll("[^\\d]", ""));
            }
        });

        // ESTIMATED TIME – sirf numbers allow
        serviceTime.textProperty().addListener((obs, oldText, newText) -> {
            if (!newText.matches("\\d*")) {
                serviceTime.setText(newText.replaceAll("[^\\d]", ""));
            }
        });
    }

    @FXML

    public void addService(ActionEvent e){
        if (serviceName.getText().isEmpty() ||
                servicePrice.getText().isEmpty() ||
                serviceTime.getText().isEmpty() ||
                serviceCategory.getText().isEmpty()) {
            error.setTextFill(Color.RED);
            error.setText("Please fill all the fields");
            System.out.println("All fields are required");
            return;
        }
        String name=serviceName.getText();
        String description=serviceDescription.getText();
        int price=Integer.parseInt(servicePrice.getText());
        int time= Integer.parseInt(serviceTime.getText());
        String category= serviceCategory.getText();
        AdminDAO.addServices(new Service(name,description,price,time,category));

        adminServiceController.refreshServices();

        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.close();
    }
}
