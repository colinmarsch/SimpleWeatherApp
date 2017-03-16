package me.colinmarsch.simpleweather.simpleweatherapp;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by colinmarsch on 2017-03-11.
 */

public class ForecastListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] highs;
    private final String[] lows;
    private final String[] dates;
    private final String[] imgId;

    public ForecastListAdapter(Activity context, String[] imgId, String[] highs, String[] lows, String[] dates) {
        super(context, R.layout.forecast_list, dates);

        this.context = context;
        this.highs = highs;
        this.lows = lows;
        this.dates = dates;
        this.imgId = imgId;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.forecast_list, null, true);

        ImageView img = (ImageView) rowView.findViewById(R.id.weather_icon);
        TextView day_of_week = (TextView) rowView.findViewById(R.id.day_of_week);
        TextView date = (TextView) rowView.findViewById(R.id.date);
        TextView high_low = (TextView) rowView.findViewById(R.id.high_low);

        int imgRes = context.getResources().getIdentifier(imgId[position], null, getContext().getPackageName());
        Drawable res = context.getResources().getDrawable(imgRes, null);
        img.setImageDrawable(res);

        day_of_week.setText("Monday");
        date.setText(dates[position]);
        high_low.setText("HI: " + highs[position] + "°C, LO: " + lows[position] + "°C");

        return rowView;
    }
}
