package com.example.carservicecenter_ds.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

import javafx.scene.input.MouseEvent;

import java.io.IOException;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class adminSideBarController implements Initializable {

    @FXML
    AnchorPane contentAnchorPane;
    @FXML private Label dashboardItem;
    @FXML private Label servicesItem;
    @FXML private Label appointmentItem;
    @FXML private Label mechanicsItem;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setActive(dashboardItem);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/adminDashBoard.fxml"));
        try {
            Parent view = loader.load();
            Scene scene=new Scene(view);
            contentAnchorPane.getChildren().clear();
            contentAnchorPane.getChildren().add(view);
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    public void toDashboard(MouseEvent event) {
        setActive(dashboardItem);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/adminDashBoard.fxml"));
            try {
                Parent view = loader.load();
                contentAnchorPane.getChildren().setAll(view);
                AnchorPane.setTopAnchor(view, 0.0);
                AnchorPane.setBottomAnchor(view, 0.0);
                AnchorPane.setLeftAnchor(view, 0.0);
                AnchorPane.setRightAnchor(view, 0.0);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


    }


    @FXML
    public void toServices(MouseEvent event) {
        setActive(servicesItem);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/services.fxml"));
        try {
            Parent view = loader.load();
            contentAnchorPane.getChildren().clear();
            contentAnchorPane.getChildren().add(view);
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void toMechanics(){
        setActive(mechanicsItem);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mechanic.fxml"));
        try {
            Parent view = loader.load();
            contentAnchorPane.getChildren().clear();
            contentAnchorPane.getChildren().add(view);
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void toAppointment(){
        setActive(appointmentItem);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdminAppointment.fxml"));
        try {
            Parent view = loader.load();
            contentAnchorPane.getChildren().clear();
            contentAnchorPane.getChildren().add(view);
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setActive(Label activeItem) {
        dashboardItem.getStyleClass().remove("active");
        servicesItem.getStyleClass().remove("active");
        appointmentItem.getStyleClass().remove("active");
        mechanicsItem.getStyleClass().remove("active");

        activeItem.getStyleClass().add("active");
    }

    @FXML
    public void logOut(ActionEvent event){
        try {
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root= loader.load();
            Scene scene=new Scene(root,1100,650);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            Stage stage= (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setMaximized(false);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
