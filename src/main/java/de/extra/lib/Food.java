package de.extra.lib;

import java.io.Serializable;

public class Food implements Serializable {

    private static final long serialVersionUID = 3957988302671731981L;
    private String name, amount, type, category;

    public Food(String name, String amount, String type, String category){
        this.name = name;
        this.amount = amount;
        this.type = type;
        this.category = category;
    }

    public String getAmount() {
        return amount;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
