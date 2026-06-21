module com.example.carservicecenter_ds {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens com.example.carservicecenter_ds to javafx.fxml;
    exports com.example.carservicecenter_ds;
    exports com.example.carservicecenter_ds.controllers;
    opens com.example.carservicecenter_ds.controllers to javafx.fxml;
}