package com.example.hop.weatherapp;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Created by Hop on 11/04/2015.
 */
public class WeatherFragment extends Fragment{
    TextView cityfield;
    TextView updatedfield;
    TextView weathericon;
    TextView detailsfield;
    TextView currenTemperatureField;
    Handler handler;
    Typeface weatherFont;
    public WeatherFragment(){
        handler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_weather, container, false);
        cityfield = (TextView)rootView.findViewById(R.id.city_field);
        updatedfield = (TextView)rootView.findViewById(R.id.updated_field);
        weathericon = (TextView)rootView.findViewById(R.id.weather_icon);
        detailsfield = (TextView)rootView.findViewById(R.id.details_field);
        currenTemperatureField = (TextView)rootView.findViewById(R.id.current_temperature_field);
        weathericon.setTypeface(weatherFont);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherFont = Typeface.createFromAsset(getActivity().getAssets(), "weather.ttf");
        updateWeather(new CityPreference(getActivity()).getCity());
    }

    public void updateWeather(final String city){
        new Thread(){
            @Override
            public void run() {
                final JSONObject json = RemoteFetch.getJSON(getActivity(), city);
                //Neu khong lay duoc du lieu
                if (json==null){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), getActivity().getString(R.string.place_not_found), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
                else{
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            renderWeather(json);
                        }
                    });
                }
            }
        }.start();
    }

    //Ham dua du lieu ra man hinh
    /*
    private void renderWeather(JSONObject json){
        try{
            cityfield.setText(json.getString("name").toUpperCase(Locale.US) + ", " + json.getJSONObject("sys").getString("country"));

        }
    }
    */
    private void renderWeather(JSONObject json){
        try {
            cityfield.setText(json.getString("name").toUpperCase(Locale.US) +
                    ", " +
                    json.getJSONObject("sys").getString("country"));

            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            JSONObject main = json.getJSONObject("main");
            detailsfield.setText("Chi tiết: " + details.getString("description") + "\n"
                                    + "Độ ẩm: " + main.getString("humidity") + "%\n"
                                    + "Áp suất: " + main.getString("pressure") + "hPa");
            currenTemperatureField.setText(String.format("%.2f ℃", main.getDouble("temp")));
            setWeatherIcon(details.getInt("id"),
                    json.getJSONObject("sys").getLong("sunrise") * 1000,
                    json.getJSONObject("sys").getLong("sunset") * 1000);
            DateFormat df = DateFormat.getDateInstance();
            String updatedOn  = df.format((new Date(json.getLong("dt") * 1000)));
            updatedfield.setText("Cập nhật lần cuối: " + updatedOn);




        }catch(Exception e){
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }
    }
    private void setWeatherIcon(int actualId, long sunrise, long sunset){
        int id = actualId / 100;
        String icon = "";
        if(actualId == 800){
            long currentTime = new Date().getTime();
            if(currentTime>=sunrise && currentTime<sunset) {
                icon = getActivity().getString(R.string.weather_sunny);
            } else {
                icon = getActivity().getString(R.string.weather_clear_night);
            }
        } else {
            switch(id) {
                case 2 : icon = getActivity().getString(R.string.weather_thunder);
                    break;
                case 3 : icon = getActivity().getString(R.string.weather_drizzle);
                    break;
                case 7 : icon = getActivity().getString(R.string.weather_foggy);
                    break;
                case 8 : icon = getActivity().getString(R.string.weather_cloudy);
                    break;
                case 6 : icon = getActivity().getString(R.string.weather_snowy);
                    break;
                case 5 : icon = getActivity().getString(R.string.weather_rainy);
                    break;
            }
        }

        weathericon.setText(icon);

    }
    public void changeCity(String city){
        updateWeather(city);
    }
}
