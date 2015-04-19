package com.example.hop.weatherapp;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

/**
 * Created by Hop on 26/03/2015.
 */

public class About extends Activity  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.about:
                //Chen xu ly o day
                break;
        }
        return true;
    }
}
