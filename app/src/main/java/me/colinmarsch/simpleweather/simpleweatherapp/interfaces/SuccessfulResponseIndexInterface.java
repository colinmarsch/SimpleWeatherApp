package me.colinmarsch.simpleweather.simpleweatherapp.interfaces;

import org.json.JSONObject;

public interface SuccessfulResponseIndexInterface {
    void onSuccess(JSONObject response, int i);
}
