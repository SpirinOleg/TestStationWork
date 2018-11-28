package com.example.a123.testStation;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.a123.testStation.database.DBHelper;
import com.example.a123.testStation.database.StationSchema;
import com.example.a123.testStation.model.City;
import com.example.a123.testStation.model.Station;

import java.util.ArrayList;
import java.util.List;

public class DBUtil {

    public static List<Station> readDB(DBHelper helper, int from) {
        SQLiteDatabase sqLiteDatabase = helper.getReadableDatabase();
        String[] selectionArgs = new String[1];
        selectionArgs[0] = String.valueOf(from);
        Cursor cursor = sqLiteDatabase.rawQuery(" SELECT * FROM " + StationSchema.StationTable.TABLE_NAME + " stt " + " JOIN " + StationSchema.CityTable.TABLE_NAME + " ct " +
                " ON " + " stt. " + StationSchema.StationTable.Cols.CITY_ID + " = " + " ct. " + StationSchema.CityTable.Cols.CITY_ID +
                " WHERE " + " ct. " + StationSchema.CityTable.Cols.DIRECTION_TYPE + " = ? ", selectionArgs);
        List<Station> stations = new ArrayList<>();
        while (cursor.moveToNext()) {
            Station s = new Station();
            s.setCountryTitle(cursor.getString(cursor.getColumnIndex(StationSchema.StationTable.Cols.COUNTRY_TITLE)));
            s.setCityId(cursor.getInt(cursor.getColumnIndex(StationSchema.StationTable.Cols.CITY_ID)));
            s.setStationTitle(cursor.getString(cursor.getColumnIndex(StationSchema.StationTable.Cols.STATION_TITLE)));
            s.setStationId(cursor.getInt(cursor.getColumnIndex(StationSchema.StationTable.Cols.STATION_ID)));
            s.setDistrictTitle(cursor.getString(cursor.getColumnIndex(StationSchema.StationTable.Cols.DISTRICT_TITLE)));
            s.setRegionTitle(cursor.getString(cursor.getColumnIndex(StationSchema.StationTable.Cols.REGION_TITLE)));
            s.setCityId(cursor.getInt(cursor.getColumnIndex(StationSchema.CityTable.Cols.CITY_ID)));
            s.setCountryTitle(cursor.getString(cursor.getColumnIndex(StationSchema.CityTable.Cols.COUNTRY_TITLE)));
            s.setCityTitle(cursor.getString(cursor.getColumnIndex(StationSchema.CityTable.Cols.CITY_TITLE)));
            stations.add(s);
        }
        cursor.close();
        return stations;
    }

    public static void recCityToDB(DBHelper helper, List<City> cities, int from) {
        SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
        sqLiteDatabase.execSQL(" DELETE FROM " + StationSchema.CityTable.TABLE_NAME);
        sqLiteDatabase.execSQL(" DELETE FROM " + StationSchema.StationTable.TABLE_NAME);

        for (City city : cities) {
            ContentValues valuesCity = new ContentValues();
            valuesCity.put(StationSchema.CityTable.Cols.CITY_ID, city.getCityId());
            valuesCity.put(StationSchema.CityTable.Cols.COUNTRY_TITLE, city.getCountryTitle());
            valuesCity.put(StationSchema.CityTable.Cols.DIRECTION_TYPE, from);
            valuesCity.put(StationSchema.CityTable.Cols.CITY_TITLE, city.getCityTitle());
            sqLiteDatabase.insert(StationSchema.CityTable.TABLE_NAME, null, valuesCity);

            for (Station station : city.getStations()) {
                ContentValues valuesStation = new ContentValues();
                valuesStation.put(StationSchema.StationTable.Cols.COUNTRY_TITLE, station.getCountryTitle());
                valuesStation.put(StationSchema.StationTable.Cols.CITY_ID, station.getCityId());
                valuesStation.put(StationSchema.StationTable.Cols.STATION_TITLE, station.getStationTitle());
                valuesStation.put(StationSchema.StationTable.Cols.STATION_ID, station.getStationId());
                valuesStation.put(StationSchema.StationTable.Cols.DISTRICT_TITLE, station.getDistrictTitle());
                valuesStation.put(StationSchema.StationTable.Cols.REGION_TITLE, station.getRegionTitle());
                sqLiteDatabase.insert(StationSchema.StationTable.TABLE_NAME, null, valuesStation);
            }
        }
    }

    public static Cursor selectCity(DBHelper helper) {

        SQLiteDatabase sqLiteDatabase = helper.getReadableDatabase();
        return sqLiteDatabase.query("city",
                null,
                null,
                null,
                null,
                null,
                null);
    }
}
