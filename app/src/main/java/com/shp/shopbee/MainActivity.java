package com.shp.shopbee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.shp.shopbee.databinding.ActivityMainBinding;

import com.shp.shopbee.fragments.FollowPageFragment;
import com.shp.shopbee.fragments.HomePageFragment;
import com.shp.shopbee.fragments.MessagePageFragment;
import com.shp.shopbee.fragments.ProfileFragment;
import com.shp.shopbee.local.Preferences;
import com.shp.shopbee.services.ProfileService;
import com.shp.shopbee.utils.AlertSignIn;


public class MainActivity extends AppCompatActivity {

    private String TAG="MainActivitiy";
    public ActivityMainBinding mainBinding;

    public static Preferences myPreferences;
    public static FirebaseDatabase mDatabase;
    public static FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,new HomePageFragment()).commitAllowingStateLoss();
        myPreferences = new Preferences(this);
        mDatabase= FirebaseDatabase.getInstance();
        mAuth=FirebaseAuth.getInstance();


        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);


        mainBinding.mainNavBar.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

       // mainBinding.mainNavBar.setVisibility(View.INVISIBLE);


        /*
        try {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,new SplashFragment()).commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
        */
        ProfileService profileService=new ProfileService(this, new ProfileService.ProfileSuccessListener() {
            @Override
            public void onProfileSuccessListener() {
                MainActivity.this.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_frame_layout, new HomePageFragment())
                        .addToBackStack(null)   //addToBackStack(null) en son açılan fragmenta geri döner
                        .commitAllowingStateLoss();
            }
        });
        if (MainActivity.myPreferences.isLoggedIn()){
            profileService.profileRequest();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_homepage:
                    MainActivity.this.getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_right_to_left,R.anim.exit_right_to_left,R.anim.enter_left_to_right,R.anim.exit_left_to_right)
                            .replace(R.id.main_frame_layout, new HomePageFragment())
                            .addToBackStack(null)   //addToBackStack(null) en son açılan fragmenta geri döner
                            .commitAllowingStateLoss();
                    Log.i(TAG,"inHomepage");
                    return true;

                case R.id.nav_follows:
                    if(MainActivity.myPreferences.isLoggedIn()) {
                         MainActivity.this.getSupportFragmentManager()
                                .beginTransaction()
                                .setCustomAnimations(R.anim.enter_right_to_left,R.anim.exit_right_to_left,R.anim.enter_left_to_right,R.anim.exit_left_to_right)
                                .replace(R.id.main_frame_layout, new FollowPageFragment())
                                .addToBackStack(null)
                                .commitAllowingStateLoss();
                        Log.i(TAG, "FollowFragment");
                    }else{
                        new AlertSignIn(MainActivity.this);
                    }
                    Log.i(TAG, "onNavigationItemSelected: not Loggin in follow");
                    return true;

                case R.id.nav_message:
                    if(MainActivity.myPreferences.isLoggedIn()) {
                        MainActivity.this.getSupportFragmentManager()
                                .beginTransaction()
                                .setCustomAnimations(R.anim.enter_right_to_left,R.anim.exit_right_to_left,R.anim.enter_left_to_right,R.anim.exit_left_to_right)
                                .replace(R.id.main_frame_layout, new MessagePageFragment())
                                .addToBackStack(null)
                                .commitAllowingStateLoss();
                        Log.i(TAG, "MessageFragment");
                    }else{
                        new AlertSignIn(MainActivity.this);
                    }
                    Log.i(TAG, "onNavigationItemSelected: not Loggin in Message");
                    return true;

                case R.id.nav_profile:
                    if(MainActivity.myPreferences.isLoggedIn()) {
                        MainActivity.this.getSupportFragmentManager()
                                .beginTransaction()
                                .setCustomAnimations(R.anim.enter_right_to_left,R.anim.exit_right_to_left,R.anim.enter_left_to_right,R.anim.exit_left_to_right)
                                .replace(R.id.main_frame_layout, new ProfileFragment())
                                .addToBackStack(null)
                                .commitAllowingStateLoss();
                        Log.i(TAG, "ProfileFragment");
                    }else{
                        new AlertSignIn(MainActivity.this);
                    }
                    Log.i(TAG, "onNavigationItemSelected: not Loggin in profile");
                    return true;
            }
            return false;
        }
    };

}
