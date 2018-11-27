package com.example.a123.testStation.activity;


import android.app.Activity;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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

import com.example.a123.testStation.OnItemRecyclerClick;
import com.example.a123.testStation.R;
import com.example.a123.testStation.adapter.TimingAdapter;
import com.example.a123.testStation.database.DBHelper;
import com.example.a123.testStation.database.StationSchema;
import com.example.a123.testStation.fragments.ScheduleFragment;
import com.example.a123.testStation.model.City;
import com.example.a123.testStation.model.Station;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.example.a123.testStation.database.StationSchema.StationTable;
import static com.example.a123.testStation.fragments.ScheduleFragment.EXTRA_FLAG;
import static com.example.a123.testStation.fragments.ScheduleFragment.EXTRA_STATION;


public class TimingActivity extends AppCompatActivity implements OnItemRecyclerClick {

    private TimingAdapter adapter;
    private RecyclerView recyclerView;
    private StationAsyncTask task;
    private SearchView searchView;
    private boolean flag; //переменная булевская для преключения между списком городов отправления и прибытия


    private static List<Station> readDB(Context context, int from) {
        DBHelper helper = new DBHelper(context);
        SQLiteDatabase sqLiteDatabase = helper.getReadableDatabase();
        String[] selectionArgs = new String[1];
        selectionArgs[0] = String.valueOf(from);
        Cursor cursor = sqLiteDatabase.rawQuery(" SELECT * FROM " + StationTable.TABLE_NAME + " stt " + " JOIN " + StationSchema.CityTable.TABLE_NAME + " ct " +
                " ON " + " stt. " + StationTable.Cols.CITY_ID + " = " + " ct. " + StationSchema.CityTable.Cols.CITY_ID +
                " WHERE " + " ct. " + StationSchema.CityTable.Cols.DIRECTION_TYPE + " = ? ", selectionArgs);
        List<Station> stations = new ArrayList<>();
        while (cursor.moveToNext()) {
            Station s = new Station();
            s.setCountryTitle(cursor.getString(cursor.getColumnIndex(StationTable.Cols.COUNTRY_TITLE)));
            s.setCityId(cursor.getInt(cursor.getColumnIndex(StationTable.Cols.CITY_ID)));
            s.setStationTitle(cursor.getString(cursor.getColumnIndex(StationTable.Cols.STATION_TITLE)));
            s.setStationId(cursor.getInt(cursor.getColumnIndex(StationTable.Cols.STATION_ID)));
            s.setDistrictTitle(cursor.getString(cursor.getColumnIndex(StationTable.Cols.DISTRICT_TITLE)));
            s.setRegionTitle(cursor.getString(cursor.getColumnIndex(StationTable.Cols.REGION_TITLE)));
            s.setCityId(cursor.getInt(cursor.getColumnIndex(StationSchema.CityTable.Cols.CITY_ID)));
            s.setCountryTitle(cursor.getString(cursor.getColumnIndex(StationSchema.CityTable.Cols.COUNTRY_TITLE)));
            s.setCityTitle(cursor.getString(cursor.getColumnIndex(StationSchema.CityTable.Cols.CITY_TITLE)));
            stations.add(s);
        }
        cursor.close();
        return stations;
    }

    private static void recCityToDB(Context context, List<City> cities, int from) {
        DBHelper helper = new DBHelper(context);
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
                valuesStation.put(StationTable.Cols.COUNTRY_TITLE, station.getCountryTitle());
                valuesStation.put(StationTable.Cols.CITY_ID, station.getCityId());
                valuesStation.put(StationTable.Cols.STATION_TITLE, station.getStationTitle());
                valuesStation.put(StationTable.Cols.STATION_ID, station.getStationId());
                valuesStation.put(StationTable.Cols.DISTRICT_TITLE, station.getDistrictTitle());
                valuesStation.put(StationTable.Cols.REGION_TITLE, station.getRegionTitle());
                sqLiteDatabase.insert(StationTable.TABLE_NAME, null, valuesStation);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_timing);

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        task = new StationAsyncTask(TimingActivity.this);
        task.execute();


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int direction = bundle.getInt(ScheduleFragment.KEY, -1);
            flag = direction == ScheduleFragment.DIRECTION_DEP;
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
    public void onClick(Station station) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_STATION, station);
        intent.putExtra(EXTRA_FLAG, flag);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onInfoClick(Station station) {
        Intent intentInfo = InfoActivity.newIntent(this, station);
        startActivity(intentInfo);
    }

    private static class StationAsyncTask extends AsyncTask<String, Integer, List<Station>> {
        private final WeakReference<TimingActivity> weakReference;

        private StationAsyncTask(TimingActivity activity){
            weakReference = new WeakReference<>(activity);
        }

        @Override
        protected List<Station> doInBackground(String... strings) {
            List<Station> allStations = new ArrayList<>();

            try {
                InputStream file = weakReference.get().getApplicationContext().getResources().openRawResource(R.raw.allstations);
                BufferedReader rd = new BufferedReader(new InputStreamReader(file));
                Gson gson = new Gson();
                com.example.a123.testStation.model.CityTable cityTable = gson.fromJson(rd, com.example.a123.testStation.model.CityTable.class);
                List<City> cities;

                DBHelper helper = new DBHelper(weakReference.get().getApplicationContext());
                SQLiteDatabase sqLiteDatabase = helper.getReadableDatabase();
                Cursor cursor = sqLiteDatabase.query("city",
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);
                if (!cursor.moveToFirst()) {
                    cities = cityTable.getCitiesFrom();
                    recCityToDB(weakReference.get().getApplicationContext(), cities, 0);
                    cities = cityTable.getCitiesTo();
                    recCityToDB(weakReference.get().getApplicationContext(), cities, 1);
                    for (City city : cities) {
                        allStations.addAll(city.getStations());
                    }
                } else {
                    allStations = readDB(weakReference.get().getApplicationContext(), 0);
                    allStations = readDB(weakReference.get().getApplicationContext(), 1);

                }
                cursor.close();
                sqLiteDatabase.close();
            } catch (Exception e) {
                weakReference.get().runOnUiThread(() -> toast(R.string.msg_error_json));
            }
            return allStations;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(List<Station> stations) {
            super.onPostExecute(stations);
            weakReference.get().adapter = new TimingAdapter(stations, weakReference.get());
            //adapter = new TimingAdapter(stations, TimingActivity.this);
            weakReference.get().recyclerView.setAdapter(weakReference.get().adapter);
            toast(R.string.msg_task_done);
        }

        @Override
        protected void onCancelled() {
            toast(R.string.msg_task_canceled);
            super.onCancelled();
        }

        private void toast(int message) {
            Activity activity = weakReference.get();
            if (!activity.isFinishing() && !activity.isDestroyed()) {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
