package com.example.carservicecenter_ds.model;

public class mechanicRequests {
    private int requestID;
    private int userId;
    private String name;
    private String email;
    private String specialization;
    private int exp;
    private String phone;
    private String status;
    private double hourlyRate;

    // Constructors
    public mechanicRequests() {}

    public mechanicRequests(int requestID, int userId, String name, String email,
                            String specialization, int exp, String phone,
                            String status, double hourlyRate) {
        this.requestID = requestID;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.specialization = specialization;
        this.exp = exp;
        this.phone = phone;
        this.status = status;
        this.hourlyRate = hourlyRate;
    }

    // Getters and Setters
    public int getRequestID() {
        return requestID;
    }

    public void setRequestID(int requestID) {
        this.requestID = requestID;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    @Override
    public String toString() {
        return "mechanicRequests{" +
                "requestID=" + requestID +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", specialization='" + specialization + '\'' +
                ", exp=" + exp +
                ", phone='" + phone + '\'' +
                ", status='" + status + '\'' +
                ", hourlyRate=" + hourlyRate +
                '}';
    }
}