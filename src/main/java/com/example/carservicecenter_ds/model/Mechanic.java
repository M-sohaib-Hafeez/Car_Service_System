package com.example.carservicecenter_ds.model;

public class Mechanic {
    private int mechanicId;
    private String name;
    private String specialization;
    private String status;
    private double hourlyRate;
    private String phone;
    private String email;
    private int experience;

    public Mechanic() {
        this.status = "AVAILABLE";
    }

    public Mechanic(String name, String specialization, double hourlyRate,
                    String phone, String email) {
        this.name = name;
        this.specialization = specialization;
        this.hourlyRate = hourlyRate;
        this.phone = phone;
        this.email = email;
        this.status = "AVAILABLE";
    }

    // Getters and Setters
    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }
    public int getMechanicId() { return mechanicId; }
    public void setMechanicId(int mechanicId) { this.mechanicId = mechanicId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(double hourlyRate) { this.hourlyRate = hourlyRate; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getStatusColor() {
        switch (status) {
            case "AVAILABLE": return "#32CD32"; // Lime Green
            case "BUSY": return "#DC143C"; // Crimson
            case "OFF_DUTY": return "#808080"; // Gray
            default: return "#000000";
        }
    }

    @Override
    public String toString() {
        return name + " - " + specialization;
    }
}