package com.shp.shopbee.models;

public class PostMessageModel {

    String message,strKey;

    public PostMessageModel(String message, String strKey) {
        this.message = message;
        this.strKey = strKey;
    }

    public PostMessageModel() {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStrKey() {
        return strKey;
    }

    public void setStrKey(String strKey) {
        this.strKey = strKey;
    }
}
