package com.shp.shopbee.fragments;

import android.content.Intent;
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
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shp.shopbee.MainActivity;
import com.shp.shopbee.R;
import com.shp.shopbee.databinding.FragmRegisterBinding;
import com.shp.shopbee.models.RegisterModel;
import com.shp.shopbee.models.ShopBeeAppData;

import static android.app.Activity.RESULT_OK;


public class RegisterFragment extends BaseFragment{

    private String TAG="RegisterFragment";

    View view;
    FragmRegisterBinding binding;

    private FirebaseAuth mAuth;
    private String selectedImagePath;

    private FirebaseStorage firebaseStorage;
    private StorageReference storageRef;

    private Uri selectedImageUri,uriProfileImg;
    String strName,strEmail,strPassword,strPhone;
    RegisterModel registerModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragm_register, container, false);
        view = binding.getRoot();
        binding.animationView.cancelAnimation();
        mAuth = FirebaseAuth.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        storageRef=firebaseStorage.getReferenceFromUrl("gs://fb-shopbee.appspot.com");



        binding.circleImgViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 237);
            }
        });

        registerProcess();  //Register kontrolleri, register olma
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 237) {
                selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                Glide.with(activity).load(selectedImageUri).into(binding.circleImgViewProfile);
            }
        }
    }

    private void uploadImg(final Uri selectedImageUri){
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
                                    Log.d(TAG, "onSuccess: uri= "+ uri.toString());
                                    uriProfileImg=uri;
                                    Toast.makeText(activity,"uploadSucces",Toast.LENGTH_SHORT).show();
                                    registerModel=new RegisterModel(strName,strEmail,strPassword,strPhone,uriProfileImg.toString());
                                    ShopBeeAppData.getInstance().setStrName(strName);
                                    ShopBeeAppData.getInstance().setStrProfileUri(uriProfileImg.toString());
                                    mDatabase.child("users").child(mAuth.getUid()).setValue(registerModel);
                                    mAuth.signOut();
                                    AuthUI.getInstance()
                                            .signOut(activity)
                                            .addOnCompleteListener(new OnCompleteListener<Void>(){

                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    // do something here
                                                    //myPreferences.deletePreferencesValues();
                                                    MainActivity.myPreferences.setLoggedIn(false);
                                                    MainActivity.myPreferences.setToken("");
                                                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,new HomePageFragment()).commit();
                                                }
                                            });
                                    binding.animationView.setVisibility(View.GONE);
                                    binding.animationView.cancelAnimation();
                                    try {
                                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,new HomePageFragment()).commitAllowingStateLoss();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
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

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    private void registerProcess(){
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.animationView.playAnimation();
                binding.animationView.setVisibility(View.VISIBLE);

                if(binding.edtTxtName.getText().toString().trim().isEmpty()
                        ||binding.edtTxtEmail.getText().toString().trim().isEmpty()
                        ||binding.edtTxtPassword.getText().toString().trim().isEmpty()
                        ||binding.edtTxtPhone.getText().toString().trim().isEmpty()
                        ||selectedImageUri==null){

                    Toast.makeText(activity, "please fill in the blank", Toast.LENGTH_SHORT).show();
                    binding.animationView.setVisibility(View.GONE);
                    binding.animationView.cancelAnimation();
                }else{
                    if(MainActivity.myPreferences.isLoggedIn()){   //trueysa buraya gircek
                        Toast.makeText(activity, "Lütfen çıkış yapınız", Toast.LENGTH_SHORT).show();
                        binding.animationView.setVisibility(View.GONE);
                        binding.animationView.cancelAnimation();
                    }else{  //false ise buraya gircek
                        mAuth.createUserWithEmailAndPassword(binding.edtTxtEmail.getText().toString().trim(), binding.edtTxtPassword.getText().toString().trim())
                                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Log.i(TAG, "createUserWithEmail:success");
                                            Log.i(TAG,mAuth.getUid());  //register'dan dönen auth tokeni aldık.

                                           // myPreferences.setUserRegistered(true);
                                           // myPreferences.setToken(mAuth.getUid());

                                            strName=binding.edtTxtName.getText().toString().trim();
                                            strEmail=binding.edtTxtEmail.getText().toString().trim();
                                            strPassword=binding.edtTxtPassword.getText().toString().trim();
                                            strPhone=binding.edtTxtPhone.getText().toString().trim();
                                            MainActivity.myPreferences.setUserRegistered(true);
                                            uploadImg(selectedImageUri);
                                            //uploadVideo(selectedImageUri);



                                            // mDatabase.child("users").child(myPreferences.getToken()).setValue(registerModel);   //RealtimeDB'ye registerModel'i kaydettik.


                                            //uploadFileAndSaveToFireBase(selectedImageUri);


                                            //lottie'yi visibility Gone Yap, ve animation'u cancel et
                                        } else {
                                            Log.e(TAG, "createUserWithEmail:Response is not successful");
                                            //lottie'yi visibility Gone Yap, ve animation'u cancel et
                                            binding.animationView.setVisibility(View.GONE);
                                            binding.animationView.cancelAnimation();
                                        }
                                    }
                                });
                    }
                }
            }
        });
    }
}
