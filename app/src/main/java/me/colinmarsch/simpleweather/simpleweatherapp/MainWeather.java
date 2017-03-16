package me.colinmarsch.simpleweather.simpleweatherapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by colinmarsch on 2017-02-22.
 */

public class MainWeather extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    public TextView mTxtTemperature, mTxtDetails, mChange, mCityName;
    public ImageView mImgView;
    public GoogleApiClient mApiClient;
    public Location mCurrLocation;
    Weather helper = Weather.getInstance();
    private final static String API_ENDPOINT = "http://api.openweathermap.org/data/2.5/weather?units=metric";
    private final static String APIKEY = "0f9cfc3727985ab2180dc4cbe36b3446";
    private String city = "Waterloo,ON";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_weather, container, false);

        if(mApiClient == null) {
            mApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mTxtDetails = (TextView) view.findViewById(R.id.details);
        mImgView = (ImageView) view.findViewById(R.id.main_bg);
        mCityName = (TextView) view.findViewById(R.id.city_name);
        mTxtTemperature = (TextView) view.findViewById(R.id.temp);
        mChange = (TextView) view.findViewById(R.id.change_city);
        final Button button = (Button) view.findViewById(R.id.refresh);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadData(API_ENDPOINT + "&q=" + city + "&appid=" + APIKEY);
            }
        });
        final Button loc_button = (Button) view.findViewById(R.id.getLocation);
        loc_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
                    onConnected(new Bundle());
                } else {
                    loadData(API_ENDPOINT + "&lat=" + mCurrLocation.getLatitude() +
                            "&type=like" + "&lon=" + mCurrLocation.getLongitude() + "&appid=" + APIKEY);
                }
            }
        });
        mChange.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), SearchActivity.class);
                startActivityForResult(intent, 2);
            }
        });
        loadData(API_ENDPOINT + "&q=" + city + "&appid=" + APIKEY);

        return view;
    }

    public void sendData() {
        Intent dataIntent = new Intent();
        dataIntent.setAction("me.colinmarsch.simpleweather.simpleweatherapp.DATA_BROADCAST");
        dataIntent.putExtra("city", city);
        getActivity().sendBroadcast(dataIntent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (2) : {
                if (resultCode == Activity.RESULT_OK) {
                    Bundle extras = data.getExtras();
                    if(extras == null) {
                        System.out.println("this ran");
                        city = null;
                    } else {
                        city = (String) extras.get("city");
                        if(city.contains(" ")){
                            String[] parts = city.split(" ");
                            city = "";
                            for (String a : parts) {
                                city += a;
                            }
                        }
                    }
                    loadData(API_ENDPOINT + "&q=" + city + "&appid=" + APIKEY);
                    sendData();
                }
                break;
            }
        }
    }

    private void loadData(String url) {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String uri = "@drawable/a"
                                    + response.getJSONArray("weather").getJSONObject(0).getString("icon");
                            int imgRes = getResources().getIdentifier(uri, null, getContext().getPackageName());
                            Drawable res = getResources().getDrawable(imgRes, null);
                            mImgView.setImageDrawable(res);
                            city = response.getString("name") + ", " + response.getJSONObject("sys").getString("country");
                            mCityName.setText(city);
                            mTxtTemperature.setText(response.getJSONObject("main").getString("temp") + "°C");
                            mTxtDetails.setText(response.getJSONArray("weather").getJSONObject(0).getString("description"));
                            sendData();

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

    public void onStart() {
        mApiClient.connect();
        super.onStart();
    }

    public void onStop() {
        mApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mCurrLocation = LocationServices.FusedLocationApi.getLastLocation(mApiClient);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
