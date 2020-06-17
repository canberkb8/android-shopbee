package com.shp.shopbee.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.shp.shopbee.R;
import com.shp.shopbee.databinding.FragmPostInfoBinding;
import com.shp.shopbee.models.PostDataModel;
import com.shp.shopbee.models.ShopBeeAppData;

public class PostInfoFragment extends BaseFragment {

    View view;
    FragmPostInfoBinding binding;
    String profileName,profileImg,productImg,productStoreName,productPrice,productExplanation,key;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragm_post_info, container, false);
        view = binding.getRoot();
        getProductData();

        clicks();
        return view;
    }


    private void click(){
        Bundle bundle = new Bundle();
        PublicProfileFragment fragPP = new PublicProfileFragment();
        bundle.putString("Name", profileName);
        bundle.putString("ProfileImg",profileImg);
        bundle.putString("Key",key);
        fragPP.setArguments(bundle);
        activity.getSupportFragmentManager().beginTransaction().add(R.id.main_frame_layout,fragPP).addToBackStack(null).commitAllowingStateLoss();
    }


    void clicks(){
        binding.txtUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click();
            }
        });
        binding.imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click();
            }
        });
        binding.imgShared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                ProductPhotoViewFragment fragPPV = new ProductPhotoViewFragment();
                bundle.putString("ProductImg",productImg);
                fragPPV.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().add(R.id.main_frame_layout,fragPPV).addToBackStack(null).commitAllowingStateLoss();
            }
        });
    }


    private void getProductData(){
        Bundle bundle = getArguments();
        if (bundle != null) {
            PostDataModel obj= (PostDataModel) bundle.getSerializable("post_obj");
            profileName = obj.getStrUserName();
            profileImg = obj.getImgUser();
            productImg = obj.getImgHomePage();
            productStoreName = obj.getStrStoreName();
            productPrice = obj.getStrPrice();
            productExplanation = obj.getStrExplanation();
            key = obj.getStrKey();
            writeData();
        }
    }

    private void writeData(){
        binding.txtUser.setText(profileName);
        binding.edtTstStoreName.setText(productStoreName);
        binding.edtTxtExplantion.setText(productExplanation);
        binding.edtTxtProductPrice.setText(productPrice);
        Glide.with(activity)
                .load(profileImg)
                .optionalCenterCrop()
                .into(binding.imgProfile);
        Glide.with(activity)
                .load(productImg)
                .optionalCenterCrop()
                .into(binding.imgShared);
    }
}
