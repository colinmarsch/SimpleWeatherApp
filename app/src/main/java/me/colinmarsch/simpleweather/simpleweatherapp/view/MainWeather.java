package me.colinmarsch.simpleweather.simpleweatherapp.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import me.colinmarsch.simpleweather.simpleweatherapp.service.LocationService;
import me.colinmarsch.simpleweather.simpleweatherapp.service.OpenWeatherMapAPI;
import me.colinmarsch.simpleweather.simpleweatherapp.R;
import me.colinmarsch.simpleweather.simpleweatherapp.interfaces.SuccessfulResponseInterface;

import static me.colinmarsch.simpleweather.simpleweatherapp.util.Utils.APIKEY;
import static me.colinmarsch.simpleweather.simpleweatherapp.util.Utils.API_ENDPOINT;
import static me.colinmarsch.simpleweather.simpleweatherapp.util.Utils.CHANGE_CITY_REQUEST_CODE;

public class MainWeather extends Fragment implements SuccessfulResponseInterface {

    public TextView mTxtTemperature, mTxtDetails, mChange, mCityName;
    public ImageView mImgView;
    private LocationService locationService;
    private OpenWeatherMapAPI api;
    private String city = "Waterloo";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_weather, container, false);

        api = new OpenWeatherMapAPI(getActivity().getApplicationContext());
        locationService = new LocationService(getActivity());
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                city = intent.getStringExtra("city");
                String url = API_ENDPOINT + "&q=" + city + "&appid=" + APIKEY;
                System.out.println(city);
                api.getWeather(url, MainWeather.this);
            }
        };

        mTxtDetails = (TextView) view.findViewById(R.id.details);
        mImgView = (ImageView) view.findViewById(R.id.main_bg);
        mCityName = (TextView) view.findViewById(R.id.city_name);
        mTxtTemperature = (TextView) view.findViewById(R.id.temp);
        mChange = (TextView) view.findViewById(R.id.change_city);
        final Button button = (Button) view.findViewById(R.id.refresh);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                api.getWeather(API_ENDPOINT + "&q=" + city + "&appid=" + APIKEY, MainWeather.this);
            }
        });
        final Button loc_button = (Button) view.findViewById(R.id.getLocation);
        loc_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
                    locationService.onConnected(new Bundle());
                } else {
                    if(locationService.getmCurrLocation() != null) {
                        api.getWeather(API_ENDPOINT + "&lat=" + locationService.getmCurrLocation().getLatitude() +
                                "&type=like" + "&lon=" + locationService.getmCurrLocation().getLongitude() + "&appid=" + APIKEY, MainWeather.this);
                    }
                }
            }
        });
        mChange.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), SearchActivity.class);
                startActivityForResult(intent, CHANGE_CITY_REQUEST_CODE);
            }
        });
        api.getWeather(API_ENDPOINT + "&q=" + city + "&appid=" + APIKEY, this);

        IntentFilter filter = new IntentFilter();
        filter.addAction("me.colinmarsch.simpleweather.simpleweatherapp.SELECTED_SAVED_CITY");
        getActivity().registerReceiver(receiver, filter);

        return view;
    }

    public void sendData() {
        Intent dataIntent = new Intent();
        dataIntent.setAction("me.colinmarsch.simpleweather.simpleweatherapp.UPDATE_CITY");
        dataIntent.putExtra("city", city);
        getActivity().sendBroadcast(dataIntent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CHANGE_CITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle extras = data.getExtras();
                if(extras == null) {
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
                api.getWeather(API_ENDPOINT + "&q=" + city + "&appid=" + APIKEY, this);
            }
        }
    }

    public void onSuccess(JSONObject response) {
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

    public void onStart() {
        locationService.getmApiClient().connect();
        super.onStart();
    }

    public void onStop() {
        locationService.getmApiClient().disconnect();
        super.onStop();
    }
}
