package com.shp.shopbee.models;

import java.io.Serializable;

public class PostDataModel implements Serializable {

    private String imgHomePage,imgUser,strUserName,strKey,strPrice,strExplanation,strStoreName,strProductKey,strLikeCount;
    private long strSharedTime;
    private boolean like;

    public PostDataModel(String imgHomePage,
                         String imgUser,
                         String strUserName,
                         String strKey,
                         String strPrice,
                         String strExplanation,
                         String strStoreName,
                         String strProductKey,
                         String strLikeCount,
                         long strSharedDate,
                         boolean like) {
        this.imgHomePage = imgHomePage;
        this.imgUser = imgUser;
        this.strUserName = strUserName;
        this.strKey = strKey;
        this.strPrice = strPrice;
        this.strExplanation = strExplanation;
        this.strStoreName = strStoreName;
        this.strProductKey = strProductKey;
        this.strLikeCount = strLikeCount;
        this.strSharedTime = strSharedTime;
        this.like = like;
    }

    public PostDataModel() {
    }

    public String getImgHomePage() {
        return imgHomePage;
    }

    public void setImgHomePage(String imgHomePage) {
        this.imgHomePage = imgHomePage;
    }

    public String getImgUser() {
        return imgUser;
    }

    public void setImgUser(String imgUser) {
        this.imgUser = imgUser;
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

    public String getStrStoreName() {
        return strStoreName;
    }

    public void setStrStoreName(String strStoreName) {
        this.strStoreName = strStoreName;
    }

    public String getStrProductKey() {
        return strProductKey;
    }

    public void setStrProductKey(String strProductKey) {
        this.strProductKey = strProductKey;
    }

    public long getStrSharedTime() {
        return strSharedTime;
    }

    public void setStrSharedTime(long strSharedTime) {
        this.strSharedTime = strSharedTime;
    }

    public String getStrLikeCount() {
        return strLikeCount;
    }

    public void setStrLikeCount(String strLikeCount) {
        this.strLikeCount = strLikeCount;
    }

    public boolean isLike(){return  like;}

    public void setLike(boolean like){this.like = like;}
}
