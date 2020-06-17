package com.shp.shopbee.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.shp.shopbee.R;
import com.shp.shopbee.adapters.AdapterComment;
import com.shp.shopbee.databinding.FragmCommentBinding;
import com.shp.shopbee.models.CommentModel;
import com.shp.shopbee.models.ShopBeeAppData;
import com.shp.shopbee.services.CommentService;

import java.util.ArrayList;
import java.util.Collections;

public class CommentFragment extends BaseFragment {

    View view;
    FragmCommentBinding binding;
    ArrayList<CommentModel> arrayList;
    AdapterComment adapterComment;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragm_comment, container, false);
        view = binding.getRoot();

        arrayList=new ArrayList<>();

        CommentService commentService = new CommentService(activity,arrayList,new CommentService.CommentSuccessListener(){
            @Override
            public void onGetCommentSuccessListener() {
                adapterComment = new AdapterComment(activity,arrayList);
                Collections.reverse(arrayList);
                binding.recyclerComment.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                binding.recyclerComment.setHasFixedSize(true);
                binding.recyclerComment.setAdapter(adapterComment);
            }
        });
        commentService.getCommentDataRequest();

        return view;
    }

}
