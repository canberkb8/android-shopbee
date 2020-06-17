package com.shp.shopbee.services;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.shp.shopbee.MainActivity;
import com.shp.shopbee.models.PostDataModel;
import com.shp.shopbee.models.ShopBeeAppData;

import java.util.ArrayList;
import java.util.Objects;

public class PostService {

    private MainActivity activity;

    ArrayList<PostDataModel> postDataModelArrayList;
    private int likeCount;


    private final PostSuccessListener listenerSuccess;

    public PostService(MainActivity activity,
                       ArrayList<PostDataModel> postList,
                       PostSuccessListener listenerSuccess) {
        this.activity = activity;
        this.postDataModelArrayList=postList;
        this.listenerSuccess=listenerSuccess;

    }

    public ArrayList<PostDataModel> getPostDataRequest(){
        MainActivity.mDatabase.getReference().child("product").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for (DataSnapshot dataSnap:dataSnapshot.getChildren()){
                        PostDataModel postData=new PostDataModel();
                        postData.setImgHomePage(String.valueOf(dataSnap.child("strSharedImg").getValue()));
                        postData.setStrUserName(dataSnap.child("strName").getValue(String.class));
                        postData.setStrExplanation(dataSnap.child("strExplanation").getValue(String.class));
                        postData.setStrPrice(dataSnap.child("strPrice").getValue(String.class));
                        postData.setImgUser(dataSnap.child("strProfileImgUrl").getValue(String.class));
                        postData.setStrKey(dataSnap.child("strUserID").getValue(String.class));
                        postData.setStrStoreName(dataSnap.child("strStoreName").getValue(String.class));
                        postData.setStrSharedTime(dataSnap.child("strSharedTime").getValue(Long.class));
                        postData.setStrProductKey(dataSnap.getKey().trim());
                        postData.setLike(false);
                        likeCount=0;
                        for (DataSnapshot dataSnap2:dataSnap.child("strLike").getChildren()){
                            likeCount++;
                            if(MainActivity.myPreferences.isLoggedIn()){
                                if (MainActivity.mAuth.getUid().equals(dataSnap2.getKey().trim())){
                                    postData.setLike(true);
                                }
                            }
                        }
                        postData.setStrLikeCount(String.valueOf(likeCount));
                        postDataModelArrayList.add(postData);
                        listenerSuccess.onGetPostSuccessListener();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return postDataModelArrayList;

    }

    public interface PostSuccessListener {
        void onGetPostSuccessListener();
    }
}
