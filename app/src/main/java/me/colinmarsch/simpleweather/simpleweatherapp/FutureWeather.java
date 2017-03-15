package me.colinmarsch.simpleweather.simpleweatherapp;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Created by colinmarsch on 2017-02-22.
 */

public class FutureWeather extends Fragment {

    private String[] highs;
    private String[] lows;
    private String[] dates;
    private String[] imgId;
    private final static String API_ENDPOINT = "http://api.openweathermap.org/data/2.5/forecast/daily?units=metric&cnt=7";
    private final static String APIKEY = "YOUR_API_KEY";
    Weather helper = Weather.getInstance();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //String url = API_ENDPOINT + "&" + city + "&" + APIKEY;
        //getData(url);
        //ForecastListAdapter adapter = new ForecastListAdapter(this, )
    }

    private void getData(String url) {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            for(int i = 0; i < 7; i++) {
                                JSONObject currObj = response.getJSONArray("list").getJSONObject(i);
                                dates[i] = new SimpleDateFormat("MM/dd")
                                        .format(new Date(Integer.parseInt(currObj.getString("dt")) * 1000L));
                                imgId[i] = "@drawable/a"
                                        + currObj.getJSONArray("weather").getJSONObject(0).getString("icon");
                                highs[i] = currObj.getJSONObject("temp").getString("max");
                                lows[i] = currObj.getJSONObject("temp").getString("min");
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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.future_weather, container, false);
    }
}
