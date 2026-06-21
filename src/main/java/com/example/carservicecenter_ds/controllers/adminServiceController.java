package com.example.carservicecenter_ds.controllers;

import com.example.carservicecenter_ds.DAO.AdminDAO;
import com.example.carservicecenter_ds.DAO.DataBaseConnection;
import com.example.carservicecenter_ds.model.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ResourceBundle;

public class adminServiceController implements Initializable {
    @FXML
    private  FlowPane servicesRoot;
    public  VBox createServiceCard(Service service){
        // Create main container
        VBox card = new VBox(10);
        card.getStyleClass().add("service-card");
        card.setPadding(new Insets(15));

        // Header with icon and title
        HBox headerBox = new HBox(10);
        headerBox.setAlignment(Pos.CENTER_LEFT);

        // Service icon (you can use SVG path or ImageView)
        SVGPath serviceIcon = new SVGPath();
        serviceIcon.getStyleClass().add("service-icon");
        String iconPath = getServiceIcon(service.getCategory());
        serviceIcon.setContent(iconPath);

        Label nameLabel = new Label(service.getServiceName());
        nameLabel.getStyleClass().add("service-name");

        headerBox.getChildren().addAll(serviceIcon, nameLabel);

        // Description with icon
        HBox descBox = new HBox(8);
        descBox.setAlignment(Pos.CENTER_LEFT);

        SVGPath descIcon = new SVGPath();
        descIcon.getStyleClass().add("detail-icon");
        descIcon.setContent("M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-6h2v6zm0-8h-2V7h2v2z");

        Label descLbl = new Label(service.getDescription());
        descLbl.getStyleClass().add("service-description");
        descLbl.setWrapText(true);
        descLbl.setMaxWidth(250);

        descBox.getChildren().addAll(descIcon, descLbl);

        // Price with icon
        HBox priceBox = new HBox(8);
        priceBox.setAlignment(Pos.CENTER_LEFT);

        SVGPath priceIcon = new SVGPath();
        priceIcon.getStyleClass().add("detail-icon");
        priceIcon.setContent("M11 8h2v5h-2zm0 7h2v2h-2zm1-13C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8z");

        Label priceLbl = new Label("Rs. " + service.getBasePrice());
        priceLbl.getStyleClass().add("service-price");

        priceBox.getChildren().addAll(priceIcon, priceLbl);

        // Time with icon
        HBox timeBox = new HBox(8);
        timeBox.setAlignment(Pos.CENTER_LEFT);

        SVGPath timeIcon = new SVGPath();
        timeIcon.getStyleClass().add("detail-icon");
        timeIcon.setContent("M11.99 2C6.47 2 2 6.48 2 12s4.47 10 9.99 10C17.52 22 22 17.52 22 12S17.52 2 11.99 2zM12 20c-4.42 0-8-3.58-8-8s3.58-8 8-8 8 3.58 8 8-3.58 8-8 8zm.5-13H11v6l5.25 3.15.75-1.23-4.5-2.67z");

        Label timeLbl = new Label(""+service.getEstimatedTime());
        timeLbl.getStyleClass().add("service-time");

        timeBox.getChildren().addAll(timeIcon, timeLbl);

        // Category with icon
        HBox categoryBox = new HBox(8);
        categoryBox.setAlignment(Pos.CENTER_LEFT);

        SVGPath categoryIcon = new SVGPath();
        categoryIcon.getStyleClass().add("detail-icon");
        categoryIcon.setContent("M10 4H4c-1.1 0-1.99.9-1.99 2L2 18c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V8c0-1.1-.9-2-2-2h-8l-2-2z");

        Label categoryLbl = new Label(service.getCategory());
        categoryLbl.getStyleClass().add("service-category");

        categoryBox.getChildren().addAll(categoryIcon, categoryLbl);

        // Action buttons container - Only Delete button now
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        // Delete button with icon
        Button deleteBtn = new Button("Delete");
        deleteBtn.getStyleClass().add("delete-btn");
        deleteBtn.setGraphic(createDeleteIcon());

        deleteBtn.setOnAction(e -> {
            if(canDeleteService(service.getServiceId())) {
                AdminDAO.deleteService(service.getServiceId());
                refreshServices();
            } else {
                // show alert
                Alert alert = new Alert(Alert.AlertType.ERROR, "Cannot delete service. Appointments exist for this service.", ButtonType.OK);
                alert.showAndWait();
            }


//            AdminDAO.deleteService(service.getServiceId());
//            refreshServices();
        });

        buttonBox.getChildren().add(deleteBtn);

        // Add all components to card
        card.getChildren().addAll(
                headerBox,
                descBox,
                priceBox,
                timeBox,
                categoryBox,
                buttonBox
        );

        // Set equal size constraints
        card.setMinWidth(250);
        card.setMinHeight(280);
        card.setMaxWidth(250);
        card.setMaxHeight(280);


        return card;
    }

    public static boolean canDeleteService(int serviceId) {
        String query = "SELECT COUNT(*) FROM appointments WHERE service_id = ?";
        try (Connection conn = DataBaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, serviceId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0; // 0 means safe to delete
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    // Helper method to create delete icon
    private SVGPath createDeleteIcon() {
        SVGPath icon = new SVGPath();
        icon.setContent("M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z");
        icon.getStyleClass().add("button-icon");
        return icon;
    }

    // Method to get appropriate SVG icon based on service category
    private String getServiceIcon(String category) {
        switch(category.toLowerCase()) {
            case "engine":
                return "M9 4v3h5v12h3V7h5V4H9zm-6 8h3v7h3v-7h3V9H3v3z";
            case "brakes":
                return "M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-2h2v2zm0-4h-2V7h2v6z";
            case "tires":
                return "M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8zm-1-13h2v6h-2zm0 8h2v2h-2z";
            case "electrical":
                return "M11 15h2v2h-2zm0-8h2v6h-2zm.99-5C6.47 2 2 6.48 2 12s4.47 10 9.99 10C17.52 22 22 17.52 22 12S17.52 2 11.99 2zM12 20c-4.42 0-8-3.58-8-8s3.58-8 8-8 8 3.58 8 8-3.58 8-8 8z";
            case "oil":
                return "M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8z";
            case "transmission":
                return "M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8zm-1-13h2v6h-2zm0 8h2v2h-2z";
            default:
                return "M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8z";
        }
    }

    public  void refreshServices() {
        servicesRoot.getChildren().clear();
        List<Service> serviceList = AdminDAO.getAllServices();
        for (Service s : serviceList) {
            servicesRoot.getChildren().add(createServiceCard(s));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        servicesRoot.getChildren().clear();
        List<Service> serviceList= AdminDAO.getAllServices();
        for(Service s: serviceList){
            servicesRoot.getChildren().add(createServiceCard(s));
        }
    }

//    @FXML
//
//    public void addService(ActionEvent e){
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addServiceform.fxml"));
//        try {
//
//
//
//            Parent root= loader.load();
//            Scene scene=new Scene(root);
//            Stage stage= new Stage();
//            stage.setScene(scene);
//            stage.show();
//        } catch (IOException exp) {
//            System.out.println("failed to load add service form");
//            throw new RuntimeException(exp);
//        }
//    }
@FXML
public void addService() throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addServiceform.fxml"));
    Parent root = loader.load();  // FXML load + controller create

    addServiceFormController formController = loader.getController();
    formController.setAdminServiceController(this);

    Stage stage = new Stage();
    stage.setScene(new Scene(root));
    stage.show();
}
}
