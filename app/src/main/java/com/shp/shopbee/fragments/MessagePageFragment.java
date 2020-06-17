package com.shp.shopbee.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.shp.shopbee.MainActivity;
import com.shp.shopbee.R;
import com.shp.shopbee.adapters.AdapterMessagePage;
import com.shp.shopbee.databinding.FragmMessagepageBinding;
import com.shp.shopbee.models.MessageModel;
import com.shp.shopbee.models.ShopBeeAppData;
import com.shp.shopbee.services.MessageService;

import java.util.ArrayList;
import java.util.Collections;

public class MessagePageFragment extends BaseFragment {

    private View view;
    private FragmMessagepageBinding binding;

    ArrayList<MessageModel> arrayList;

    private AdapterMessagePage adapterMessagePage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragm_messagepage, container, false);
        view = binding.getRoot();


        arrayList=new ArrayList<>();
        MessageService messageService = new MessageService(activity, new MessageService.MessageSuccessListener() {
            @Override
            public void onGetMessageSuccessListener() {
                if(ShopBeeAppData.getInstance().getMessageModelArrayList()!=null){
                    arrayList=ShopBeeAppData.getInstance().getMessageModelArrayList();
                    adapterMessagePage = new AdapterMessagePage(activity,arrayList);
                    Collections.reverse(arrayList);
                    binding.recyclerMessagePageItem.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                    binding.recyclerMessagePageItem.scrollToPosition(0);
                    binding.recyclerMessagePageItem.setAdapter(adapterMessagePage);
                }
            }
        });
        messageService.getPostDataRequest();

        return view;
    }

}
