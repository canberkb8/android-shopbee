package com.shp.shopbee.models;

public class MessageModel {
    private String strUserImg , strUserName , strKey , strLastMessage;

    public MessageModel(String strUserImg, String strUserName, String strKey, String strLastMessage) {
        this.strUserImg = strUserImg;
        this.strUserName = strUserName;
        this.strKey = strKey;
        this.strLastMessage = strLastMessage;
    }

    public MessageModel() {
    }

    public String getStrUserImg() {
        return strUserImg;
    }

    public void setStrUserImg(String strUserImg) {
        this.strUserImg = strUserImg;
    }

    public String getStrUserName() {
        return strUserName;
    }

    public void setStrUserName(String strUserName) {
        this.strUserName = strUserName;
    }

    public String getStrKey() {
        return strKey;
    }

    public void setStrKey(String strKey) {
        this.strKey = strKey;
    }

    public String getStrLastMessage() {
        return strLastMessage;
    }

    public void setStrLastMessage(String strLastMessage) {
        this.strLastMessage = strLastMessage;
    }
}
