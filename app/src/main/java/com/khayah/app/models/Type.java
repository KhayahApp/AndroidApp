package com.khayah.app.models;

public class Type {
    private int image;
    private String title;
    private String alias;

    public Type(int image, String title, String alias) {
        this.image = image;
        this.title = title;
        this.alias = alias;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
