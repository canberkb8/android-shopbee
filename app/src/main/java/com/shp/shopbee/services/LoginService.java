package com.shp.shopbee.services;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.shp.shopbee.MainActivity;

public class LoginService {

    private MainActivity activity;


    private final LoginSuccessListener listenerSuccess;

    public LoginService(MainActivity activity,LoginSuccessListener listenerSuccess) {
        this.activity = activity;
        this.listenerSuccess=listenerSuccess;

    }

    public void loginRequest(final String email, final String password){

        MainActivity.mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(activity,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            MainActivity.myPreferences.setLoggedIn(true);
                            MainActivity.myPreferences.setToken(MainActivity.mAuth.getUid());
                            listenerSuccess.onSuccessLoginListener();


                        }else{
                            Toast.makeText(activity, "Yanlış giriş", Toast.LENGTH_SHORT).show();
                        }
                    }


                });

    }

    public interface LoginSuccessListener {
        void onSuccessLoginListener();
    }
}
