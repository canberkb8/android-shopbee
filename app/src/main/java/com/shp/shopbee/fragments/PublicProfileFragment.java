package com.shp.shopbee.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.shp.shopbee.MainActivity;
import com.shp.shopbee.R;
import com.shp.shopbee.databinding.FragmPublicPorfileBinding;
import com.shp.shopbee.models.ShopBeeAppData;


public class PublicProfileFragment extends BaseFragment {

    View view;
    FragmPublicPorfileBinding binding;
    private FirebaseAuth mAuth;
    String key,img,name;
    public int followNumber=0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragm_public_porfile, container, false);
        view = binding.getRoot();
        mAuth = FirebaseAuth.getInstance();
        getProfilData();
        checkFollow();
        if (!MainActivity.myPreferences.isLoggedIn() || key.equals(mAuth.getUid())){
            binding.btnFollow.setVisibility(View.INVISIBLE);
            binding.btnMessage.setVisibility(View.INVISIBLE);
        }
        click();
        return view;
    }

    void click(){
        binding.btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                following();
            }
        });
        binding.circleImgViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                ProfilePhotoViewFragment fragPV = new ProfilePhotoViewFragment();
                bundle.putString("ProfileImg",img);
                fragPV.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().add(R.id.main_frame_layout,fragPV).addToBackStack(null).commitAllowingStateLoss();
            }
        });
        binding.btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                MessageFragment fragMsgF = new MessageFragment();
                bundle.putString("Name" , name);
                bundle.putString("ProfileImg" , img);
                bundle.putString("Key" , key);
                fragMsgF.setArguments(bundle);
                ShopBeeAppData.getInstance().setStrKey(key);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,fragMsgF).addToBackStack(null).commitAllowingStateLoss();
            }
        });
    }

    private void getProfilData(){
        Bundle bundle = this.getArguments();
        if(bundle != null){
            name = bundle.getString("Name");
            img = bundle.getString("ProfileImg");
            key = bundle.getString("Key");
            binding.txtProfileName.setText("Name : " + name);
            Glide.with(activity).load(img).into(binding.circleImgViewProfile);
        }
    }
    private void following(){
        if (binding.btnFollow.getText().toString().equals("Follow")){
            mDatabase.child("follow").child(mAuth.getUid()).child("following").child(key).setValue(true);
            mDatabase.child("follow").child(key).child("followers").child(mAuth.getUid()).setValue(true);
            binding.btnFollow.setText("Following");
        }else{
            mDatabase.child("follow").child(mAuth.getUid()).child("following").child(key).removeValue();
            mDatabase.child("follow").child(key).child("followers").child(mAuth.getUid()).removeValue();
            binding.btnFollow.setText("Follow");
        }
    }
    private void checkFollow(){
        mDatabase.child("follow").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (MainActivity.myPreferences.isLoggedIn()){
                    if (dataSnapshot.child(mAuth.getUid()).child("following").child(key).exists()){
                        binding.btnFollow.setText("Following");
                        followNumber = 0;
                    }
                    else{
                        binding.btnFollow.setText("Follow");
                        followNumber = 0;
                    }
                }
                for (DataSnapshot dataSnap:dataSnapshot.child(key).child("followers").getChildren()){
                    followNumber ++;
                    Log.i("Follownumber", "Follow Number :" + followNumber );
                }
                binding.txtFollowers.setText("Followers : "+ followNumber);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}