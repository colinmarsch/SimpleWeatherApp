package me.colinmarsch.simpleweather.simpleweatherapp;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    public AutoCompleteTextView mtextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mtextView = (AutoCompleteTextView) findViewById(R.id.autocomplete_city);
        //get the string array from the json file provided
        AssetManager am = getApplicationContext().getAssets();
        try {
            //using the json and finding all the cities listed in it
            ArrayList<org.json.simple.JSONObject> jsons = ReadJSON(am);
            ArrayList<String> cities = new ArrayList<>();
            for(org.json.simple.JSONObject obj : jsons) {
                cities.add(obj.get("name") + "," + obj.get("country"));
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cities);
            mtextView.setAdapter(adapter);
        } catch (org.json.simple.parser.ParseException | IOException e) {
            e.printStackTrace();
        }
        mtextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if(i == EditorInfo.IME_ACTION_SEND) {
                    Intent in = new Intent();
                    in.putExtra("city", textView.getText());
                    setResult(Activity.RESULT_OK, in);
                    finish();
                    handled = true;
                }
                return handled;
            }
        });
    }

    public static synchronized ArrayList<org.json.simple.JSONObject> ReadJSON(AssetManager am)
            throws org.json.simple.parser.ParseException, IOException {
        InputStream is = am.open("city.list.json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        ArrayList<org.json.simple.JSONObject> json = new ArrayList<>();
        //parsing strings to json
        while((line = reader.readLine()) != null) {
            org.json.simple.JSONObject obj = (org.json.simple.JSONObject) new JSONParser().parse(line);
            json.add(obj);
        }
        return json;
    }
}
