package me.colinmarsch.simpleweather.simpleweatherapp.data;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Weather {

    private RequestQueue mRequestQueue;
    private static Weather mInstance;
    private Context mContxt;
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

    private RequestQueue getRequestQueue() {
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
