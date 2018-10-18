package me.colinmarsch.simpleweather.simpleweatherapp.data;

import android.provider.BaseColumns;

public final class SavedCitiesContract {

    private SavedCitiesContract() {}

    public static class CitiesEntry implements BaseColumns {

        public static final String TABLE_NAME = "saved_cities";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_NAME_CITY = "city";
    }
}
