package me.colinmarsch.simpleweather.simpleweatherapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by colinmarsch on 2017-05-01.
 */

public class CityListAdapter extends ArrayAdapter<String> {

    private String[] cities;
    private Activity context;

    public CityListAdapter(Activity context, String[] cities) {
        super(context, R.layout.cities_list, cities);
        this.cities = cities;
        this.context = context;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.cities_list, null, true);

        TextView city_name = (TextView) rowView.findViewById(R.id.city_name);
        city_name.setText(cities[position]);
        return rowView;
    }
}
