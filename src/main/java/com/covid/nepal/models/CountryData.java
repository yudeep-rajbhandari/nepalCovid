package com.covid.nepal.models;

public class CountryData {
    private String country;
    private Integer Active_Cases;
    private Integer Total_Deaths;
    private Double TotIn1M;
    private Double DIn1M;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getActive_Cases() {
        return Active_Cases;
    }

    public void setActive_Cases(Integer active_Cases) {
        Active_Cases = active_Cases;
    }

    public Integer getTotal_Deaths() {
        return Total_Deaths;
    }

    public void setTotal_Deaths(Integer total_Deaths) {
        Total_Deaths = total_Deaths;
    }

    public Double getTotIn1M() {
        return TotIn1M;
    }

    public void setTotIn1M(Double totIn1M) {
        TotIn1M = totIn1M;
    }

    public Double getDIn1M() {
        return DIn1M;
    }

    public void setDIn1M(Double DIn1M) {
        this.DIn1M = DIn1M;
    }
}
