package com.example.a123.teststation.activity;


import android.app.Activity;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.a123.teststation.OnItemRecyclerClick;
import com.example.a123.teststation.R;
import com.example.a123.teststation.adapter.TimingAdapter;
import com.example.a123.teststation.database.DBHelper;
import com.example.a123.teststation.fragments.ScheduleFragment;
import com.example.a123.teststation.model.City;
import com.example.a123.teststation.model.CityTablo;
import com.example.a123.teststation.model.Station;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.example.a123.teststation.database.StationSchema.CityTable;
import static com.example.a123.teststation.database.StationSchema.StationTable;


public class TimingActivity extends AppCompatActivity implements OnItemRecyclerClick {

    private static final String PREFERENCES_FILENAME = "pref_db";
    private static final String PREFERENCES_CITY_UPDATE = "city_update";
    private static final String PREFERENCES_STATION_UPDATE = "station_update";
    private TimingAdapter adapter;
    private RecyclerView recyclerView;
    private StationAsyncTask task;
    private SearchView searchView;
    private boolean flag; //переменная булевская для преключения между списком городов отправления и прибытия

    public static void recCityToDB(Context context, List<City> cities, int from) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_FILENAME, Context.MODE_PRIVATE);
        preferences.edit().putBoolean(PREFERENCES_CITY_UPDATE, false).apply();
        DBHelper helper = new DBHelper(context);
        SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM " + CityTable.TABLE_NAME);
        //int destination = from ? 0 : 1;

        for (City city : cities) {
            ContentValues values = new ContentValues();
            values.put(CityTable.Cols.CITY_ID, city.getCityId());
            values.put(CityTable.Cols.CITY_TITLE, city.getCityTitle());
            values.put(CityTable.Cols.COUNTRY_TITLE, city.getCountryTitle());
            values.put(CityTable.Cols.DIRECTION_TYPE, from);
            sqLiteDatabase.insert(CityTable.TABLE_NAME, null, values);
        }
        sqLiteDatabase.close();
        preferences.edit().putBoolean(PREFERENCES_CITY_UPDATE, true).apply();
    }

    public static void recStationToDB(Context context, List<Station> stations) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_FILENAME, Context.MODE_PRIVATE);
        preferences.edit().putBoolean(PREFERENCES_STATION_UPDATE, false).apply();
        DBHelper helper = new DBHelper(context);
        SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM " + StationTable.TABLE_NAME);
        for (Station station : stations) {
            ContentValues values = new ContentValues();
            values.put(StationTable.Cols.COUNTRY_TITLE, station.getCountryTitle());
            values.put(StationTable.Cols.CITY_ID, station.getCityId());
            values.put(StationTable.Cols.STATION_TITLE, station.getStationTitle());
            values.put(StationTable.Cols.STATION_ID, station.getStationId());
            values.put(StationTable.Cols.DISTRICT_TITLE, station.getDistrictTitle());
            values.put(StationTable.Cols.REGION_TITLE, station.getRegionTitle());
            sqLiteDatabase.insert(StationTable.TABLE_NAME, null, values);
        }
        sqLiteDatabase.close();
        preferences.edit().putBoolean(PREFERENCES_STATION_UPDATE, true).apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_timing);

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        task = new StationAsyncTask();
        task.execute();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int direction = bundle.getInt(ScheduleFragment.KEY, -1);
            if (direction == ScheduleFragment.DIRECTION_DEP) {
                flag = true;
            } else {
                flag = false;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timing_fragment, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.item_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                adapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.item_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        task.cancel(true);
    }

    @Override
    public void onClick(int position, Station station) {
        Intent intent = new Intent();
        intent.putExtra("STATION", station);
        intent.putExtra("flag", flag);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onInfoClick(Station station) {
        Intent intentInfo = new Intent(this, InfoActivity.class);
        intentInfo.putExtra("INFOSTATION", station);
        startActivity(intentInfo);
    }

    public class StationAsyncTask extends AsyncTask<String, Integer, List<Station>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Station> doInBackground(String... strings) {
            List<Station> allstations = new ArrayList<>();

            try {

                InputStream file = getResources().openRawResource(R.raw.allstations);
                BufferedReader rd = new BufferedReader(new InputStreamReader(file));
                Gson gson = new Gson();

                SharedPreferences preferences = getApplicationContext().getSharedPreferences(PREFERENCES_FILENAME, getApplicationContext().MODE_PRIVATE);
                if (preferences.getBoolean(PREFERENCES_CITY_UPDATE, false) & preferences.getBoolean(PREFERENCES_STATION_UPDATE, false)) {
                    CityTablo citiTablo = gson.fromJson(rd, CityTablo.class);
                    List<City> cities;
                    if (flag) {
                        cities = citiTablo.getCitiesFrom();
                        recCityToDB(getApplicationContext(), cities, 0);
                    } else {
                        cities = citiTablo.getCitiesTo();
                        recCityToDB(getApplicationContext(), cities, 1);
                    }
                    for (City city : cities) {
                        allstations.addAll(city.getStations());
                    }
                    recStationToDB(getApplicationContext(), allstations);
                } else {
                    CityTablo citiTablo = gson.fromJson(rd, CityTablo.class);
                    List<City> cities;
                    if (flag) {
                        cities = citiTablo.getCitiesFrom();
                    } else {
                        cities = citiTablo.getCitiesTo();
                    }
                    for (City city : cities) {
                        allstations.addAll(city.getStations());
                    }
                }
            } catch (Exception e) {
                runOnUiThread(() -> toast(R.string.msg_error_json));
            }
            return allstations;

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(List<Station> stations) {
            super.onPostExecute(stations);
            adapter = new TimingAdapter(stations, TimingActivity.this);
            recyclerView.setAdapter(adapter);
            toast(R.string.msg_task_complited);
        }

        @Override
        protected void onCancelled() {
            toast(R.string.msg_task_canceled);
            super.onCancelled();
        }

        private void toast(int message) {
            Activity activity = TimingActivity.this;
            if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
