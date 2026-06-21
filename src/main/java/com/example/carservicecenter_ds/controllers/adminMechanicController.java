package com.example.carservicecenter_ds.controllers;

import com.example.carservicecenter_ds.DAO.AdminDAO;
import com.example.carservicecenter_ds.model.Mechanic;
import com.example.carservicecenter_ds.model.Service;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class adminMechanicController implements Initializable {

    @FXML
    private FlowPane mechanicsRoot;
    public VBox createMechanicCard(Mechanic mechanic) {
        // Create labels with CSS classes
        Label nameLabel = new Label(mechanic.getName());
        nameLabel.getStyleClass().add("mechanic-name");

        Label specialLbl = new Label("Specialization: " + mechanic.getSpecialization());
        specialLbl.getStyleClass().add("mechanic-detail");

        Label statusLbl = new Label("Status: " + mechanic.getStatus());
        statusLbl.getStyleClass().add("mechanic-detail");
        statusLbl.getStyleClass().add("mechanic-status");

        Label rateLbl = new Label("Hourly Rate: $" + mechanic.getHourlyRate());
        rateLbl.getStyleClass().add("mechanic-detail");
        rateLbl.getStyleClass().add("mechanic-rate");

        Label phoneLbl = new Label("Phone: " + mechanic.getPhone());
        phoneLbl.getStyleClass().add("mechanic-detail");

        Label emailLbl = new Label("Email: " + mechanic.getEmail());
        emailLbl.getStyleClass().add("mechanic-detail");

        // Create delete button with CSS class
        Button deleteBtn = new Button("Delete");
        deleteBtn.getStyleClass().add("delete-btn");
        deleteBtn.setOnAction(e -> {
            if(AdminDAO.CheckMechanicAppoinment(mechanic.getMechanicId())){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Cannot Delete Mechanic");
                alert.setHeaderText(null); // header ko chhod rahe hain
                alert.setContentText("This mechanic cannot be deleted because they are assigned to active services.");

                alert.showAndWait();
            }else{
                    AdminDAO.deleteMechanic(mechanic.getMechanicId());
                    refreshMechanics();
                }
        });

        // Create card container with CSS class
        VBox card = new VBox(8);
        card.getStyleClass().add("mechanic-card");
        card.setPadding(new Insets(10));

        card.setPrefWidth(250);

        // Make VBox fill available space
        VBox.setVgrow(nameLabel, Priority.NEVER);
        VBox.setVgrow(specialLbl, Priority.NEVER);
        VBox.setVgrow(statusLbl, Priority.NEVER);
        VBox.setVgrow(rateLbl, Priority.NEVER);
        VBox.setVgrow(phoneLbl, Priority.NEVER);
        VBox.setVgrow(emailLbl, Priority.ALWAYS); // Email label will expand
        VBox.setVgrow(deleteBtn, Priority.NEVER);

        // Set text wrapping
        nameLabel.setWrapText(true);
        specialLbl.setWrapText(true);
        phoneLbl.setWrapText(true);
        emailLbl.setWrapText(true);

        card.getChildren().addAll(nameLabel, specialLbl, statusLbl, rateLbl, phoneLbl, emailLbl, deleteBtn);

        return card;
    }


    public void refreshMechanics() {
        mechanicsRoot.getChildren().clear();
        List<Mechanic> mechanicList = AdminDAO.getAllMechanic();
        for (Mechanic m : mechanicList) {
            mechanicsRoot.getChildren().add(createMechanicCard(m));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mechanicsRoot.getChildren().clear();
        List<Mechanic> mechanicList= AdminDAO.getAllMechanic();
        for(Mechanic m: mechanicList){
            mechanicsRoot.getChildren().add(createMechanicCard(m));
        }
    }
    @FXML
    public void mechanicRequests() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mechanicRequests.fxml"));
        Parent root = loader.load();  // FXML load + controller create

        mechanicRequestsController controller= loader.getController();
        controller.setMechanicController(this);

//        Scene scene=new Scene(root);
//        scene.getStylesheets().add(getClass().getResource("/resource/com.example.carscervicecenter_ds/style.css").toExternalForm());

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
