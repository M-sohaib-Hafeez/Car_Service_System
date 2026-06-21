package com.example.carservicecenter_ds.model;

import java.time.LocalDateTime;

public class ServiceAppointment {
    private int mechanicId;
    private int appointmentId;
    private int customerId;
    private int vehicleId;
    private int serviceId;
    private LocalDateTime appointmentDate;
    private LocalDateTime scheduledTime;
    private String status;
    private int priorityLevel;
    private LocalDateTime estimatedCompletion;
    private LocalDateTime actualCompletion;
    private double totalCost;
    private double discountApplied;
    private String notes;
    private LocalDateTime createdAt;

    // Additional fields for display
    private String mechanicName;
    private String customerName;
    private String vehicleInfo;
    private String serviceName;

    public ServiceAppointment() {
        this.status = "PENDING";
        this.priorityLevel = 3;
        this.discountApplied = 0.0;
        this.appointmentDate = LocalDateTime.now();
    }

    // Getters and Setters
    public int getAppointmentId() { return appointmentId; }
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }

    public int getMechanicId() {
        return mechanicId;
    }

    public void setMechanicId(int mechanicId) {
        this.mechanicId = mechanicId;
    }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public int getVehicleId() { return vehicleId; }
    public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }

    public int getServiceId() { return serviceId; }
    public void setServiceId(int serviceId) { this.serviceId = serviceId; }

    public LocalDateTime getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDateTime appointmentDate) { this.appointmentDate = appointmentDate; }

    public LocalDateTime getScheduledTime() { return scheduledTime; }
    public void setScheduledTime(LocalDateTime scheduledTime) { this.scheduledTime = scheduledTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getPriorityLevel() { return priorityLevel; }
    public void setPriorityLevel(int priorityLevel) { this.priorityLevel = priorityLevel; }

    public LocalDateTime getEstimatedCompletion() { return estimatedCompletion; }
    public void setEstimatedCompletion(LocalDateTime estimatedCompletion) { this.estimatedCompletion = estimatedCompletion; }

    public LocalDateTime getActualCompletion() { return actualCompletion; }
    public void setActualCompletion(LocalDateTime actualCompletion) { this.actualCompletion = actualCompletion; }

    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }

    public double getDiscountApplied() { return discountApplied; }
    public void setDiscountApplied(double discountApplied) { this.discountApplied = discountApplied; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getMechanicName() { return mechanicName; }
    public void setMechanicName(String mechanicName) { this.mechanicName = mechanicName; }

    public String getVehicleInfo() { return vehicleInfo; }
    public void setVehicleInfo(String vehicleInfo) { this.vehicleInfo = vehicleInfo; }

    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }


    public String getStatusColor() {
        switch (status) {
            case "PENDING": return "#FFA500"; // Orange
            case "IN_PROGRESS": return "#1E90FF"; // Dodger Blue
            case "COMPLETED": return "#32CD32"; // Lime Green
            case "CANCELLED": return "#DC143C"; // Crimson
            default: return "#000000";
        }
    }

    public String getPriorityText() {
        switch (priorityLevel) {
            case 1: return "Emergency";
            case 2: return "High";
            case 3: return "Normal";
            case 4: return "Low";
            default: return "Normal";
        }
    }

    public String getPriorityColor() {
        switch (priorityLevel) {
            case 1: return "#DC143C"; // Crimson
            case 2: return "#FF4500"; // Orange Red
            case 3: return "#1E90FF"; // Dodger Blue
            case 4: return "#32CD32"; // Lime Green
            default: return "#1E90FF";
        }
    }
}