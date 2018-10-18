package me.colinmarsch.simpleweather.simpleweatherapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import me.colinmarsch.simpleweather.simpleweatherapp.data.SavedCitiesContract.CitiesEntry;

public class SavedCitiesHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cities2.db";
    private static final int DATABASE_VERSION = 1;

    public SavedCitiesHelper(Context context) {
        //null takes the place of a cursor factory
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_CITIES_TABLE = "CREATE TABLE " + CitiesEntry.TABLE_NAME + " ( " +
                                        CitiesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                                        CitiesEntry.COLUMN_NAME_CITY + " TEXT NOT NULL );";
        System.out.println(SQL_CREATE_CITIES_TABLE);
        db.execSQL(SQL_CREATE_CITIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
