package com.shp.shopbee.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.shp.shopbee.MainActivity;
import com.shp.shopbee.R;
import com.shp.shopbee.adapters.AdapterPostData;
import com.shp.shopbee.databinding.FragmFollowpageBinding;
import com.shp.shopbee.models.PostDataModel;
import com.shp.shopbee.models.ShopBeeAppData;
import com.shp.shopbee.services.PostFollowService;

import java.util.ArrayList;
import java.util.Collections;
public class FollowPageFragment extends BaseFragment {

    View view;
    FragmFollowpageBinding binding;
    private FirebaseAuth mAuth;

    ArrayList<PostDataModel> arrayList;

    AdapterPostData adapterPostData;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragm_followpage, container, false);
        view = binding.getRoot();
        mAuth = FirebaseAuth.getInstance();

        arrayList=new ArrayList<>();

        PostFollowService postFollowService = new PostFollowService(activity, new PostFollowService.FollowSuccessListener() {
            @Override
            public void onPostFollowSuccessListener() {
                if(ShopBeeAppData.getInstance().getFollowArrayList()!=null){
                    arrayList=ShopBeeAppData.getInstance().getFollowArrayList();
                    adapterPostData = new AdapterPostData(activity,arrayList);

                    selectionsort(arrayList);
                    binding.recyclerFollowPage.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                    binding.recyclerFollowPage.setHasFixedSize(true);
                    binding.recyclerFollowPage.setAdapter(adapterPostData);
                }
            }
        });
        postFollowService.getKeys();
        cliks();
        return view;
    }

    public static ArrayList<PostDataModel> selectionsort(ArrayList<PostDataModel> arrayList)
    {
        PostDataModel model = new PostDataModel();
        int min;

        for(int i=0; i < arrayList.size()-1; i++)
        {
            min=i;
            for(int j=i; j < arrayList.size(); j++)
            {
                if (arrayList.get(j).getStrSharedTime() < arrayList.get(min).getStrSharedTime()){
                    min=j;
                }
            }
            model=arrayList.get(i);
            arrayList.set(i,arrayList.get(min));
            arrayList.set(min,model);
        }
        Collections.reverse(arrayList);
        return arrayList;
    }

    void cliks() {
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
}
