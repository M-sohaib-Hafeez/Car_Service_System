package com.example.carservicecenter_ds.controllers;

import com.example.carservicecenter_ds.DAO.AdminDAO;
import com.example.carservicecenter_ds.model.Service;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class adminController {
//    public VBox createServiceCard1(
//            int serviceId,
//            String name,
//            String description,
//            double basePrice,
//            String estimatedTime,
//            String category
//    ) {
//
//        Label nameLbl = new Label(name);
//        nameLbl.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
//
//        Label descLbl = new Label(description);
//        descLbl.setWrapText(true);
//
//        Label priceLbl = new Label("Price: Rs. " + basePrice);
//        Label timeLbl = new Label("Estimated Time: " + estimatedTime);
//        Label categoryLbl = new Label("Category: " + category);
//
//        Button deleteBtn = new Button("Delete");
//        deleteBtn.setStyle(
//                "-fx-background-color: #e74c3c; " +
//                        "-fx-text-fill: white; " +
//                        "-fx-cursor: hand;"
//        );
//
//        // 🔥 Delete Button Action
//        deleteBtn.setOnAction(e -> {
//            AdminDAO.deleteService(serviceId);
//        });
//
//        VBox card = new VBox(8);
//        card.getChildren().addAll(
//                nameLbl,
//                descLbl,
//                priceLbl,
//                timeLbl,
//                categoryLbl,
//                deleteBtn
//        );
//
//        card.setPadding(new Insets(12));
//        card.setStyle(
//                "-fx-background-color: white;" +
//                        "-fx-border-color: #ddd;" +
//                        "-fx-border-radius: 8;" +
//                        "-fx-background-radius: 8;"
//        );
//
//        return card;
//    }


}
