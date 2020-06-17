package com.shp.shopbee.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.shp.shopbee.MainActivity;
import com.shp.shopbee.R;
import com.shp.shopbee.databinding.CardDesignPostanothermessageItemBinding;
import com.shp.shopbee.models.PostMessageModel;

import java.util.ArrayList;

public class AdapterPostAnotherMessage extends RecyclerView.Adapter<AdapterPostAnotherMessage.ViewHolderPostMessagePage> {

    private MainActivity activity;
    private ArrayList<PostMessageModel> postMessageModelArrayList;
    private CardDesignPostanothermessageItemBinding binding;

    public AdapterPostAnotherMessage(MainActivity activity, ArrayList<PostMessageModel> postMessageModelArrayList) {
        this.activity = activity;
        this.postMessageModelArrayList = postMessageModelArrayList;
    }

    @NonNull
    @Override
    public AdapterPostAnotherMessage.ViewHolderPostMessagePage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(activity),
                R.layout.card_design_postanothermessage_item,
                parent,
                false);
        return new AdapterPostAnotherMessage.ViewHolderPostMessagePage(binding);
    }

    //Her bir item'ı UI'da gösterdiğin method, (db'den dönen datanın atamaları)
    @Override
    public void onBindViewHolder(@NonNull AdapterPostAnotherMessage.ViewHolderPostMessagePage holder, int position) {
        PostMessageModel postMessageModel =postMessageModelArrayList.get(position);
        holder.bind(postMessageModel,position);
    }

    @Override
    public int getItemCount() {
        return postMessageModelArrayList.size();
    }

    public class ViewHolderPostMessagePage extends RecyclerView.ViewHolder {

        private CardDesignPostanothermessageItemBinding rowBinding;

        public ViewHolderPostMessagePage(CardDesignPostanothermessageItemBinding rowBinding) {
            super(binding.getRoot());
            this.rowBinding = rowBinding;
        }
        private void bind(final PostMessageModel postMessageModel, int position){
            rowBinding.cardTxtMessage.setText(postMessageModel.getMessage());
        }

    }
}

