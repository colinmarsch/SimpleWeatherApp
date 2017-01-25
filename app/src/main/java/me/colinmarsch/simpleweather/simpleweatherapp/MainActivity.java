package me.colinmarsch.simpleweather.simpleweatherapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.R.attr.data;

public class MainActivity extends AppCompatActivity {

    public TextView mTxtTemperature, mTxtDetails, mChange, mCityName;
    public ImageView mImgView;
    Weather helper = Weather.getInstance();
    private final static String API_ENDPOINT = "http://api.openweathermap.org/data/2.5/weather?units=metric";
    private final static String APIKEY = "0f9cfc3727985ab2180dc4cbe36b3446";
    private String city = "Waterloo,ON";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTxtDetails = (TextView) findViewById(R.id.details);
        mImgView = (ImageView) findViewById(R.id.main_bg);
        mCityName = (TextView) findViewById(R.id.city_name);
        mTxtTemperature = (TextView) findViewById(R.id.temp);
        mChange = (TextView) findViewById(R.id.change_city);
        final Button button = (Button) findViewById(R.id.refresh);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadData();
            }
        });
        mChange.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivityForResult(intent, 2);
            }
        });
        loadData();
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
                    }
                    mCityName.setText(city);
                    System.out.println(city);
                }
                break;
            }
        }
    }


    private void loadData() {
        String url = API_ENDPOINT + "&q=" + city + "&appid=" + APIKEY;
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String uri = "@drawable/a" + response.getJSONArray("weather").getJSONObject(0).getString("icon");
                            int imgRes = getResources().getIdentifier(uri, null, getPackageName());
                            Drawable res = getResources().getDrawable(imgRes, null);
                            mImgView.setImageDrawable(res);
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
}
