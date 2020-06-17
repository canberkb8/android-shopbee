package com.shp.shopbee.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shp.shopbee.MainActivity;
import com.shp.shopbee.R;
import com.shp.shopbee.databinding.CardDesignCommentBinding;
import com.shp.shopbee.models.CommentModel;

import java.util.ArrayList;

public class AdapterComment extends RecyclerView.Adapter<AdapterComment.ViewHolderComment>{

    private MainActivity activity;
    private ArrayList<CommentModel> commentModelArrayList;
    private CardDesignCommentBinding binding;

    public AdapterComment(MainActivity activity, ArrayList<CommentModel> commentModelArrayList) {
        this.activity = activity;
        this.commentModelArrayList = commentModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolderComment onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(activity),
                R.layout.card_design_comment,
                parent,
                false);
        return new ViewHolderComment(binding);
    }

    //Her bir item'ı UI'da gösterdiğin method, (db'den dönen datanın atamaları)
    @Override
    public void onBindViewHolder(@NonNull ViewHolderComment holder, int position) {
        CommentModel commentModel =commentModelArrayList.get(position);
        holder.bind(commentModel,position);
    }

    @Override
    public int getItemCount() {
        return commentModelArrayList.size();
    }

    public class ViewHolderComment extends RecyclerView.ViewHolder {

        private CardDesignCommentBinding rowBinding;

        public ViewHolderComment(CardDesignCommentBinding rowBinding) {
            super(binding.getRoot());
            this.rowBinding = rowBinding;
        }
        private void bind(final CommentModel commentModel, int position){
            Glide.with(activity)
                    .load(commentModel.getStrImg())
                    .optionalCenterCrop()
                    .into(rowBinding.cardImgProfile);
            rowBinding.cardTxtUser.setText(commentModel.getStrName());
            rowBinding.cardTxtComment.setText(commentModel.getStrComment());
        }
    }
}
