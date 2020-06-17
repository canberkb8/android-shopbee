package com.shp.shopbee.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.shp.shopbee.MainActivity;

public class Preferences {


    //Ekranın sağ alt köşesindeki Device File Explorerden > data > data > (app ismini arat) > app isminin içinde shared_prefs
    private SharedPreferences sharedPreferences;    //SharedPreferences referansı, local'de data tutmak için kullanılır

    public Preferences(Context context){    //Dışardan tanımlayacağın constructor
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }
    public void setUserRegistered(Boolean bool){
        sharedPreferences.edit().putBoolean("register_in",bool).apply();
    }
    public boolean isUserRegistered() {

        return sharedPreferences.getBoolean("register_in",false);
    }
    public void setLoggedIn(Boolean bool){
        sharedPreferences.edit().putBoolean("logged_in",bool).apply();
    }
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean("logged_in",false);
    }

    public void setToken(String token){
        sharedPreferences.edit().putString("token", token).apply();
    }
    public String getToken()
    {
        return sharedPreferences.getString("token","");
    }

    public void setUserName(String name){
        sharedPreferences.edit().putString("name", name).apply();
    }
    public String getUserName()
    {
        return sharedPreferences.getString("name","");
    }

    public void deletePreferencesValues() {
        sharedPreferences.edit().clear().apply();

    }
}
