package me.colinmarsch.simpleweather.simpleweatherapp.service;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import me.colinmarsch.simpleweather.simpleweatherapp.util.Utils;
import me.colinmarsch.simpleweather.simpleweatherapp.data.Weather;
import me.colinmarsch.simpleweather.simpleweatherapp.interfaces.SuccessfulResponseIndexInterface;
import me.colinmarsch.simpleweather.simpleweatherapp.interfaces.SuccessfulResponseInterface;

public class OpenWeatherMapAPI {
    Weather helper;

    public OpenWeatherMapAPI(Context context) {
        helper = Weather.getInstance(context);
    }

    public void getWeather(String url, final SuccessfulResponseInterface weatherInterface) {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    weatherInterface.onSuccess(response);
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(Utils.DEBUG_TAG, "Error making json request: " + error.getMessage());
                }
            });
        helper.add(jsObjRequest);
    }

    public void getWeather(String url, final int i, final SuccessfulResponseIndexInterface weatherInterface) {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        weatherInterface.onSuccess(response, i);
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(Utils.DEBUG_TAG, "Error making json request: " + error.getMessage());
                    }
                });
        helper.add(jsObjRequest);
    }
}
