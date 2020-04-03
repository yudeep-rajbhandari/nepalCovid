package com.covid.nepal.models;

public class CoronaFear {
    private String location;
    private String responsibleHospital;
    private String gender;
    private String age;
    private String description;
    private String statuses;
    private boolean status;

    public String getStatuses() {
        return statuses;
    }

    public void setStatuses(String statuses) {
        this.statuses = statuses;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getResponsibleHospital() {
        return responsibleHospital;
    }

    public void setResponsibleHospital(String responsibleHospital) {
        this.responsibleHospital = responsibleHospital;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
