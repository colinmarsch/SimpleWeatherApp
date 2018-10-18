package me.colinmarsch.simpleweather.simpleweatherapp.interfaces;

import org.json.JSONObject;

public interface SuccessfulResponseInterface {
    void onSuccess(JSONObject response);
}
