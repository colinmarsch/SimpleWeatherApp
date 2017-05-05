package me.colinmarsch.simpleweather.simpleweatherapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;



public class SearchActivity extends AppCompatActivity {

    public EditText mtextView;
    public Button mSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mtextView = (EditText) findViewById(R.id.autocomplete_city);
        mSearch = (Button) findViewById(R.id.search_btn);
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent();
                in.putExtra("city", mtextView.getText().toString().trim());
                setResult(Activity.RESULT_OK, in);
                finish();
            }
        });

        mtextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if(i == EditorInfo.IME_ACTION_DONE) {
                    Intent in = new Intent();
                    in.putExtra("city", textView.getText().toString().trim());
                    setResult(Activity.RESULT_OK, in);
                    finish();
                    handled = true;
                }
                return handled;
            }
        });
    }


}
