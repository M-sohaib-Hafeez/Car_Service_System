package com.example.carservicecenter_ds.model;

public class Service {
    private int serviceId;
    private String serviceName;
    private String description;
    private double basePrice;
    private int estimatedTime; // in minutes
    private String category;

    public Service() {}

    public Service(String serviceName, String description, double basePrice,
                   int estimatedTime, String category) {
        this.serviceName = serviceName;
        this.description = description;
        this.basePrice = basePrice;
        this.estimatedTime = estimatedTime;
        this.category = category;
    }

    // Getters and Setters
    public int getServiceId() { return serviceId; }
    public void setServiceId(int serviceId) { this.serviceId = serviceId; }

    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getBasePrice() { return basePrice; }
    public void setBasePrice(double basePrice) { this.basePrice = basePrice; }

    public int getEstimatedTime() { return estimatedTime; }
    public void setEstimatedTime(int estimatedTime) { this.estimatedTime = estimatedTime; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    @Override
    public String toString() {
        return serviceName + " - $" + basePrice + " (" + category + ")";
    }
}