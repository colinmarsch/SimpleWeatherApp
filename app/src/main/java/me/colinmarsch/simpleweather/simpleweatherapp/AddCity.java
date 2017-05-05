package me.colinmarsch.simpleweather.simpleweatherapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by colinmarsch on 2017-05-01.
 */


public class AddCity extends AppCompatActivity {
    public EditText mtextView;
    public Button mAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_city_layout);
        mtextView = (EditText) findViewById(R.id.city_text_add);
        mAdd = (Button) findViewById(R.id.add_btn);
        mAdd.setOnClickListener(new View.OnClickListener() {
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
