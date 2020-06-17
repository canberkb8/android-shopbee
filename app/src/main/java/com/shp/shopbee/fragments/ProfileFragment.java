package com.shp.shopbee.fragments;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shp.shopbee.MainActivity;
import com.shp.shopbee.R;
import com.shp.shopbee.adapters.AdapterMessagePage;
import com.shp.shopbee.databinding.FragmProfileBinding;
import com.shp.shopbee.models.ShopBeeAppData;
import com.shp.shopbee.services.MessageService;
import com.shp.shopbee.services.ProfileService;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends BaseFragment {

    View view;
    FragmProfileBinding binding;
    private String selectedImagePath;
    private StorageReference storageRef;
    private boolean changeImg;
    int followNumber=0;

    private Uri selectedImageUri,uriProfileImg;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragm_profile, container, false);
        view = binding.getRoot();


        loadUserData(); //User Datayı bastığın kısım

        clicks();

        return view;
    }

    private void getFollowersCount(){
        mDatabase.child("follow").child(Objects.requireNonNull(MainActivity.mAuth.getUid())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnap:dataSnapshot.child("followers").getChildren()){
                    followNumber ++;
                }
                binding.txtFollowers.setText("Followers : " + followNumber);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadUserData(){
        if(ShopBeeAppData.getInstance().getUser()!=null){
            Glide.with(activity)
                    .load(ShopBeeAppData.getInstance().getUser().getUserProfileImg())
                    .optionalCenterCrop()
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(binding.circleImgViewProfile);
            Log.e("profil",ShopBeeAppData.getInstance().getUser().getUserProfileImg());
            binding.edtTxtName.setText(ShopBeeAppData.getInstance().getUser().getUserName());
            binding.edtTxtEmail.setText(ShopBeeAppData.getInstance().getUser().getUserEmail());
            binding.edtTxtPhone.setText(ShopBeeAppData.getInstance().getUser().getUserPhone());

            binding.txtName.setText(ShopBeeAppData.getInstance().getUser().getUserName());
            binding.txtEmail.setText(ShopBeeAppData.getInstance().getUser().getUserEmail());
            binding.txtPhone.setText(ShopBeeAppData.getInstance().getUser().getUserPhone());
            getFollowersCount();
        }
    }


    void clicks(){
        binding.circleImgViewProfile.setEnabled(false);
        binding.circleImgViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 237);
            }
        });
        binding.btnEditOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.btnEdit.setVisibility(View.VISIBLE);
                binding.edtTxtPhone.setVisibility(View.VISIBLE);
                binding.edtTxtName.setVisibility(View.VISIBLE);
                binding.edtTxtEmail.setVisibility(View.VISIBLE);
                binding.btnEditOpen.setVisibility(View.INVISIBLE);
                binding.txtEmail.setVisibility(View.INVISIBLE);
                binding.txtName.setVisibility(View.INVISIBLE);
                binding.txtPhone.setVisibility(View.INVISIBLE);
                binding.circleImgViewProfile.setEnabled(true);
            }
        });
        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfileData();
                binding.btnEdit.setVisibility(View.INVISIBLE);
                binding.edtTxtPhone.setVisibility(View.INVISIBLE);
                binding.edtTxtName.setVisibility(View.INVISIBLE);
                binding.edtTxtEmail.setVisibility(View.INVISIBLE);
                binding.btnEditOpen.setVisibility(View.VISIBLE);
                binding.txtEmail.setVisibility(View.VISIBLE);
                binding.txtName.setVisibility(View.VISIBLE);
                binding.txtPhone.setVisibility(View.VISIBLE);
                binding.circleImgViewProfile.setEnabled(false);
            }
        });
        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mAuth.signOut();
                AuthUI.getInstance()
                        .signOut(activity)
                        .addOnCompleteListener(new OnCompleteListener<Void>(){

                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // do something here
                                //myPreferences.deletePreferencesValues();
                                MainActivity.myPreferences.setLoggedIn(false);
                                MainActivity.myPreferences.setToken("");
                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,new HomePageFragment()).commitAllowingStateLoss();
                            }
                        });
            }
        });
        binding.imgAdd.setOnClickListener(new View.OnClickListener() {
            private int MY_CAMERA_PERMISSION_CODE=100;
            @Override
            public void onClick(View v) {
                if (MainActivity.myPreferences.isLoggedIn()){
                    if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,new ShareFragment()).addToBackStack(null).commitAllowingStateLoss();
                    }else{
                        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                            Log.i("Camera","Camera permission is need to show the camera preview");
                        }
                        requestPermissions(new String[]{Manifest.permission.CAMERA},MY_CAMERA_PERMISSION_CODE);
                    }
                }else{
                    Toast.makeText(activity,"Paylaşım Yapmak için giriş yapmalısınız !!!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 237) {
                changeImg = true;
                selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                Glide.with(activity)
                        .load(selectedImageUri)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .optionalCenterCrop()
                        .into(binding.circleImgViewProfile);
                updateUploadImg(selectedImageUri);
            }
        }
    }



    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }



    private void updateUploadImg (final Uri selectedImageUri){
        binding.animationView.playAnimation();
        binding.animationView.setVisibility(View.VISIBLE);
        binding.btnEdit.setEnabled(false);
        binding.btnLogout.setEnabled(false);
        try {
            storageRef = FirebaseStorage.getInstance().getReference();
            final StorageReference imfRef = storageRef.child("profileImgs").child(selectedImageUri.getLastPathSegment()+"userId");
            imfRef.putFile(selectedImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Upload succeeded
                            imfRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(final Uri uri) {
                                    Log.d("TAG", "onSuccess: uri= "+ uri.toString());
                                    uriProfileImg=uri;
                                    Toast.makeText(activity,"uploadSucces",Toast.LENGTH_SHORT).show();
                                    binding.animationView.setVisibility(View.GONE);
                                    binding.animationView.cancelAnimation();
                                    binding.btnEdit.setEnabled(true);
                                    binding.btnLogout.setEnabled(true);
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Upload failed
                    Toast.makeText(activity, "Upload failed...", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void updateProfileData(){
        Map<String, Object> updates = new HashMap<>();
        updates.put("strName", binding.edtTxtName.getText().toString().trim());
        updates.put("strEmail", binding.edtTxtEmail.getText().toString().trim());
        updates.put("strPhone", binding.edtTxtPhone.getText().toString().trim());
        updatePostProfileImg(String.valueOf(uriProfileImg));
        if (changeImg){
            updates.put("strProfileImgUrl" , uriProfileImg.toString().trim());
            changeImg=false;
        }else{
            updatePostProfileImg(ShopBeeAppData.getInstance().getStrProfileUri());
        }
        Log.i("ProfileImg:", "ProfileImg: "+uriProfileImg);
        mDatabase.child("users").child(MainActivity.mAuth.getUid().trim()).updateChildren(updates);
    }

    private void updatePostProfileImg(final String updatedProfileImg){
        mDatabase.child("product").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnap:dataSnapshot.getChildren()){
                    if (dataSnap.child("strUserID").getValue().toString().trim().equals(MainActivity.mAuth.getUid())){
                        dataSnap.getRef().child("strName").setValue(binding.edtTxtName.getText().toString().trim());
                        if(changeImg){
                            dataSnap.getRef().child("strProfileImgUrl").setValue(updatedProfileImg);
                            changeImg=false;
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
