package com.example.intelligentlamp.classes;

/**
 * Created by 76377 on 2018/1/5.
 */

public class Model {
    private String name;

    private int imageId;

    public Model(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getImageId() {
        return imageId;
    }
}
