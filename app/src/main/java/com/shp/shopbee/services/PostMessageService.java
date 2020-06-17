package com.shp.shopbee.services;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.shp.shopbee.MainActivity;
import com.shp.shopbee.models.PostMessageModel;
import com.shp.shopbee.models.ShopBeeAppData;

import java.util.ArrayList;
import java.util.Objects;

public class PostMessageService {

    private MainActivity activity;

    ArrayList<PostMessageModel> sendMessageList;

    private final PostMessageService.MessageSuccessListener listenerSuccess;

    public PostMessageService(MainActivity activity,
                              ArrayList<PostMessageModel> sendMessageList,
                          PostMessageService.MessageSuccessListener listenerSuccess
    ) {
        this.activity = activity;
        this.sendMessageList=sendMessageList;
        this.listenerSuccess=listenerSuccess;
    }

    public ArrayList<PostMessageModel> getPostMessageData(){

        MainActivity.mDatabase.getReference()
                .child("message")
                .child(Objects.requireNonNull(MainActivity.mAuth.getUid()))
                .child(ShopBeeAppData.getInstance().getStrKey())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sendMessageList.clear();
                ShopBeeAppData.getInstance().setPostMessageModelArrayList(sendMessageList);
                if(dataSnapshot.exists()){
                    for (DataSnapshot dataSnap:dataSnapshot.getChildren()) {
                        PostMessageModel messageData=new PostMessageModel();
                        messageData.setStrKey(dataSnap.child("strKey").getValue().toString());
                        messageData.setMessage(dataSnap.child("message").getValue().toString());
                        sendMessageList.add(messageData);
                    }
                    ShopBeeAppData.getInstance().setPostMessageModelArrayList(sendMessageList);
                    listenerSuccess.onGetMessageSuccessListener();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return sendMessageList;

    }

    public interface MessageSuccessListener {
        void onGetMessageSuccessListener();
    }
}
