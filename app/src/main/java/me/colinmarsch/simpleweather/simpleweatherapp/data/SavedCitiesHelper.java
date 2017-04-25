package me.colinmarsch.simpleweather.simpleweatherapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import me.colinmarsch.simpleweather.simpleweatherapp.data.SavedCitiesContract.CitiesEntry;

/**
 * Created by colinmarsch on 2017-04-24.
 */

public class SavedCitiesHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "cities.db";
    private static final int DATABASE_VERSION = 1;

    public SavedCitiesHelper(Context context) {
        //null takes the place of a cursor factory
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_CITIES_TABLE = "CREATE TABLE " + CitiesEntry.TABLE_NAME + " (" +
                                        CitiesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                                        CitiesEntry.COLUMN_NAME_CITY + "TEXT NOT NULL);";
        db.execSQL(SQL_CREATE_CITIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
