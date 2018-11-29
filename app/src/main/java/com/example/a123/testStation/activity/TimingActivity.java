package com.example.a123.testStation.activity;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.a123.testStation.DBUtil;
import com.example.a123.testStation.Direction;
import com.example.a123.testStation.OnItemRecyclerClick;
import com.example.a123.testStation.R;
import com.example.a123.testStation.adapter.TimingAdapter;
import com.example.a123.testStation.database.DBHelper;
import com.example.a123.testStation.fragments.ScheduleFragment;
import com.example.a123.testStation.model.City;
import com.example.a123.testStation.model.CityTable;
import com.example.a123.testStation.model.Station;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


import static com.example.a123.testStation.fragments.ScheduleFragment.EXTRA_FLAG;
import static com.example.a123.testStation.fragments.ScheduleFragment.EXTRA_STATION;
import static com.example.a123.testStation.fragments.ScheduleFragment.KEY;


public class TimingActivity extends AppCompatActivity implements OnItemRecyclerClick {

    private TimingAdapter adapter;
    private RecyclerView recyclerView;
    private StationAsyncTask task;
    private SearchView searchView;
    private boolean flagDirection; //переменная булевская для преключения между списком городов отправления и прибытия


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

        InputStream file = getApplicationContext().getResources().openRawResource(R.raw.allstations);
        task = new StationAsyncTask(TimingActivity.this, new DBHelper(getApplicationContext()), file);
        task.execute();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int direction = bundle.getInt(ScheduleFragment.KEY, -1);
//            if (direction == ScheduleFragment.DIRECTION_DEP) flagDirection = true;
//            else flagDirection = false;
            flagDirection = direction == Direction.FROM.getSqlValue();
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
        intent.putExtra(EXTRA_FLAG, flagDirection);
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
        private final DBHelper helper;
        private final InputStream file;


        private StationAsyncTask(TimingActivity activity, DBHelper helper, InputStream file){
            weakReference = new WeakReference<>(activity);
            this.helper = helper;
            this.file = file;
        }

        @Override
        protected List<Station> doInBackground(String... strings) {
            List<Station> allStations = new ArrayList<>();

            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(file));
                Gson gson = new Gson();
                CityTable cityTable = gson.fromJson(rd, CityTable.class);
                List<City> cities;

                if (!DBUtil.selectCity(helper).moveToFirst()) {
                    cities = cityTable.getCitiesFrom();
//                    DBUtil.recCityToDB(helper, cities, 0);
                    DBUtil.recCityToDB(helper, cities, Direction.FROM.getSqlValue());
                    cities = cityTable.getCitiesTo();
//                    DBUtil.recCityToDB(helper, cities, 1);
                    DBUtil.recCityToDB(helper, cities, Direction.TO.getSqlValue());
                    for (City city : cities) {
                        allStations.addAll(city.getStations());
                    }
                } else {
//                    allStations = DBUtil.readDB(helper, 0);
                    allStations = DBUtil.readDB(helper, Direction.FROM.getSqlValue());
//                    allStations = DBUtil.readDB(helper, 1);
                    allStations = DBUtil.readDB(helper, Direction.TO.getSqlValue());

                }
                DBUtil.selectCity(helper).close();
            } catch (Exception e) {
                Log.d("AsyncTask", String.valueOf(R.string.msg_error_json));

            }
            return allStations;
        }

        @Override
        protected void onPostExecute(List<Station> stations) {
            if(!(stations == null)){
            super.onPostExecute(stations);
            TimingActivity ti = weakReference.get();
            if(!ti.isFinishing() || !ti.isDestroyed()){
                ti.adapter = new TimingAdapter(stations, ti);
                ti.recyclerView.setAdapter(ti.adapter);
            } else {
                Log.d("PostExecute", String.valueOf(R.string.msg_error_json));
                    }
            }  else {
                Log.d("PostExecute", String.valueOf(R.string.msg_error_json));
            }
        }

        @Override
        protected void onCancelled() {
            Log.d("PostExecute", String.valueOf(R.string.msg_task_canceled));
            super.onCancelled();
        }

    }
}
