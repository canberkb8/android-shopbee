package com.shp.shopbee.models;

public class SharedModel {

    private String strName,
            strProfileImgUrl,
            strSharedImg,
            strStoreName,
            strPrice,
            strExplanation,
            strUserID;
    private long strSharedTime;

    public SharedModel(String strName,
                       String strProfileImgUrl,
                       String strSharedImg,
                       String strStoreName,
                       String strPrice,
                       String strExplanation,
                       String strUserID,
                       long strSharedTime) {
        this.strName = strName;
        this.strProfileImgUrl = strProfileImgUrl;
        this.strSharedImg = strSharedImg;
        this.strStoreName = strStoreName;
        this.strPrice = strPrice;
        this.strExplanation = strExplanation;
        this.strUserID = strUserID;
        this.strSharedTime = strSharedTime;
    }
    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public String getStrProfileImgUrl() {
        return strProfileImgUrl;
    }

    public void setStrProfileImgUrl(String strProfileImgUrl) {
        this.strProfileImgUrl = strProfileImgUrl;
    }

    public String getStrSharedImg() {
        return strSharedImg;
    }

    public void setStrSharedImg(String strSharedImg) {
        this.strSharedImg = strSharedImg;
    }

    public String getStrStoreName() {
        return strStoreName;
    }

    public void setStrStoreName(String strStoreName) {
        this.strStoreName = strStoreName;
    }

    public String getStrPrice() {
        return strPrice;
    }

    public void setStrPrice(String strPrice) {
        this.strPrice = strPrice;
    }

    public String getStrExplanation() {
        return strExplanation;
    }

    public void setStrExplanation(String strExplanation) {
        this.strExplanation = strExplanation;
    }

    public String getStrUserID() {
        return strUserID;
    }

    public void setStrUserID(String strUserID) {
        this.strUserID = strUserID;
    }
    public long getStrSharedTime() {
        return strSharedTime;
    }

    public void setStrSharedTime(long strSharedTime) {
        this.strSharedTime = strSharedTime;
    }


}
