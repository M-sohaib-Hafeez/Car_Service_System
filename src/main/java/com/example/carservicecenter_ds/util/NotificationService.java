package com.example.carservicecenter_ds.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class NotificationService {

    public static void showSuccess(String message) {
        showAlert(AlertType.INFORMATION, "Success", message);
    }

    public static void showError(String message) {
        showAlert(AlertType.ERROR, "Error", message);
    }

    public static void showWarning(String message) {
        showAlert(AlertType.WARNING, "Warning", message);
    }

    public static void showInfo(String message) {
        showAlert(AlertType.INFORMATION, "Information", message);
    }

    private static void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
