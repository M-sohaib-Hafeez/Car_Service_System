package com.example.carservicecenter_ds.controllers;

import com.example.carservicecenter_ds.model.Mechanic;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MechanicMainController {

    @FXML
    private VBox contentArea;

    @FXML
    private HBox overviewItem;

    @FXML
    private HBox jobsItem;

    @FXML
    private HBox completedItem;


    private Mechanic currentMechanic;

//    public void setCurrentMechanic(Mechanic mechanic) {
//        this.currentMechanic = mechanic;
//        showOverview();
//    }

    private void loadPage(String fxml) {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/fxml/" + fxml));
            VBox page = loader.load();

            Object controller = loader.getController();

            if (controller instanceof MechanicOverviewController overview) {
                overview.setMechanicId(currentMechanic.getMechanicId());
            }
            if (controller instanceof MechanicJobsController jobs) {
                jobs.setMechanicId(currentMechanic.getMechanicId());
            }
            if (controller instanceof MechanicCompletedJobsController completed) {
                completed.setMechanicId(currentMechanic.getMechanicId());
            }

            contentArea.getChildren().setAll(page);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCurrentMechanic(Mechanic mechanic) {
        this.currentMechanic = mechanic;
        setActive(overviewItem);
        loadPage("mechanic-overview.fxml");
    }



    @FXML
    private void showOverview(MouseEvent e) {
        setActive(overviewItem);
        loadPage("mechanic-overview.fxml");
    }


    @FXML
    private void showJobs(MouseEvent e) {
        setActive(jobsItem);
        loadPage("mechanic-jobs.fxml");
    }


    @FXML
    private void showCompleted(MouseEvent e) {
        setActive(completedItem);
        loadPage("mechanic-completed.fxml");
    }


    @FXML
    private void logout() {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/fxml/login.fxml")
            );

            Stage stage = (Stage) contentArea
                    .getScene().getWindow();

            Scene scene = new Scene(root, 1100, 650);
            scene.getStylesheets().add(
                    getClass().getResource("/css/styles.css").toExternalForm()
            );

            stage.setScene(scene);
            stage.setMaximized(false);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.centerOnScreen();
            stage.setTitle("Login");
            stage.show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setActive(HBox selected) {
        overviewItem.getStyleClass().remove("active");
        jobsItem.getStyleClass().remove("active");
        completedItem.getStyleClass().remove("active");

        selected.getStyleClass().add("active");
    }


}

