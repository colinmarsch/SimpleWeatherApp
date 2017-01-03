package me.colinmarsch.simpleweather.simpleweatherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;

public class SearchActivity extends AppCompatActivity {

    public AutoCompleteTextView mtextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mtextView = (AutoCompleteTextView) findViewById(R.id.autocomplete_city);
        //get the string array from the json file provided
    }
}
