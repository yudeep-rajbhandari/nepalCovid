package com.covid.nepal.models;

public class SuspectedCase {
    private  String symptoms;
    private  String period;
    private String age;
    private String gender;
    private String bodyTemperature;
    private String contactFromOutside;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    private boolean status;


    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBodyTemperature() {
        return bodyTemperature;
    }

    public void setBodyTemperature(String bodyTemperature) {
        this.bodyTemperature = bodyTemperature;
    }

    public String getHistoryOfDisease() {
        return historyOfDisease;
    }

    public void setHistoryOfDisease(String historyOfDisease) {
        this.historyOfDisease = historyOfDisease;
    }

    private String historyOfDisease;

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getContactFromOutside() {
        return contactFromOutside;
    }

    public void setContactFromOutside(String contactFromOutside) {
        this.contactFromOutside = contactFromOutside;
    }


}
