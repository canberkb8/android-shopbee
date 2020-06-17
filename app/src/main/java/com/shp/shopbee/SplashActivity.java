package com.shp.shopbee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.shp.shopbee.databinding.ActivitySplashBinding;
import com.shp.shopbee.fragments.HomePageFragment;

public class SplashActivity extends AppCompatActivity {

    private static int splashTimeOut=3000;

    private ActivitySplashBinding splashBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);

        animation();    //animasyonlarının oynatıldığı metod
    }

    void animation(){
        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                    //this.getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, new HomePageFragment()).commitAllowingStateLoss();
                    //activity.mainBinding.mainNavBar.setVisibility(View.VISIBLE);
                }
            },splashTimeOut);

            Animation animAlpha = AnimationUtils.loadAnimation(this,R.anim.alpha);
            Animation animTranslate = AnimationUtils.loadAnimation(this,R.anim.translate);
            splashBinding.imgSplash.startAnimation(animAlpha);
            splashBinding.txtSplash.startAnimation(animTranslate);

        } catch (Exception e) {
            e.printStackTrace();
        }

        Animation animAlpha = AnimationUtils.loadAnimation(this,R.anim.alpha);
        Animation animTranslate = AnimationUtils.loadAnimation(this,R.anim.translate);
        splashBinding.imgSplash.startAnimation(animAlpha);
        splashBinding.txtSplash.startAnimation(animTranslate);
    }
}
