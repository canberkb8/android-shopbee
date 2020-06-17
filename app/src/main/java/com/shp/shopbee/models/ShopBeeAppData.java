package com.shp.shopbee.models;

import com.shp.shopbee.adapters.AdapterPostData;

import java.util.ArrayList;
import java.util.List;

public class ShopBeeAppData {

    //Singleton'un > app ayağa kalktığında objelerin app ayakta kaldığı sürece burada tutulur.
    private static final ShopBeeAppData ourInstance = new ShopBeeAppData();
    public static ShopBeeAppData getInstance(){
        return ourInstance;
    }

    public ShopBeeAppData() {

    }

    /*public  static  ShopBeeAppData getOurInstance(){
        return ourInstance;
    }*/





    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private ArrayList<PostDataModel> followArrayList;

    public ArrayList<PostDataModel> getFollowArrayList() {
        return followArrayList;
    }

    public void setFollowArrayList(ArrayList<PostDataModel> followArrayList) {
        this.followArrayList = followArrayList;
    }

    private ArrayList<CommentModel> commentModelArrayList;

    public ArrayList<CommentModel> getCommentModelArrayList() {
        return commentModelArrayList;
    }

    public void setCommentModelArrayList(ArrayList<CommentModel> commentModelArrayList) {
        this.commentModelArrayList = commentModelArrayList;
    }

    private ArrayList<MessageModel> messageModelArrayList;

    public ArrayList<MessageModel> getMessageModelArrayList() {
        return messageModelArrayList;
    }

    public void setMessageModelArrayList(ArrayList<MessageModel> messageModelArrayList) {
        this.messageModelArrayList = messageModelArrayList;
    }

    private ArrayList<PostMessageModel> postMessageModelArrayList;

    public ShopBeeAppData(ArrayList<PostMessageModel> postMessageModelArrayList) {
        this.postMessageModelArrayList = postMessageModelArrayList;
    }

    public ArrayList<PostMessageModel> getPostMessageModelArrayList() {
        return postMessageModelArrayList;
    }

    public void setPostMessageModelArrayList(ArrayList<PostMessageModel> postMessageModelArrayList) {
        this.postMessageModelArrayList = postMessageModelArrayList;
    }

    private String strName;

    public String getStrName() {
        return strName;
    }

    public String setStrName(String strName) {
        this.strName = strName;
        return strName;
    }
    private String strProfileUri;

    public String getStrProfileUri() {
        return strProfileUri;
    }

    public void setStrProfileUri(String strProfileUri) {
        this.strProfileUri = strProfileUri;
    }

    private String strKey;

    public String getStrKey() {
        return strKey;
    }

    public void setStrKey(String strKey) {
        this.strKey = strKey;
    }

    private String strProductKey;

    public String getProductKey() {
        return strProductKey;
    }

    public void setProductKey(String strProductKey) {
        this.strProductKey = strProductKey;
    }

    private int updatedPosition;

    public int getUpdatedPosition() {
        return updatedPosition;
    }

    public void setUpdatedPosition(int updatedPosition) {
        this.updatedPosition = updatedPosition;
    }
}
