package com.shp.shopbee.fragments;

import android.content.Context;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shp.shopbee.MainActivity;


//tamamen soyut class, layoutu yok
public abstract  class BaseFragment extends Fragment{

    private String TAG="BaseFragment";
    public int count = 0;

    protected MainActivity activity;
    protected DatabaseReference mDatabase;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = ((MainActivity) context);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

}
