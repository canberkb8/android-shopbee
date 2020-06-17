package com.shp.shopbee.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shp.shopbee.MainActivity;
import com.shp.shopbee.R;
import com.shp.shopbee.databinding.CardDesignMesseageItemBinding;
import com.shp.shopbee.fragments.MessageFragment;
import com.shp.shopbee.fragments.PostInfoFragment;
import com.shp.shopbee.fragments.PublicProfileFragment;
import com.shp.shopbee.models.MessageModel;
import com.shp.shopbee.models.ShopBeeAppData;
import com.shp.shopbee.services.PostMessageService;

import java.util.ArrayList;

public class AdapterMessagePage extends RecyclerView.Adapter<AdapterMessagePage.ViewHolderMessagePage>{

    private MainActivity activity;
    private ArrayList<MessageModel> messageModelArrayList;
    private CardDesignMesseageItemBinding binding;

    public AdapterMessagePage(MainActivity activity, ArrayList<MessageModel> messageModelArrayList) {
        this.activity = activity;
        this.messageModelArrayList = messageModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolderMessagePage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(activity),
                R.layout.card_design_messeage_item,
                parent,
                false);
        return new ViewHolderMessagePage(binding);
    }

    //Her bir item'ı UI'da gösterdiğin method, (db'den dönen datanın atamaları)
    @Override
    public void onBindViewHolder(@NonNull ViewHolderMessagePage holder, int position) {
        MessageModel messageModel =messageModelArrayList.get(position);
        holder.bind(messageModel,position);
    }

    @Override
    public int getItemCount() {
        return messageModelArrayList.size();
    }

    public class ViewHolderMessagePage extends RecyclerView.ViewHolder {

        private CardDesignMesseageItemBinding rowBinding;

        public ViewHolderMessagePage(CardDesignMesseageItemBinding rowBinding) {
            super(binding.getRoot());
            this.rowBinding = rowBinding;
        }
        private void bind(final MessageModel messageModel, int position){


            Glide.with(activity)
                    .load(messageModel.getStrUserImg())
                    .optionalCenterCrop()
                    .into(rowBinding.cardImgProfile);
            rowBinding.cardTxtUser.setText(messageModel.getStrUserName());
            rowBinding.cardTxtMessage.setText(messageModel.getStrLastMessage());
            binding.cardGeneral.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShopBeeAppData.getInstance().setStrKey(messageModel.getStrKey());
                }
            });
            binding.cardGeneral.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    MessageFragment fragMF = new MessageFragment();
                    bundle.putString("Name", messageModel.getStrUserName());
                    bundle.putString("Key", messageModel.getStrKey());
                    bundle.putString("ProfileImg", messageModel.getStrUserImg());
                    fragMF.setArguments(bundle);
                    ShopBeeAppData.getInstance().setStrKey(messageModel.getStrKey());
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,fragMF).addToBackStack(null).commitAllowingStateLoss();
                }
            });


        }

    }
}
