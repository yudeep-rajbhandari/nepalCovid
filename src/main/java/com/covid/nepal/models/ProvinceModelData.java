package com.covid.nepal.models;

public class ProvinceModelData {
    private String province_name;
    private Integer total_tested;
    private Integer total_negative;
    private Integer total_in_isolation;
    private Integer total_death;
    private Integer total_recovered;
    private Integer total_positive;
    private Integer num_of_isolation_bed;
    private Integer occupied_isolation_bed;
    private Integer facility_count;
    private String update_date;

    public Integer getProvince_id() {
        return province_id;
    }

    public void setProvince_id(Integer province_id) {
        this.province_id = province_id;
    }

    private Integer province_id;

    public String getProvince_name() {
        return province_name;
    }

    public void setProvince_name(String province_name) {
        this.province_name = province_name;
    }

    public Integer getTotal_tested() {
        return total_tested;
    }

    public void setTotal_tested(Integer total_tested) {
        this.total_tested = total_tested;
    }

    public Integer getTotal_negative() {
        return total_negative;
    }

    public void setTotal_negative(Integer total_negative) {
        this.total_negative = total_negative;
    }

    public Integer getTotal_in_isolation() {
        return total_in_isolation;
    }

    public void setTotal_in_isolation(Integer total_in_isolation) {
        this.total_in_isolation = total_in_isolation;
    }

    public Integer getTotal_death() {
        return total_death;
    }

    public void setTotal_death(Integer total_death) {
        this.total_death = total_death;
    }

    public Integer getTotal_recovered() {
        return total_recovered;
    }

    public void setTotal_recovered(Integer total_recovered) {
        this.total_recovered = total_recovered;
    }

    public Integer getTotal_positive() {
        return total_positive;
    }

    public void setTotal_positive(Integer total_positive) {
        this.total_positive = total_positive;
    }

    public Integer getNum_of_isolation_bed() {
        return num_of_isolation_bed;
    }

    public void setNum_of_isolation_bed(Integer num_of_isolation_bed) {
        this.num_of_isolation_bed = num_of_isolation_bed;
    }

    public Integer getOccupied_isolation_bed() {
        return occupied_isolation_bed;
    }

    public void setOccupied_isolation_bed(Integer occupied_isolation_bed) {
        this.occupied_isolation_bed = occupied_isolation_bed;
    }

    public Integer getFacility_count() {
        return facility_count;
    }

    public void setFacility_count(Integer facility_count) {
        this.facility_count = facility_count;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }
}
