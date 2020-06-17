package com.shp.shopbee.adapters;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.shp.shopbee.MainActivity;
import com.shp.shopbee.R;
import com.shp.shopbee.databinding.CardDesignPostItemBinding;
import com.shp.shopbee.fragments.CommentFragment;
import com.shp.shopbee.fragments.HomePageFragment;
import com.shp.shopbee.fragments.PostInfoFragment;
import com.shp.shopbee.fragments.PublicProfileFragment;
import com.shp.shopbee.models.CommentModel;
import com.shp.shopbee.models.PostDataModel;
import com.shp.shopbee.models.SharedModel;
import com.shp.shopbee.models.ShopBeeAppData;
import com.shp.shopbee.services.PostService;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.Objects;

public class AdapterPostData extends RecyclerView.Adapter<AdapterPostData.ViewHolderHomePage> {

    //Global Kısım
    private MainActivity activity;
    private ArrayList<PostDataModel> homePageArrayList;
    private CardDesignPostItemBinding binding;
    private String key;


    public AdapterPostData(MainActivity activity, ArrayList<PostDataModel> homePageArrayList) {
        this.activity = activity;
        this.homePageArrayList = homePageArrayList;
    }

    @NonNull
    @Override
    public ViewHolderHomePage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {     //card_design_post_item'i yaratır.
        binding = DataBindingUtil.inflate(LayoutInflater.from(activity),
                R.layout.card_design_post_item,
                parent,
                false);
        return new ViewHolderHomePage(binding);
    }

    //Her bir item'ı UI'da gösterdiğin method, (db'den dönen datanın atamaları)
    @Override
    public void onBindViewHolder(@NonNull ViewHolderHomePage holder, int position) {
        PostDataModel postDataModel =homePageArrayList.get(position);   //ArrayList'i HomePageFragmentta doldurduk
        holder.bind(postDataModel,position);
    }

    @Override
    public int getItemCount() {
        return homePageArrayList.size();
    }


    public class ViewHolderHomePage extends RecyclerView.ViewHolder {

        private CardDesignPostItemBinding rowBinding;

