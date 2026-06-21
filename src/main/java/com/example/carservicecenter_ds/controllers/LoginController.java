package com.example.carservicecenter_ds.controllers;

import com.example.carservicecenter_ds.DAO.CustomerDAO;
import com.example.carservicecenter_ds.DAO.MechanicDAO;
import com.example.carservicecenter_ds.DAO.UserDAO;
import com.example.carservicecenter_ds.model.Customer;
import com.example.carservicecenter_ds.model.Mechanic;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import com.example.carservicecenter_ds.model.User;
import com.example.carservicecenter_ds.util.NotificationService;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

//    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private ImageView carImage;

    private UserDAO userDAO;
    private CustomerDAO customerDAO;
    private CustomerController customerController;
    private Stage primaryStage;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userDAO = new UserDAO();
        customerDAO = new CustomerDAO();
        customerController = new CustomerController();

        loadCarImage();
        playCarAnimation();
    }

    private void loadCarImage() {
        try {
            carImage.setImage(new Image(getClass().getResourceAsStream("/images/carlogin.png")));
        } catch (Exception e) {
            System.out.println("Car image not found");
        }
    }

    private void playCarAnimation() {
        TranslateTransition carEnter = new TranslateTransition(Duration.seconds(1.5), carImage);
        carEnter.setFromX(300);
        carEnter.setToX(-120);
        carEnter.setInterpolator(Interpolator.EASE_OUT);
        carEnter.play();
    }


    @FXML
    private void handleLogin(ActionEvent e) {

        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter both username and password");
            errorLabel.setVisible(true);
            return;
        }

        // ADMIN LOGIN
        if ((email.equals("admin") || email.equals("admin@carservice.com")) && password.equals("admin123")) {
            try {
                FXMLLoader loader =
                        new FXMLLoader(getClass().getResource("/fxml/sideBar.fxml"));
                Parent root = loader.load();

                Scene scene = new Scene(root);
                scene.getStylesheets().add(
                        Objects.requireNonNull(
                                getClass().getResource("/css/styles.css")
                        ).toExternalForm()
                );

                Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("Admin Dashboard");
                stage.setMaximized(true);
//                stage.setFullScreen(true);
                stage.show();
                return;

            } catch (IOException ex) {
                ex.printStackTrace();
                showError("Failed to load admin dashboard");
                return;
            }
        }

        // CUSTOMER QUICK LOGIN tMemporary
        if (email.equals("customer") && password.equals("cus123")) {
            try {
                FXMLLoader loader =
                        new FXMLLoader(getClass().getResource("/fxml/customer-dashboard.fxml"));
                Parent root = loader.load();

                Scene scene = new Scene(root, 1300, 800);
                scene.getStylesheets().add(
                        Objects.requireNonNull(
                                getClass().getResource("/css/styles.css")
                        ).toExternalForm()
                );

                Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("Customer Dashboard");
                stage.setMaximized(true);
                stage.show();
                return;

            } catch (IOException ex) {
                ex.printStackTrace();
                showError("Failed to load customer dashboard");
                return;
            }
        }





        User user = userDAO.authenticate(email, password);

        if (user == null) {
            showError("Invalid email or password");
            return;
        }

        switch (user.getRole()) {

            case "CUSTOMER":
                openCustomerDashboard(user, e);
                break;

            case "MECHANIC":
                openMechanicDashboard(user, e);
                break;

            default:
                showError("Unknown user role");
        }
    }

    private void openCustomerDashboard(User user, ActionEvent e) {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/fxml/customer-dashboard.fxml"));
            Parent root = loader.load();

            CustomerController controller = loader.getController();

            if (user.getCustomerId() != null) {
                controller.setCustomerId(user.getCustomerId());
            } else {
                // Fallback: Try to find customer by email if customer_id is missing in users table
                Customer customer = customerDAO.getCustomerByEmail(user.getEmail());
                if (customer != null) {
                    controller.setCustomerId(customer.getCustomerId());
                } else {
                    System.err.println("Could not find customer profile for user: " + user.getEmail());
                }
            }

            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    Objects.requireNonNull(
                            getClass().getResource("/css/styles.css")
                    ).toExternalForm()
            );

            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Customer Dashboard");
            stage.setMaximized(true);
            stage.show();

        } catch (IOException ex) {
            ex.printStackTrace();
            showError("Failed to load customer dashboard");
        }
    }

   private void openMechanicDashboard(User user, ActionEvent e) {
    try {
        Mechanic mechanic =
                new MechanicDAO().getMechanicByUserId(user.getUserId());

        if (mechanic == null) {
            showError("Mechanic profile not found");
            return;
        }

        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/fxml/mechanicMain.fxml"));
        Parent root = loader.load();

        MechanicMainController controller = loader.getController();
        controller.setCurrentMechanic(mechanic);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(
                Objects.requireNonNull(
                        getClass().getResource("/css/styles.css")
                ).toExternalForm()
        );

        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Mechanic Dashboard");
        stage.setMaximized(true);
        stage.show();

    } catch (IOException ex) {
        ex.printStackTrace();
        showError("Failed to load mechanic dashboard");
    }
}




    private void showError(String message) {
        javafx.scene.control.Alert alert =
                new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);

        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void handleSuccessfulLogin(User user) {
        NotificationService.showSuccess("Login successful!\nRole: " + user.getRole());

        // TEMPORARY: navigation disabled

    }


    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage= primaryStage;
    }
    //  method to open signup screen
    public void openSignUp(MouseEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SignUp.fxml"));

            Parent root = loader.load();

            Stage stage=(Stage)((Node)e.getSource()).getScene().getWindow();
            Scene scene =new Scene(root,1100,650);
            scene.getStylesheets().add(
                    getClass().getResource("/css/styles.css").toExternalForm()
            );
            stage.setTitle("Sign Up");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

            // OPTIONAL: close login window
            // ((Stage) usernameField.getScene().getWindow()).close();

        } catch (IOException exp) {
            exp.printStackTrace();
        }
    }





}
