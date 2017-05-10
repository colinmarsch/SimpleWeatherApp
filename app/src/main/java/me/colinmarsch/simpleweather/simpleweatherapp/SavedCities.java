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

import me.colinmarsch.simpleweather.simpleweatherapp.data.SavedCitiesContract.CitiesEntry;
import me.colinmarsch.simpleweather.simpleweatherapp.data.SavedCitiesHelper;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cities_layout, container, false);
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
        cities = citiesList.toArray(new String[citiesList.size()]);
        CityListAdapter adapter= new CityListAdapter(getActivity(), cities);
        list = (ListView) view.findViewById(R.id.cities_listView);
        list.setAdapter(adapter);
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
