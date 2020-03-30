package com.covid.nepal.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.json.JSONPropertyName;

public class NepalInformation {

    private Integer Recovered;
    private Integer Negative;
    private Integer Positive;

    @JsonProperty("Recovered")
    public Integer getRecovered() {
        return Recovered;
    }

    public void setRecovered(Integer recovered) {
        Recovered = recovered;
    }
    @JsonProperty("Negative")
    public Integer getNegative() {
        return Negative;
    }

    public void setNegative(Integer negative) {
        Negative = negative;
    }
    @JsonProperty("Positive")
    public Integer getPositive() {
        return Positive;
    }

    public void setPositive(Integer positive) {
        Positive = positive;
    }

    @JsonProperty("Isolation")
    public Integer getIsolation() {
        return Isolation;
    }

    public void setIsolation(Integer isolation) {
        Isolation = isolation;
    }
    @JsonProperty("Total Samples Tested")
    public Integer getTotal() {
        return Total;
    }

    public void setTotal(Integer total) {
        Total = total;
    }

    private Integer Isolation;
    private Integer Total;
}
