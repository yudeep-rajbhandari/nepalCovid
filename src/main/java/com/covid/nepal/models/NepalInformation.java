package com.covid.nepal.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.json.JSONPropertyName;

public class NepalInformation {

    private Integer Recovered;
    private Integer Negative;

    public Integer getRecovered() {
        return Recovered;
    }

    public void setRecovered(Integer recovered) {
        Recovered = recovered;
    }

    public Integer getNegative() {
        return Negative;
    }

    public void setNegative(Integer negative) {
        Negative = negative;
    }

    public Integer getPositive() {
        return Positive;
    }

    public void setPositive(Integer positive) {
        Positive = positive;
    }

    public Integer getIsolation() {
        return Isolation;
    }

    public void setIsolation(Integer isolation) {
        Isolation = isolation;
    }

    public Integer getTotal_Samples_Tested() {
        return Total_Samples_Tested;
    }

    public void setTotal_Samples_Tested(Integer total_Samples_Tested) {
        Total_Samples_Tested = total_Samples_Tested;
    }

    private Integer Positive;
    private Integer Isolation;
    private Integer Total_Samples_Tested;
}
