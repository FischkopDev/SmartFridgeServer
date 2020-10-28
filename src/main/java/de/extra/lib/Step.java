package de.extra.lib;

import java.awt.*;
import java.io.Serializable;

public class Step implements Serializable {

    private static final long serialVersionUID = 3935968342621737981L;

    private String description, name;
    private Image img;

    public Step(String desccription, String name, Image img){
        this.description = desccription;
        this.name = name;
        this.img = img;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Image getImg() {
        return img;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImg(Image img) {
        this.img = img;
    }

}
