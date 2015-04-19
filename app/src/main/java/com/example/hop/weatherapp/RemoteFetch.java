package com.example.hop.weatherapp;

/**
 * Created by Hop on 10/04/2015.
 */
import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RemoteFetch {
    static final String OPEN_WEATHER_MAP_API = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric";
    public static JSONObject getJSON(Context context, String city){
        try{
            URL url = new URL(String.format(OPEN_WEATHER_MAP_API, city));
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.addRequestProperty("x-api-key", context.getString(R.string.open_weather_maps_app_id));
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer json = new StringBuffer(1024);
            String temp = "";
            while ((temp=reader.readLine())!=null)
                json.append(temp).append("\n");
            reader.close();
            JSONObject data = new JSONObject(json.toString());
            /*Gia tri tra ve khi ko get duoc du lieu tu server. Loi 404*/
            if (data.getInt("cod")!=200){
                return null;
            }
            return data;
        }
        catch (MalformedURLException ex){
            return null;
        }
        catch (IOException ex){
            return null;
        }
        catch (JSONException ex){
            return null;
        }
    }
}
