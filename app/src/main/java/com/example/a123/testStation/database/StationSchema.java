package com.example.a123.testStation.database;


import android.provider.BaseColumns;

public class StationSchema implements BaseColumns {

    public static final class StationTable {
        public static final String TABLE_NAME = "station";

        public static final class Cols {

            public static final String COUNTRY_TITLE = "country_title";
            public static final String CITY_ID = "city_id";
            public static final String STATION_TITLE = "station_title";
            public static final String STATION_ID = "station_id";
            public static final String DISTRICT_TITLE = "district_title";
            public static final String REGION_TITLE = "region_title";
        }
    }

    public static final class CityTable {
        public static final String TABLE_NAME = "city";

        public static final class Cols {

            public static final String CITY_ID = "city_id";
            public static final String COUNTRY_TITLE = "country_title";
            public static final String DIRECTION_TYPE = "direction_type";
            public static final String CITY_TITLE = "city_title";
        }
    }
}
