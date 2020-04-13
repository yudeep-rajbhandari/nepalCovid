package com.covid.nepal.models;

import java.util.ArrayList;

public class EssentialsModel {
    private String Name;
    private String MinOrder;
    private ArrayList<String> Location;
    private String Link;
    private String Category;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMinOrder() {
        return MinOrder;
    }

    public void setMinOrder(String minOrder) {
        MinOrder = minOrder;
    }

    public ArrayList<String> getLocation() {
        return Location;
    }

    public void setLocation(ArrayList<String> location) {
        Location = location;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }
    
}
