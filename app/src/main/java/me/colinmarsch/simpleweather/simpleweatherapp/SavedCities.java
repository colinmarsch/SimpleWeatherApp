package me.colinmarsch.simpleweather.simpleweatherapp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import me.colinmarsch.simpleweather.simpleweatherapp.data.SavedCitiesContract.CitiesEntry;
import me.colinmarsch.simpleweather.simpleweatherapp.data.SavedCitiesHelper;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;
import static me.colinmarsch.simpleweather.simpleweatherapp.R.string.error;

/**
 * Created by colinmarsch on 2017-05-01.
 */

public class SavedCities extends Fragment {

    private Button add_city;
    private String[] cities;
    private List<String> citiesList;
    private ContentValues values;
    private SQLiteDatabase db;
    private ListView list;
    private String[] imgID;
    private String[] temps;
    private View view;
    private final static String API_ENDPOINT = "http://api.openweathermap.org/data/2.5/weather?units=metric";
    private final static String APIKEY = "0f9cfc3727985ab2180dc4cbe36b3446";
    Weather helper = Weather.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.cities_layout, container, false);
        citiesList = new ArrayList<String>();
        cities = citiesList.toArray(new String[citiesList.size()]);
        add_city = (Button) view.findViewById(R.id.add_city);
        add_city.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), AddCity.class);
                startActivityForResult(intent, 4);
            }
        });
        SavedCitiesHelper mDbHelper = new SavedCitiesHelper(getActivity());
        db = mDbHelper.getWritableDatabase();
        values = new ContentValues();
        loadList(view);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView city_selected = (TextView) list.getAdapter().getView(position, null, list).findViewById(R.id.city_name);
                String city = city_selected.getText().toString();
                Intent dataIntent = new Intent();
                dataIntent.setAction("me.colinmarsch.simpleweather.simpleweatherapp.DATA_BROADCAST2");
                dataIntent.putExtra("city", city);
                getActivity().sendBroadcast(dataIntent);
                Snackbar.make(view, "Set the city to " + city, Snackbar.LENGTH_LONG).show();
            }
        });
        return view;
    }

    private void loadList(View view) {
        String[] projection = new String[]{CitiesEntry.COLUMN_NAME_CITY};
        Cursor c = db.query(CitiesEntry.TABLE_NAME, projection, null, null, null, null, null);
        int columnIndex = c.getColumnIndex(CitiesEntry.COLUMN_NAME_CITY);
        citiesList.clear();
        while(c.moveToNext()) {
            citiesList.add(c.getString(columnIndex));
        }
        imgID = new String[citiesList.size()];
        temps = new String[citiesList.size()];
        for(String city : citiesList) {
            final int i = citiesList.indexOf(city);
            System.out.println("this ran here");
            String url = API_ENDPOINT + "&q=" + city + "&appid=" + APIKEY;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String uri = "@drawable/a"
                                        + response.getJSONArray("weather").getJSONObject(0).getString("icon");
                                imgID[i] = uri;
                                String temp = response.getJSONObject("main").getString("temp") + "°C";
                                temps[i] = temp;
                                CityListAdapter adapter= new CityListAdapter(getActivity(), cities, imgID, temps);
                                list.setAdapter(adapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
            helper.add(jsonObjectRequest);
        }
        cities = citiesList.toArray(new String[citiesList.size()]);
        list = (ListView) view.findViewById(R.id.cities_listView);
        c.close();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (4): {
                if (resultCode == Activity.RESULT_OK) {
                    Bundle extras = data.getExtras();
                    if (extras == null) {
                        System.out.println("this ran");
                    } else {
                        String city = (String) extras.get("city");
                        values = new ContentValues();
                        values.put(CitiesEntry.COLUMN_NAME_CITY, city);
                        db.insert(CitiesEntry.TABLE_NAME, null, values);
                    }
                    loadList(getView());
                }
                break;
            }
        }
    }
}
