package me.colinmarsch.simpleweather.simpleweatherapp;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.android.volley.VolleyLog.TAG;

/**
 * Created by colinmarsch on 2016-12-26.
 */

public class Weather extends Application{

    private RequestQueue mRequestQueue;
    private static Weather mInstance;
    public static final String TAG = Weather.class.getName();

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
    }

    public static synchronized Weather getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public <T> void add(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancel() {
        mRequestQueue.cancelAll(TAG);
    }
}
