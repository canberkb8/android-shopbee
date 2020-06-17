package com.shp.shopbee.services;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.shp.shopbee.MainActivity;
import com.shp.shopbee.models.MessageModel;
import com.shp.shopbee.models.ShopBeeAppData;

import java.util.ArrayList;
import java.util.Objects;

public class MessageService {

    private MainActivity activity;

    ArrayList<MessageModel> messageModelArrayList;

    private final MessageSuccessListener listenerSuccess;

    public MessageService(MainActivity activity,
                       MessageSuccessListener listenerSuccess) {
        this.activity = activity;
        this.listenerSuccess=listenerSuccess;

    }

    public ArrayList<MessageModel> getPostDataRequest(){
        messageModelArrayList = new ArrayList<>();
        MainActivity.mDatabase.getReference().child("messagepage").child(Objects.requireNonNull(MainActivity.mAuth.getUid())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messageModelArrayList.clear();
                if(dataSnapshot.exists()) {
                    for (DataSnapshot dataSnap:dataSnapshot.getChildren()){
                        MessageModel messageData=new MessageModel();

                        messageData.setStrUserImg(dataSnap.child("strUserImg").getValue(String.class));
                        messageData.setStrUserName(dataSnap.child("strUserName").getValue(String.class));
                        messageData.setStrKey(dataSnap.child("strKey").getValue(String.class));
                        messageData.setStrLastMessage(dataSnap.child("strLastMessage").getValue(String.class));
                        messageModelArrayList.add(messageData);
                    }
                    ShopBeeAppData.getInstance().setMessageModelArrayList(messageModelArrayList);
                    listenerSuccess.onGetMessageSuccessListener();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return messageModelArrayList;

    }

    public interface MessageSuccessListener {
        void onGetMessageSuccessListener();
    }

}
