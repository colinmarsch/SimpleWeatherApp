package me.colinmarsch.simpleweather.simpleweatherapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public TextView mTxtTemperature, mTxtDetails, mChange, mCityName;
    public ImageView mImgView;
    public GoogleApiClient mApiClient;
    public Location mCurrLocation;
    Weather helper = Weather.getInstance();
    private final static String API_ENDPOINT = "http://api.openweathermap.org/data/2.5/weather?units=metric";
    private final static String APIKEY = "API_KEY";
    private String city = "Waterloo,ON";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(mApiClient == null) {
            mApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mTxtDetails = (TextView) findViewById(R.id.details);
        mImgView = (ImageView) findViewById(R.id.main_bg);
        mCityName = (TextView) findViewById(R.id.city_name);
        mTxtTemperature = (TextView) findViewById(R.id.temp);
        mChange = (TextView) findViewById(R.id.change_city);
        final Button button = (Button) findViewById(R.id.refresh);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadData(API_ENDPOINT + "&q=" + city + "&appid=" + APIKEY);
            }
        });
        final Button loc_button = (Button) findViewById(R.id.getLocation);
        loc_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
                    onConnected(new Bundle());
                } else {
                    loadData(API_ENDPOINT + "&lat=" + mCurrLocation.getLatitude() +
                            "&lon=" + mCurrLocation.getLongitude() + "&appid=" + APIKEY);
                }
            }
        });
        mChange.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivityForResult(intent, 2);
            }
        });
        loadData(API_ENDPOINT + "&q=" + city + "&appid=" + APIKEY);
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
                            int imgRes = getResources().getIdentifier(uri, null, getPackageName());
                            Drawable res = getResources().getDrawable(imgRes, null);
                            mImgView.setImageDrawable(res);
                            city = response.getString("name") + ", " + response.getJSONObject("sys").getString("country");
                            mCityName.setText(city);
                            mTxtTemperature.setText(response.getJSONObject("main").getString("temp") + "°C");
                            mTxtDetails.setText(response.getJSONArray("weather").getJSONObject(0).getString("description"));

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

    protected void onStart() {
        mApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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
