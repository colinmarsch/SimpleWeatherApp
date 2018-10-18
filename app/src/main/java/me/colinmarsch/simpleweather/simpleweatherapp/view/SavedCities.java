package me.colinmarsch.simpleweather.simpleweatherapp.view;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.colinmarsch.simpleweather.simpleweatherapp.service.OpenWeatherMapAPI;
import me.colinmarsch.simpleweather.simpleweatherapp.R;
import me.colinmarsch.simpleweather.simpleweatherapp.adapter.CityListAdapter;
import me.colinmarsch.simpleweather.simpleweatherapp.data.SavedCitiesContract.CitiesEntry;
import me.colinmarsch.simpleweather.simpleweatherapp.data.SavedCitiesHelper;
import me.colinmarsch.simpleweather.simpleweatherapp.interfaces.SuccessfulResponseIndexInterface;

import static me.colinmarsch.simpleweather.simpleweatherapp.util.Utils.ADD_CITY_REQUEST_CODE;
import static me.colinmarsch.simpleweather.simpleweatherapp.util.Utils.APIKEY;
import static me.colinmarsch.simpleweather.simpleweatherapp.util.Utils.API_ENDPOINT;

public class SavedCities extends Fragment implements SuccessfulResponseIndexInterface {

    private String[] cities;
    private List<String> citiesList;
    private ContentValues values;
    private SQLiteDatabase db;
    private ListView list;
    private String[] imgID;
    private String[] temps;
    private View view;
    private OpenWeatherMapAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.cities_layout, container, false);
        api = new OpenWeatherMapAPI(getActivity().getApplicationContext());
        citiesList = new ArrayList<>();
        cities = citiesList.toArray(new String[0]);
        Button add_city = (Button) view.findViewById(R.id.add_city);
        add_city.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), SearchActivity.class);
                startActivityForResult(intent, ADD_CITY_REQUEST_CODE);
            }
        });
        Button refresh_list = (Button) view.findViewById(R.id.refresh_list);
        refresh_list.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadList(view);
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
                dataIntent.setAction("me.colinmarsch.simpleweather.simpleweatherapp.SELECTED_SAVED_CITY");
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
            String url = API_ENDPOINT + "&q=" + city + "&appid=" + APIKEY;
            api.getWeather(url, i, this);
        }
        cities = citiesList.toArray(new String[0]);
        list = (ListView) view.findViewById(R.id.cities_listView);
        registerForContextMenu(list);
        c.close();
    }

    @Override
    public void onSuccess(JSONObject response, int i) {
        try {
            String uri = "@drawable/a"
                    + response.getJSONArray("weather").getJSONObject(0).getString("icon");
            imgID[i] = uri;
            String temp = response.getJSONObject("main").getString("temp") + "°C";
            temps[i] = temp;
            CityListAdapter adapter = new CityListAdapter(getActivity(), cities, imgID, temps);
            list.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.cities_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String city = (String) list.getItemAtPosition(info.position);
        if(item.getItemId() == R.id.removeMenuButton) {
            return removeCity(city);
        }
        return super.onContextItemSelected(item);
    }

    public boolean removeCity(String city) {
        db.delete(CitiesEntry.TABLE_NAME, CitiesEntry.COLUMN_NAME_CITY + "=?", new String[]{city});
        loadList(getView());
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_CITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    String city = (String) extras.get("city");
                    values = new ContentValues();
                    values.put(CitiesEntry.COLUMN_NAME_CITY, city);
                    db.insert(CitiesEntry.TABLE_NAME, null, values);
                }
                loadList(getView());
            }
        }
    }
}
