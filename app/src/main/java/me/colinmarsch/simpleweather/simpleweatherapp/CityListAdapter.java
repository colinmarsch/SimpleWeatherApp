package me.colinmarsch.simpleweather.simpleweatherapp;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by colinmarsch on 2017-05-01.
 */

public class CityListAdapter extends ArrayAdapter<String> {

    private String[] cities;
    private String[] imgID;
    private String[] temps;
    private Activity context;

    public CityListAdapter(Activity context, String[] cities, String[] imgID, String[] temps) {
        super(context, R.layout.cities_list, cities);
        this.cities = cities;
        this.imgID = imgID;
        this.temps = temps;
        this.context = context;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.cities_list, null, true);

        TextView city_name = (TextView) rowView.findViewById(R.id.city_name);
        city_name.setText(cities[position]);

        if(imgID[position] != null){
            int imgRes = context.getResources().getIdentifier(imgID[position], null, getContext().getPackageName());
            Drawable res = context.getResources().getDrawable(imgRes, null);
            ImageView mIcon = (ImageView) rowView.findViewById(R.id.current_city_icon);
            mIcon.setImageDrawable(res);
        }

        TextView currTemp = (TextView) rowView.findViewById(R.id.temperature);
        if(temps[position] != null) {
            currTemp.setText(temps[position]);
        } else {
            currTemp.setText("...");
        }

        return rowView;
    }
}
