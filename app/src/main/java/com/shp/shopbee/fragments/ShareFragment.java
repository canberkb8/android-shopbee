package com.shp.shopbee.fragments;

import android.content.Intent;
import android.graphics.Bitmap;


import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.Continuation;
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
import com.mindorks.paracamera.Camera;
import com.shp.shopbee.R;
import com.shp.shopbee.databinding.FragmSharepageBinding;
import com.shp.shopbee.models.SharedModel;
import com.shp.shopbee.models.ShopBeeAppData;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Date;

public class ShareFragment extends BaseFragment {

    View view;
    FragmSharepageBinding binding;

    private StorageReference storageRef;
    Camera camera;
    SharedModel sharedModel;
    FirebaseAuth mAuth;
    FirebaseStorage firebaseStorage;
    String strPrice,strStoreName,strExplantion,strSharedImg,key;
    Date strSharedDate;
    long strSharedTime;
    private InterstitialAd mInterstitialAd;
    boolean ImgCount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragm_sharepage, container, false);
        view = binding.getRoot();
        takePhoto();
        mAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageRef=firebaseStorage.getReferenceFromUrl("gs://fb-shopbee.appspot.com");
        getProfileData();
        buildAd();
        binding.imgShared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        binding.btnProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedProcess();
                showAd();
            }
        });
        return view;
    }

    public void takePhoto(){
        camera = new Camera.Builder()
                .resetToCorrectOrientation(true)// it will rotate the camera bitmap to the correct orientation from meta data
                .setTakePhotoRequestCode(1)
                .setDirectory("pics")
                .setName("ali_" + System.currentTimeMillis())
                .setImageFormat(Camera.IMAGE_JPEG)
                .setCompression(75)
                .setImageHeight(1000)// it will try to achieve this height as close as possible maintaining the aspect ratio;
                .build(this);
        try {
            camera.takePicture();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Camera.REQUEST_TAKE_PHOTO) {
            Bitmap bitmap = camera.getCameraBitmap();
            if (bitmap != null) {
                Glide.with(activity).load(bitmap).into(binding.imgShared);
                storageRef = FirebaseStorage.getInstance().getReference();
                uploadImage(bitmap);
                ImgCount = true;
            } else {
                Toast.makeText(activity, "Picture not taken!", Toast.LENGTH_SHORT).show();
                ImgCount = false;
            }
        }
    }
    private  void getProfileData(){
        mDatabase.child("users").child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 ShopBeeAppData.getInstance().setStrName(String.valueOf(dataSnapshot.child("strName").getValue()));
                 ShopBeeAppData.getInstance().setStrProfileUri(String.valueOf(dataSnapshot.child("strProfileImgUrl").getValue()));
                 Log.i("Name", "Name: "+dataSnapshot.child("strName").getValue(String.class));

               /* for (DataSnapshot dataSnap:dataSnapshot.getChildren()){

                }*/
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void uploadImage(Bitmap bitmap) {
        key = mDatabase.child("product").push().getKey();
        final StorageReference ref =  storageRef.child("Productİmg/" + key + ".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();

        final UploadTask uploadTask = ref.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(activity, "Uploaded", Toast.LENGTH_SHORT).show();
                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return ref.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downUri = task.getResult();
                            Log.d("Final URL", "onComplete: Url: " + downUri.toString());
                            strSharedImg = downUri.toString();
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(activity, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void buildAd(){
        MobileAds.initialize(activity,"ca-app-pub-5075751572546757~1990600297");
        mInterstitialAd = new InterstitialAd(activity);
        mInterstitialAd.setAdUnitId("ca-app-pub-5075751572546757/4426026092");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }
    private void showAd(){
        if (mInterstitialAd.isLoaded()){
            mInterstitialAd.show();
        }
        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                //Yuklendıgınde
                Log.d("onAdLoaded", "onAdLoaded: onAdLoaded" );
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

            @Override
            public void onAdFailedToLoad(int i) {
                //Hatada

                Log.d("onAdFailedToLoad", "onAdFailedToLoad: onAdFailedToLoad" );
            }

            @Override
            public void onAdOpened() {
                //acıldıgında
                Log.d("onAdOpened", "onAdOpened: onAdOpened" );
            }

            @Override
            public void onAdLeftApplication() {
                //Reklama tıklanıp app kapandıgında
                Log.d("onAdLeftApplication", "onAdLeftApplication: onAdLeftApplication" );
            }

            @Override
            public void onAdClosed() {
                //Kapatılınca (tekrar yukleme ıslemı yapılır genelde --------> mInterstitialAd.loadAd(new AdRequest.Builder().build()); )
                Log.d("onAdClosed", "onAdClosed: onAdClosed" );
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });
    }
    private void sharedProcess(){
        if(binding.edtTstStoreName.getText().toString().trim().isEmpty()
                ||binding.txtProductPrice.getText().toString().trim().isEmpty()
                ||binding.txtExplanation.getText().toString().trim().isEmpty()
                || !ImgCount){
            Toast.makeText(activity, "Please fill in the blank", Toast.LENGTH_LONG).show();
        }else{
            strStoreName=binding.edtTstStoreName.getText().toString().trim();
            strPrice = binding.edtTxtProductPrice.getText().toString().trim();
            strExplantion = binding.edtTxtExplantion.getText().toString().trim();
            strSharedDate = Calendar.getInstance().getTime();
            strSharedTime = strSharedDate.getTime();
            sharedModel=new SharedModel(ShopBeeAppData.getInstance().getStrName(),ShopBeeAppData.getInstance().getStrProfileUri(),strSharedImg,strStoreName,strPrice,strExplantion,mAuth.getUid(),strSharedTime);
            mDatabase.child("product").child(key).setValue(sharedModel);
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,new HomePageFragment()).commitAllowingStateLoss();
        }
    }

}