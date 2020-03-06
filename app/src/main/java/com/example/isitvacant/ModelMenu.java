package com.example.isitvacant;

public class ModelMenu {
    String name,cost,image,type;

    public ModelMenu() {
    }

    public ModelMenu(String name, String cost, String image,String type) {
        this.name = name;
        this.cost = cost;
        this.image = image;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
