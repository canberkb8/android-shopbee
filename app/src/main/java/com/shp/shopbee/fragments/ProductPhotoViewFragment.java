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
import com.shp.shopbee.databinding.FragmProductViewBinding;

public class ProductPhotoViewFragment extends BaseFragment {

    View view;
    FragmProductViewBinding binding;
    String img;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragm_product_view, container, false);
        view = binding.getRoot();
        getProfilImg();
        binding.imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.getSupportFragmentManager().beginTransaction().remove(ProductPhotoViewFragment.this).commitAllowingStateLoss();
            }
        });
        return view;
    }
    private void getProfilImg(){
        Bundle bundle = this.getArguments();
        if(bundle != null){
            img = bundle.getString("ProductImg");
            Glide.with(activity).load(img).optionalCenterCrop().into(binding.imgProduct);
        }
    }
}