        public ViewHolderHomePage(CardDesignPostItemBinding rowBinding) {
            super(binding.getRoot());
            this.rowBinding = rowBinding;
        }
        private void bind(final PostDataModel postDataModel, final int position){

            if (postDataModel.isLike()){
                rowBinding.imgLike.setVisibility(View.INVISIBLE);
                rowBinding.imgRedlike.setVisibility(View.VISIBLE);
            }else{
                rowBinding.imgLike.setVisibility(View.VISIBLE);
                rowBinding.imgRedlike.setVisibility(View.INVISIBLE);
            }

            rowBinding.txtLike.setText(postDataModel.getStrLikeCount()+" Like");

            Glide.with(activity)
                    .load(postDataModel.getImgUser())
                    .optionalCenterCrop()
                    .into(rowBinding.cardImgProfile);

            rowBinding.cardTxtUser.setText(postDataModel.getStrUserName());

            Glide.with(activity)
                    .load(postDataModel.getImgHomePage())
                    .optionalCenterCrop()
                    .into(rowBinding.cardImgPostItem);



            rowBinding.cardImgProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    PublicProfileFragment fragPP = new PublicProfileFragment();
                    bundle.putString("Name", postDataModel.getStrUserName());
                    bundle.putString("ProfileImg", postDataModel.getImgUser());
                    bundle.putString("Key", postDataModel.getStrKey());
                    fragPP.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().add(R.id.main_frame_layout,fragPP).addToBackStack(null).commitAllowingStateLoss();
                }
            });
            rowBinding.cardTxtUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    PublicProfileFragment fragPP = new PublicProfileFragment();
                    bundle.putString("Name", postDataModel.getStrUserName());
                    bundle.putString("ProfileImg", postDataModel.getImgUser());
                    bundle.putString("Key", postDataModel.getStrKey());
                    fragPP.setArguments(bundle);
                    ShopBeeAppData.getInstance().setStrName(postDataModel.getStrUserName());
                    activity.getSupportFragmentManager().beginTransaction().add(R.id.main_frame_layout,fragPP).addToBackStack(null).commitAllowingStateLoss();
                }
            });
            rowBinding.cardImgPostItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("post_obj", postDataModel);
                    PostInfoFragment postInfoFragment = new PostInfoFragment();
                    postInfoFragment.setArguments(bundle);

                    activity.getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.main_frame_layout,postInfoFragment)
                            .addToBackStack(null)
                            .commitAllowingStateLoss();

                }
            });
            if(MainActivity.myPreferences.isLoggedIn()){
                rowBinding.btnCommentSend.setVisibility(View.VISIBLE);
                rowBinding.edtTxtComment.setVisibility(View.VISIBLE);
                rowBinding.btnCommentSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(rowBinding.edtTxtComment.getText().toString().trim().isEmpty()){
                        Toast.makeText(activity, "Please fill in the blank", Toast.LENGTH_LONG).show();
                    }else{
                        key = MainActivity.mDatabase.getReference().child("product").child(postDataModel.getStrProductKey().trim()).child("strComments").push().getKey();
                        CommentModel commentModel =new CommentModel(ShopBeeAppData.getInstance().getUser().getUserName(),ShopBeeAppData.getInstance().getUser().getUserProfileImg(),rowBinding.edtTxtComment.getText().toString().trim());
                        MainActivity.mDatabase.getReference().child("product").child(postDataModel.getStrProductKey().trim()).child("strComments").child(key).setValue(commentModel);
                        Toast.makeText(activity,"Comment Successful",Toast.LENGTH_SHORT).show();
                        rowBinding.edtTxtComment.setText("");

                        ShopBeeAppData.getInstance().setUpdatedPosition(position);
                        homePageArrayList.clear();
                    }
                }
            });
            }else{
                rowBinding.btnCommentSend.setVisibility(View.INVISIBLE);
                rowBinding.edtTxtComment.setVisibility(View.INVISIBLE);
            }
            if(MainActivity.myPreferences.isLoggedIn()){
                rowBinding.imgLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.mDatabase.getReference()
                            .child("product")
                            .child(postDataModel.getStrProductKey().trim())
                            .child("strLike")
                            .child(Objects.requireNonNull(MainActivity.mAuth.getUid()))
                            .setValue(true);

                    ShopBeeAppData.getInstance().setUpdatedPosition(position);
                    homePageArrayList.clear();

                    rowBinding.txtLike.setText((Integer.parseInt(postDataModel.getStrLikeCount())+1)+" Like");
                    rowBinding.imgLike.setVisibility(View.INVISIBLE);
                    rowBinding.imgRedlike.setVisibility(View.VISIBLE);
                }
            });
            }else{
                rowBinding.imgLike.setVisibility(View.INVISIBLE);
                rowBinding.txtLike.setVisibility(View.INVISIBLE);
            }
            if(MainActivity.myPreferences.isLoggedIn()){
                rowBinding.imgRedlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.mDatabase.getReference()
                            .child("product")
                            .child(postDataModel.getStrProductKey().trim())
                            .child("strLike")
                            .child(Objects.requireNonNull(MainActivity.mAuth.getUid()))
                            .removeValue();

                    ShopBeeAppData.getInstance().setUpdatedPosition(position);
                    homePageArrayList.clear();

                    rowBinding.txtLike.setText(Integer.parseInt(postDataModel.getStrLikeCount())+" Like");
                    rowBinding.imgLike.setVisibility(View.VISIBLE);
                    rowBinding.imgRedlike.setVisibility(View.INVISIBLE);
                }
            });
            }else{
                rowBinding.imgRedlike.setVisibility(View.INVISIBLE);
                rowBinding.txtLike.setVisibility(View.INVISIBLE);
            }

            rowBinding.txtComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShopBeeAppData.getInstance().setProductKey(postDataModel.getStrProductKey());
                    activity.getSupportFragmentManager().beginTransaction().add(R.id.main_frame_layout,new CommentFragment()).addToBackStack(null).commitAllowingStateLoss();
                }
            });
        }

    }
}
