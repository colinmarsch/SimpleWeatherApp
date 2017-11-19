package me.colinmarsch.simpleweather.simpleweatherapp;

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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

import me.colinmarsch.simpleweather.simpleweatherapp.adapter.ForecastListAdapter;

/**
 * Created by colinmarsch on 2017-02-22.
 */

public class FutureWeather extends Fragment {

    private String[] highs;
    private String[] lows;
    private String[] dates;
    private String[] days;
    private String[] imgId;
    private final static String API_ENDPOINT = "http://api.openweathermap.org/data/2.5/forecast/daily?units=metric&cnt=7";
    private final static String APIKEY = "0f9cfc3727985ab2180dc4cbe36b3446";
    Weather helper;
    private String city;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void getData(String url) {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
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

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        helper.add(jsObjRequest);
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            city = intent.getStringExtra("city");
            String url = API_ENDPOINT + "&q=" + city + "&appid=" + APIKEY;
            System.out.println(city);
            getData(url);
        }
    };

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.future_weather, container, false);

        helper = Weather.getInstance(getActivity().getApplicationContext());

        dates = new String[7];
        lows = new String[7];
        highs = new String[7];
        days = new String[7];
        imgId = new String[7];

        IntentFilter filter = new IntentFilter();
        filter.addAction("me.colinmarsch.simpleweather.simpleweatherapp.DATA_BROADCAST");
        getActivity().registerReceiver(receiver, filter);



        return view;
    }
}
