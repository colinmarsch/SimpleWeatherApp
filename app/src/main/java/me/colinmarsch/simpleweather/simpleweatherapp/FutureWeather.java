package me.colinmarsch.simpleweather.simpleweatherapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by colinmarsch on 2017-02-22.
 */

public class FutureWeather extends Fragment {

    private String[] highs;
    private String[] lows;
    private String[] dates;
    private String[] imgId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getData();
        //ForecastListAdapter adapter = new ForecastListAdapter(this, )
    }

    private void getData() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.future_weather, container, false);
    }
}
