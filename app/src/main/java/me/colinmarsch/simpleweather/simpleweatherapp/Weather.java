package me.colinmarsch.simpleweather.simpleweatherapp;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.android.volley.VolleyLog.TAG;

/**
 * Created by colinmarsch on 2016-12-26.
 */

public class Weather {

    private RequestQueue mRequestQueue;
    private static Weather mInstance;
    private static Context mContxt;
    private static final String TAG = Weather.class.getName();

    private Weather(Context context) {
        mContxt = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized Weather getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new Weather(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if(mRequestQueue == null) {
            // getApplicationContext() here stops the potential leaking of Activity or BroadcastReceiver
            mRequestQueue = Volley.newRequestQueue(mContxt.getApplicationContext());
        }
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
