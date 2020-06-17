package com.shp.shopbee.models;

public class CommentModel {
    private String strName,strImg,strComment;

    public CommentModel(String strName, String strImg, String strComment) {
        this.strName = strName;
        this.strImg = strImg;
        this.strComment = strComment;
    }

    public CommentModel() {
    }


    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public String getStrImg() {
        return strImg;
    }

    public void setStrImg(String strImg) {
        this.strImg = strImg;
    }

    public String getStrComment() {
        return strComment;
    }

    public void setStrComment(String strComment) {
        this.strComment = strComment;
    }
}
