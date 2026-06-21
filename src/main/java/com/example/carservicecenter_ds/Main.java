package com.example.carservicecenter_ds;

import com.example.carservicecenter_ds.DAO.DataBaseInitializer;
import com.example.carservicecenter_ds.controllers.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.application.Application.launch;


public class Main extends Application {


    @Override
    public void start(Stage primaryStage) {
        try {
            DataBaseInitializer.initialize();

            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/fxml/login.fxml"));

                Parent root = loader.load();

                // Get controller instance
                LoginController controller = loader.getController();

                // Pass stage to controller
                controller.setPrimaryStage(primaryStage);

                Scene scene = new Scene(root, 1100, 650);
                scene.getStylesheets().add(
                        getClass().getResource("/css/styles.css").toExternalForm()
                );

                primaryStage.setTitle("Car Service Center - Login");
                primaryStage.setScene(scene);
                primaryStage.show();

            } catch (Exception e) {
                e.printStackTrace();
            }

    }


    public static void main(String[] args) {
        System.out.println("Starting Car Service Center Management System...");
        launch(args);
    }
}
