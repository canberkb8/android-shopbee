package com.shp.shopbee.models;

public class RegisterModel {

    private String strName,strEmail,strPassword,strPhone,strProfileImgUrl;

    public RegisterModel(String strName, String strEmail, String strPassword, String strPhone,String strProfileImgUrl) {
        this.strName = strName;
        this.strEmail = strEmail;
        this.strPassword = strPassword;
        this.strPhone = strPhone;
        this.strProfileImgUrl = strProfileImgUrl;
    }

    public RegisterModel() {
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public String getStrEmail() {
        return strEmail;
    }

    public void setStrEmail(String strEmail) {
        this.strEmail = strEmail;
    }

    public String getStrPassword() {
        return strPassword;
    }

    public void setStrPassword(String strPassword) {
        this.strPassword = strPassword;
    }

    public String getStrPhone() {
        return strPhone;
    }

    public void setStrPhone(String strPhone) {
        this.strPhone = strPhone;
    }

    public String getStrProfileImgUrl() {
        return strProfileImgUrl;
    }

    public void setStrProfileImgUrl(String strProfileImgUrl) {
        this.strProfileImgUrl = strProfileImgUrl;
    }
}
