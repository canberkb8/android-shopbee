package com.shp.shopbee.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;

import com.shp.shopbee.R;
import com.shp.shopbee.adapters.AdapterPostAnotherMessage;
import com.shp.shopbee.adapters.AdapterPostMyMessage;
import com.shp.shopbee.databinding.FragmMessageBinding;
import com.shp.shopbee.models.MessageModel;
import com.shp.shopbee.models.PostMessageModel;
import com.shp.shopbee.models.ShopBeeAppData;
import com.shp.shopbee.services.PostMessageService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;


public class MessageFragment extends BaseFragment {


    View view;
    FragmMessageBinding binding;
    private FirebaseAuth mAuth;

    String name,key,img,myKey,message;
    ArrayList<PostMessageModel> arrayList;


    private AdapterPostMyMessage adapterPostMyMessage;
    private AdapterPostAnotherMessage adapterPostAnotherMessage;
    PostMessageModel postMessageModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragm_message, container, false);
        view = binding.getRoot();

        mAuth = FirebaseAuth.getInstance();
        getProfilData();
        click();

        arrayList=new ArrayList<>();

        PostMessageService postMessageService = new PostMessageService(activity,arrayList,new PostMessageService.MessageSuccessListener() {
            @Override
            public void onGetMessageSuccessListener() {
                postMessageModel  = new PostMessageModel();
                if(ShopBeeAppData.getInstance().getPostMessageModelArrayList()!=null){
                    arrayList=ShopBeeAppData.getInstance().getPostMessageModelArrayList();
                    adapterPostMyMessage = new AdapterPostMyMessage(activity,arrayList);
                    binding.recyclerMessageItem.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                    Objects.requireNonNull(binding.recyclerMessageItem.getLayoutManager()).smoothScrollToPosition(binding.recyclerMessageItem,null, arrayList.size()-1);
                    binding.recyclerMessageItem.setHasFixedSize(true);
                    binding.recyclerMessageItem.setAdapter(adapterPostMyMessage);
                }
            }
        });
        postMessageService.getPostMessageData();
        return view;
    }

    private void getProfilData(){
        Bundle bundle = this.getArguments();
        if(bundle != null){
            name = bundle.getString("Name");
            img = bundle.getString("ProfileImg");
            key = bundle.getString("Key");
        }
    }


    private void click() {
        binding.messageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayList.clear();
                if (ShopBeeAppData.getInstance().getPostMessageModelArrayList().size() != 0) {
                    ShopBeeAppData.getInstance().getPostMessageModelArrayList().clear();
                }
                message = String.valueOf(binding.edtTxtMessage.getText());
                myKey = Objects.requireNonNull(mAuth.getUid()).trim();

                postMessageModel = new PostMessageModel(message,myKey);    //fb'de message node'una kaydeder
                MessageModel messageModel = new MessageModel(img,name,key,message);     //fb'de messagePage node'una kaydeder,kendı nodeuna kaydeder


                mDatabase.child("messagepage").child(myKey).child(key).setValue(messageModel);

                MessageModel myMessageModel = new MessageModel(ShopBeeAppData.getInstance().getUser().getUserProfileImg(),
                        ShopBeeAppData.getInstance().getUser().getUserName(),   //karşı tarafın nodeuna kaydeder
                        myKey,message);

                mDatabase.child("messagepage").child(key).child(myKey).setValue(myMessageModel);

                mDatabase.child("message").child(myKey).child(key).push().setValue(postMessageModel);
                mDatabase.child("message").child(key).child(myKey).push().setValue(postMessageModel);

                binding.edtTxtMessage.setText("");
            }

        });
    }
}
