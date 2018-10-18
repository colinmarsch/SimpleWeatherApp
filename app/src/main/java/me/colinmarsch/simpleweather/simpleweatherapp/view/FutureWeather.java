package me.colinmarsch.simpleweather.simpleweatherapp.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import me.colinmarsch.simpleweather.simpleweatherapp.R;
import me.colinmarsch.simpleweather.simpleweatherapp.adapter.ForecastListAdapter;
import me.colinmarsch.simpleweather.simpleweatherapp.interfaces.SuccessfulResponseInterface;
import me.colinmarsch.simpleweather.simpleweatherapp.service.OpenWeatherMapAPI;

import static me.colinmarsch.simpleweather.simpleweatherapp.util.Utils.APIKEY;

public class FutureWeather extends Fragment implements SuccessfulResponseInterface {

    private String[] highs = new String[7];
    private String[] lows = new String[7];
    private String[] dates = new String[7];
    private String[] days = new String[7];
    private String[] imgId = new String[7];
    private final static String API_ENDPOINT = "http://api.openweathermap.org/data/2.5/forecast/daily?units=metric&cnt=7";
    private OpenWeatherMapAPI api;
    private BroadcastReceiver receiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSuccess(JSONObject response) {
        try {
            for(int i = 0; i < 7; i++) {
                JSONObject currObj = response.getJSONArray("list").getJSONObject(i);
                dates[i] = new SimpleDateFormat("MMM dd", Locale.CANADA)
                        .format(new Date(Integer.parseInt(currObj.getString("dt")) * 1000L));
                days[i] = new SimpleDateFormat("EEEE", Locale.CANADA)
                        .format(new Date(Integer.parseInt(currObj.getString("dt")) * 1000L));
                imgId[i] = "@drawable/a"
                        + currObj.getJSONArray("weather").getJSONObject(0).getString("icon");
                highs[i] = currObj.getJSONObject("temp").getString("max");
                lows[i] = currObj.getJSONObject("temp").getString("min");
                ForecastListAdapter adapter = new ForecastListAdapter(getActivity(), imgId, highs, lows, dates, days);
                ListView list = (ListView) getView().findViewById(R.id.future_forecast);
                list.setAdapter(adapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.future_weather, container, false);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String city = intent.getStringExtra("city");
                String url = API_ENDPOINT + "&q=" + city + "&appid=" + APIKEY;
                System.out.println(city);
                api.getWeather(url, FutureWeather.this);
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction("me.colinmarsch.simpleweather.simpleweatherapp.UPDATE_CITY");
        getActivity().registerReceiver(receiver, filter);

        api = new OpenWeatherMapAPI(getActivity().getApplicationContext());

        return view;
    }
}
