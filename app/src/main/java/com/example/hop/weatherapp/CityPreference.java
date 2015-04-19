package com.example.hop.weatherapp;

/**
 * Created by Hop on 11/04/2015.
 */
import android.app.Activity;
import android.content.SharedPreferences;

public class CityPreference {
    SharedPreferences pref;
    public CityPreference(Activity activity){
        pref = activity.getPreferences(Activity.MODE_PRIVATE);

    }
    /*Neu nguoi dung khong chon thanh pho
    Mac dinh la Da Nang, VN
     */
    public String getCity(){
       return pref.getString("city", "Da Nang, VN");
    }
    public void setCity(String city){
        pref.edit().putString("city", city).commit();
    }
}
