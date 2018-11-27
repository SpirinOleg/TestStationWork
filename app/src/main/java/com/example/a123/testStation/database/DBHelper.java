package com.example.a123.testStation.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "station.db";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE_STATION = " CREATE TABLE " + StationSchema.StationTable.TABLE_NAME + " ( " +
            " _ID integer primary key autoincrement,  " +
            StationSchema.StationTable.Cols.COUNTRY_TITLE + " TEXT, " +
            StationSchema.StationTable.Cols.CITY_ID + " INTEGER, " +
            StationSchema.StationTable.Cols.STATION_TITLE + " TEXT, " +
            StationSchema.StationTable.Cols.STATION_ID + " INTEGER, " +
            StationSchema.StationTable.Cols.DISTRICT_TITLE + " TEXT, " +
            StationSchema.StationTable.Cols.REGION_TITLE + " TEXT, " +
            "FOREIGN KEY (" + StationSchema.StationTable.Cols.CITY_ID + ") REFERENCES " + StationSchema.CityTable.TABLE_NAME + "  ( " + StationSchema.CityTable.Cols.CITY_ID + " )) ";

    private static final String SQL_CREATE_TABLE_CITY = " CREATE TABLE " + StationSchema.CityTable.TABLE_NAME + " ( " +
            " _ID integer primary key autoincrement,  " +
            StationSchema.CityTable.Cols.CITY_ID + " INTEGER, " +
            StationSchema.CityTable.Cols.COUNTRY_TITLE + " TEXT, " +
            StationSchema.CityTable.Cols.DIRECTION_TYPE + " INTEGER, " +
            StationSchema.CityTable.Cols.CITY_TITLE + " TEXT " +
            " ) ";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_STATION);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_CITY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + StationSchema.StationTable.TABLE_NAME);
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + StationSchema.CityTable.TABLE_NAME);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_STATION);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_CITY);
    }
}
