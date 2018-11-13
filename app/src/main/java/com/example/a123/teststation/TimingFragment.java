package com.example.a123.teststation;


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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.example.a123.teststation.StationSchema.*;


public class TimingFragment extends AppCompatActivity implements OnItemRecyclerClick {

    private static final String TAG = TimingFragment.class.getSimpleName();
    private TimingAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ProgressBar progressBar;
    private StationAsyncTask task;
    private SearchView searchView;

    public static final String PREFERENCES_FILENAME = "pref_db";
    public static final String PREFERENCES_UPDATE = "update_db";
    public static final String PREFERENCES_CITY_UPDATE = "city_update";
    public static final String PREFERENCES_STATION_UPDATE = "station_update";

    //Надо убрать после добавления БД
    private boolean flag; //переменная булевская для преключения между списком городов отправления и прибытия

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_timing);


        progressBar = findViewById(R.id.progressBar);
        progressBar.setProgress(0);


        mRecyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timing_fragment, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.item_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String query) {
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.item_search){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()){
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onResume() {
        super.onResume();
        task = new StationAsyncTask();
        task.execute();

    }

    @Override
    public void onPause() {
        super.onPause();
        task.cancel(true);
    }

    //0 - отправление from,  1 - назначение to


    public static void recCityToDB(Context context, List<City> cities, int from) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_FILENAME, Context.MODE_PRIVATE);
        preferences.edit().putBoolean(PREFERENCES_CITY_UPDATE, false).apply();
        if(preferences.getBoolean(PREFERENCES_CITY_UPDATE, false)) {
        preferences.edit().putBoolean(PREFERENCES_UPDATE, false).apply();
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
        preferences.edit().putBoolean(PREFERENCES_UPDATE, true).apply();
        preferences.edit().putBoolean(PREFERENCES_CITY_UPDATE, true).apply();
        } else {
            preferences.edit().putBoolean(PREFERENCES_CITY_UPDATE, true).apply();
        }
    }

    public static void recStationToDB(Context context, List<Station> stations) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_FILENAME, Context.MODE_PRIVATE);
        preferences.getBoolean(PREFERENCES_STATION_UPDATE, false);
        if(preferences.getBoolean(PREFERENCES_STATION_UPDATE, false)) {
            preferences.edit().putBoolean(PREFERENCES_UPDATE, false).apply();
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
            preferences.edit().putBoolean(PREFERENCES_UPDATE, true).apply();
            preferences.edit().putBoolean(PREFERENCES_STATION_UPDATE, true).apply();
        } else {
            preferences.edit().putBoolean(PREFERENCES_STATION_UPDATE, true).apply();
        }
    }


    @Override
    public void onClick(int position, Station station) {
        //Log.d("TimingFragment", Integer.toString(station.getStationId()));
        Intent intent = new Intent();
        intent.putExtra("STATION", station);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onInfoClick(int position, Station station) {
        Intent intentInfo = new Intent(this, InfoActivity.class);
        intentInfo.putExtra("INFOSTATION", station);
        startActivity(intentInfo);
    }

    public class StationAsyncTask extends AsyncTask<String, Integer, List<Station>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Log.e(TAG, "Задача запущена");
            progressBar.setVisibility(View.VISIBLE);
            toast("Задача запущена");
        }

        @Override
        protected List<Station> doInBackground(String... strings) {
            List<Station> allstations = new ArrayList<>();
            runOnUiThread(() -> toast("Начинаю бессмыссленную работу"));
            for (int i = 0; i < 100; i++) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
                publishProgress(i);
            }
            runOnUiThread(() -> toast("Начинаю полезную работу"));
            try {

                InputStream file = getResources().openRawResource(R.raw.allstations);
                BufferedReader rd = new BufferedReader(new InputStreamReader(file));
                Gson gson = new Gson();

                SharedPreferences preferences = getApplicationContext().getSharedPreferences(PREFERENCES_FILENAME, getApplicationContext().MODE_PRIVATE);
                if (preferences.getBoolean(PREFERENCES_CITY_UPDATE, false)) {


                    CityTablo citiTablo = gson.fromJson(rd, CityTablo.class);
                    List<City> cities;
                    if (flag) {
//                    Log.e(TAG, "getCitiesFrom" + citiTablo.getCitiesFrom().get(0).getStations().get(0).getStationTitle());
                        cities = citiTablo.getCitiesFrom();
                        recCityToDB(getApplicationContext(), cities, 0);
                    } else {
//                    Log.e(TAG, "getCitiesTo" + citiTablo.getCitiesTo().get(0).getStations().get(0).getStationTitle());
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
//                    Log.e(TAG, "getCitiesFrom" + citiTablo.getCitiesFrom().get(0).getStations().get(0).getStationTitle());
                        cities = citiTablo.getCitiesFrom();
                    } else {
//                    Log.e(TAG, "getCitiesTo" + citiTablo.getCitiesTo().get(0).getStations().get(0).getStationTitle());
                        cities = citiTablo.getCitiesTo();
                    }
                    for (City city : cities) {
                        allstations.addAll(city.getStations());
                    }
                        }
                } catch(Exception e){
                    runOnUiThread(() -> toast("Ошибка импорта json"));
                }
                return allstations;

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);

        }

        @Override
        protected void onPostExecute(List<Station> stations) {
            super.onPostExecute(stations);
            progressBar.setVisibility(View.INVISIBLE);
            mAdapter = new TimingAdapter(stations, TimingFragment.this);
            mRecyclerView.setAdapter(mAdapter);
            //Log.e(TAG, "Задача завершена");
            toast("Задача завершена");
        }

        @Override
        protected void onCancelled() {
            //Log.e(TAG, "onCancelled()");
            toast("Операция отменена");
            super.onCancelled();
        }

        private void toast(String message) {
            Activity activity = TimingFragment.this;
            if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
