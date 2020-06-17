package com.shp.shopbee.services;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.shp.shopbee.MainActivity;
import com.shp.shopbee.models.ShopBeeAppData;
import com.shp.shopbee.models.User;

import java.util.Objects;

public class ProfileService {

    private MainActivity activity;


    private final ProfileSuccessListener listenerSuccess;

    public ProfileService(MainActivity activity,ProfileSuccessListener listenerSuccess) {
        this.activity = activity;
        this.listenerSuccess=listenerSuccess;

    }

    public void profileRequest(){

        MainActivity.mDatabase.getReference().child("users").child(Objects.requireNonNull(MainActivity.mAuth.getUid()).trim()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    User user=new User();
                    user.setUserName(dataSnapshot.child("strName").getValue(String.class));
                    user.setUserEmail(dataSnapshot.child("strEmail").getValue(String.class));
                    user.setUserPhone(dataSnapshot.child("strPhone").getValue(String.class));
                    user.setUserProfileImg(dataSnapshot.child("strProfileImgUrl").getValue(String.class));

                    ShopBeeAppData.getInstance().setUser(user);
                    listenerSuccess.onProfileSuccessListener();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public interface ProfileSuccessListener {
        void onProfileSuccessListener();
    }
}
