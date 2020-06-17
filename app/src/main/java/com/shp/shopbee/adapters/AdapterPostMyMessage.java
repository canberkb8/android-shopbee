package com.shp.shopbee.adapters;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.shp.shopbee.MainActivity;
import com.shp.shopbee.R;
import com.shp.shopbee.databinding.CardDesignPostanothermessageItemBinding;
import com.shp.shopbee.databinding.CardDesignPostmymessageItemBinding;
import com.shp.shopbee.models.PostMessageModel;

import java.util.ArrayList;

public class AdapterPostMyMessage extends RecyclerView.Adapter<AdapterPostMyMessage.ViewHolderPostMessagePage> {

    private MainActivity activity;
    private ArrayList<PostMessageModel> postMessageModelArrayList;
    private CardDesignPostmymessageItemBinding binding;
    public AdapterPostMyMessage(MainActivity activity, ArrayList<PostMessageModel> postMessageModelArrayList) {
        this.activity = activity;
        this.postMessageModelArrayList = postMessageModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolderPostMessagePage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(activity),
                R.layout.card_design_postmymessage_item,
                parent,
                false);
        return new ViewHolderPostMessagePage(binding);
    }

    //Her bir item'ı UI'da gösterdiğin method, (db'den dönen datanın atamaları)
    @Override
    public void onBindViewHolder(@NonNull ViewHolderPostMessagePage holder, int position) {
        PostMessageModel postMessageModel =postMessageModelArrayList.get(position);
        holder.bind(postMessageModel,position);
    }

    @Override
    public int getItemCount() {
        return postMessageModelArrayList.size();
    }

    public class ViewHolderPostMessagePage extends RecyclerView.ViewHolder {

        private CardDesignPostmymessageItemBinding rowBinding;

        public ViewHolderPostMessagePage(CardDesignPostmymessageItemBinding rowBinding) {
            super(binding.getRoot());
            this.rowBinding = rowBinding;
        }
        private void bind(final PostMessageModel postMessageModel, int position){
            if (postMessageModel.getStrKey().equals(MainActivity.mAuth.getUid())){
                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                params.setMarginStart(96);
                params.setMarginEnd(32);
                rowBinding.txtMessage.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
                rowBinding.txtMessage.setLayoutParams(params);
                rowBinding.imgRight.setVisibility(View.VISIBLE);
                rowBinding.imgLeft.setVisibility(View.INVISIBLE);
                rowBinding.txtMessage.setText(postMessageModel.getMessage());
            }else{
                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                params.setMarginStart(32);
                params.setMarginEnd(96);
                rowBinding.txtMessage.setGravity(Gravity.LEFT | Gravity.BOTTOM);
                rowBinding.txtMessage.setLayoutParams(params);
                rowBinding.imgRight.setVisibility(View.INVISIBLE);
                rowBinding.imgLeft.setVisibility(View.VISIBLE);
                rowBinding.txtMessage.setText(postMessageModel.getMessage());
            }
        }

    }

}
