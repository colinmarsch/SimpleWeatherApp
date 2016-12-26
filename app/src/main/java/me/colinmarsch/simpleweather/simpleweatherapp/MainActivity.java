package me.colinmarsch.simpleweather.simpleweatherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
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

    public TextView mTxtTemperature, mTxtDetails;
    Weather helper = Weather.getInstance();
    private final static String API_ENDPOINT = "http://api.openweathermap.org/data/2.5/weather?units=metric";
    private final static String APIKEY = "YOUR_API_KEY";
    JSONObject obj;
    protected void createObj() {
        obj = new JSONObject();
        try {
            obj.put("q", "Waterloo,ON");
            obj.put("appid", APIKEY);
            obj.put("units", "metric");
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createObj();
        mTxtDetails = (TextView) findViewById(R.id.details);
        mTxtTemperature = (TextView) findViewById(R.id.temp);
        loadData();
    }
    private void loadData() {
        String url = API_ENDPOINT + "&q=" + "Waterloo,ON" + "&appid=" + APIKEY;
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
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
