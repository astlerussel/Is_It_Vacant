package com.example.isitvacant;

public class ModelDisplayMenu {
    String name,type,cost,image;

    public ModelDisplayMenu() {
    }

    public ModelDisplayMenu(String name, String type, String cost, String image) {
        this.name = name;
        this.type = type;
        this.cost = cost;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
