package com.shp.shopbee.utils;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.shp.shopbee.MainActivity;
import com.shp.shopbee.R;
import com.shp.shopbee.databinding.PopupLoginBinding;
import com.shp.shopbee.fragments.HomePageFragment;
import com.shp.shopbee.fragments.RegisterFragment;
import com.shp.shopbee.local.Preferences;
import com.shp.shopbee.services.LoginService;
import com.shp.shopbee.services.ProfileService;


public class PopupSignIn {

    public PopupSignIn(final MainActivity activity){
        final Preferences myPreferences=new Preferences(activity);
        final PopupLoginBinding loginBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.popup_login, null, false);
        final Dialog popupLogin=new Dialog(activity);
        popupLogin.setContentView(loginBinding.getRoot());
        popupLogin.show();

        loginBinding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (loginBinding.edtTxtLoginEmail.getText().toString().trim().isEmpty()
                            || loginBinding.edtTxtLoginPw.getText().toString().trim().isEmpty()) {
                        Toast.makeText(activity, "please fill in the blanks", Toast.LENGTH_SHORT).show();
                    } else {
                        LoginService loginService = new LoginService(activity, new LoginService.LoginSuccessListener() {
                            @Override
                            public void onSuccessLoginListener() {
                                ProfileService profileService=new ProfileService(activity, new ProfileService.ProfileSuccessListener() {
                                    @Override
                                    public void onProfileSuccessListener() {
                                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,new HomePageFragment()).commitAllowingStateLoss();
                                    }
                                });
                                profileService.profileRequest();
                            }
                        });
                        loginService.loginRequest(loginBinding.edtTxtLoginEmail.getText().toString().trim(),
                                loginBinding.edtTxtLoginPw.getText().toString().trim());
                        popupLogin.dismiss();
                    }
            }
        });
        loginBinding.txtBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myPreferences.isUserRegistered()){
                    Toast.makeText(activity,"Daha önceden zaten kayıt oldunuz",Toast.LENGTH_SHORT).show();
                }else
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,new RegisterFragment()).commit();
                    popupLogin.dismiss();

            }
        });
    }

}
