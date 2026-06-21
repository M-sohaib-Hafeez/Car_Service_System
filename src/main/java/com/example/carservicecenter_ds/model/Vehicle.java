package com.example.carservicecenter_ds.model;

import java.time.LocalDate;

public class Vehicle {
    private int vehicleId;
    private int customerId;
    private String licensePlate;
    private String vehicleType;
    private String make;
    private String model;
    private int year;
    private LocalDate lastServiceDate;

    public Vehicle() {}

    public Vehicle(int customerId, String licensePlate, String vehicleType,
                   String make, String model, int year) {
        this.customerId = customerId;
        this.licensePlate = licensePlate;
        this.vehicleType = vehicleType;
        this.make = make;
        this.model = model;
        this.year = year;
    }

    // Getters and Setters
    public int getVehicleId() { return vehicleId; }
    public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }

    public String getMake() { return make; }
    public void setMake(String make) { this.make = make; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public LocalDate getLastServiceDate() { return lastServiceDate; }
    public void setLastServiceDate(LocalDate lastServiceDate) { this.lastServiceDate = lastServiceDate; }

    @Override
    public String toString() {
        return year + " " + make + " " + model + " (" + licensePlate + ")";
    }
}