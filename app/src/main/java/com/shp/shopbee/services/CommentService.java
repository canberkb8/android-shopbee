package com.shp.shopbee.services;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.shp.shopbee.MainActivity;
import com.shp.shopbee.models.CommentModel;
import com.shp.shopbee.models.ShopBeeAppData;

import java.util.ArrayList;

public class CommentService {

    private MainActivity activity;

    ArrayList<CommentModel> postCommentArrayList;


    private final CommentService.CommentSuccessListener listenerSuccess;

    public CommentService(MainActivity activity,
                       ArrayList<CommentModel> postList,
                          CommentService.CommentSuccessListener listenerSuccess) {
        this.activity = activity;
        this.postCommentArrayList=postList;
        this.listenerSuccess=listenerSuccess;

    }

    public ArrayList<CommentModel> getCommentDataRequest(){
        MainActivity.mDatabase.getReference().child("product").child(ShopBeeAppData.getInstance().getProductKey()).child("strComments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for (DataSnapshot dataSnap:dataSnapshot.getChildren()){
                        CommentModel commentModel=new CommentModel();
                        commentModel.setStrName(dataSnap.child("strName").getValue(String.class));
                        commentModel.setStrComment(dataSnap.child("strComment").getValue(String.class));
                        commentModel.setStrImg(dataSnap.child("strImg").getValue(String.class));
                        postCommentArrayList.add(commentModel);
                        listenerSuccess.onGetCommentSuccessListener();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return postCommentArrayList;

    }

    public interface CommentSuccessListener {
        void onGetCommentSuccessListener();
    }
}
