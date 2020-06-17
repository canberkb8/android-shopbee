package com.shp.shopbee.services;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.shp.shopbee.MainActivity;
import com.shp.shopbee.models.PostDataModel;
import com.shp.shopbee.models.ShopBeeAppData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PostFollowService {

    private MainActivity activity;
    ArrayList<String> keys;
    ArrayList<PostDataModel> followArrayList;
    int likeCount;

    String key;

    private final FollowSuccessListener listenerSuccess;


    public PostFollowService(MainActivity activity,
                             FollowSuccessListener listenerSuccess) {
        this.activity = activity;
        this.listenerSuccess=listenerSuccess;

    }
    public void getKeys(){
        keys = new ArrayList<>();
        if(MainActivity.myPreferences.isLoggedIn()){
            MainActivity.mDatabase.getReference().child("follow").child(Objects.requireNonNull(MainActivity.mAuth.getUid())).child("following").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnap:dataSnapshot.getChildren()){
                        key = dataSnap.getKey();
                        keys.add(key);
                    }
                    getPostDataRequest();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    public List<PostDataModel> getPostDataRequest(){
        followArrayList = new ArrayList<>();
        MainActivity.mDatabase.getReference().child("product").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for (String item: keys){
                        for (DataSnapshot dataSnap:dataSnapshot.getChildren()){
                            if (item.equals(dataSnap.child("strUserID").getValue().toString())){
                                PostDataModel postData=new PostDataModel();
                                postData.setImgHomePage(String.valueOf(dataSnap.child("strSharedImg").getValue()));
                                postData.setStrUserName(dataSnap.child("strName").getValue(String.class));
                                postData.setStrExplanation(dataSnap.child("strExplanation").getValue(String.class));
                                postData.setStrPrice(dataSnap.child("strPrice").getValue(String.class));
                                postData.setImgUser(dataSnap.child("strProfileImgUrl").getValue(String.class));
                                postData.setStrKey(dataSnap.child("strUserID").getValue(String.class));
                                postData.setStrStoreName(dataSnap.child("strStoreName").getValue(String.class));
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
                                followArrayList.add(postData);
                            }
                        }

                    }
                    ShopBeeAppData.getInstance().setFollowArrayList(followArrayList);
                    listenerSuccess.onPostFollowSuccessListener();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return followArrayList;

    }

    public interface FollowSuccessListener {
        void onPostFollowSuccessListener();
    }
}
