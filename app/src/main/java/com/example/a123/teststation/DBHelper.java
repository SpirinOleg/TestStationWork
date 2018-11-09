package com.example.a123.teststation;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.a123.teststation.StationSchema.*;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "station.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String SQL_CREATE_TABLE_STATION = " CREATE TABLE " + StationTable.TABLE_NAME + " ( " +
            "_ID integer primary key autoincrement,  " +
            StationTable.Cols.COUNTRY_TITLE + " TEXT, " +
            StationTable.Cols.CITY_ID + " INTEGER," +
            StationTable.Cols.STATION_TITLE + " TEXT," +
            StationTable.Cols.STATION_ID + " INTEGER," +
            StationTable.Cols.DISTRICT_TITLE + " TEXT," +
            StationTable.Cols.REGION_TITLE + " TEXT, " +
            "FOREIGN KEY (" + StationTable.Cols.CITY_ID + ") REFERENCES " + CityTable.TABLE_NAME + "  ( " + CityTable.Cols.CITY_ID  + " )) ";


    private static final String SQL_CREATE_TABLE_CITY = "CREATE TABLE " + CityTable.TABLE_NAME + "(" +
            " _ID integer primary key autoincrement, " +
            CityTable.Cols.COUNTRY_TITLE + "TEXT," +
            CityTable.Cols.CITY_ID + "INTEGER," +
            CityTable.Cols.CITY_TITLE + "TEXT," +
            CityTable.Cols.DIRECTION_TYPE + "INTEGER" +
            ")";

/*    private static final String SQL_CREATE_TABLE_DIRECTION = "CREATE TABLE " + DirectionTable.TABLE_NAME + "(" +
            " _ID integer primary key autoincrement, " +
            DirectionTable.Cols.CITY_ID + "INTEGER," +
            DirectionTable.Cols.STATION_ID + "INTEGER," +
            DirectionTable.Cols.DIRECTION_TYPE + "INTEGER," +
            " FOREIGN KEY ("+ DirectionTable.Cols.CITY_ID +") REFERENCES " + CityTable.TABLE_NAME + " ("+ CityTable.Cols.CITY_ID +"), "+
            " FOREIGN KEY ("+ DirectionTable.Cols.STATION_ID+") REFERENCES " + StationTable.TABLE_NAME + " ("+ StationTable.Cols.STATION_ID +") "+
            ")";*/


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_STATION);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_CITY);
        //sqLiteDatabase.execSQL(SQL_CREATE_TABLE_DIRECTION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + StationTable.TABLE_NAME);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_STATION);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_CITY);
        //sqLiteDatabase.execSQL(SQL_CREATE_TABLE_DIRECTION);
    }
}
