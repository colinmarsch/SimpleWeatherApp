package me.colinmarsch.simpleweather.simpleweatherapp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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
        return view;
    }

    private void loadList(View view) {
        String[] projection = {CitiesEntry.COLUMN_NAME_CITY};
        Cursor c = db.query(CitiesEntry.TABLE_NAME, projection, null, null, null, null, null);
        CityListAdapter adapter= new CityListAdapter(getActivity(), cities);
        ListView list = (ListView) view.findViewById(R.id.cities_listView);
        list.setAdapter(adapter);
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
                        citiesList.add(city);
                        values = new ContentValues();
                        values.put(CitiesEntry.COLUMN_NAME_CITY, city);
                        db.insert(CitiesEntry.TABLE_NAME, null, values);
                    }
                    cities = citiesList.toArray(new String[citiesList.size()]);
                    loadList(getView());
                }
                break;
            }
        }
    }
}
