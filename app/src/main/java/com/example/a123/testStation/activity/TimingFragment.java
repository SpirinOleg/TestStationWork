package com.example.a123.testStation.activity;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.a123.testStation.DBUtil;
import com.example.a123.testStation.Direction;
import com.example.a123.testStation.OnItemRecyclerClick;
import com.example.a123.testStation.R;
import com.example.a123.testStation.adapter.TimingAdapter;
import com.example.a123.testStation.database.DBHelper;
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

import static android.app.Activity.RESULT_OK;
import static com.example.a123.testStation.fragments.ScheduleFragment.EXTRA_FLAG;
import static com.example.a123.testStation.fragments.ScheduleFragment.EXTRA_STATION;
import static com.example.a123.testStation.fragments.ScheduleFragment.KEY;


public class TimingFragment extends Fragment implements OnItemRecyclerClick {

    private TimingAdapter adapter =  new TimingAdapter(this);
    private RecyclerView recyclerView;
    private StationAsyncTask task;
    private SearchView searchView;
    private ProgressBar progressBar;
    private Direction flagDirection; //переменная булевская для преключения между списком городов отправления и прибытия

    public static TimingFragment newInstance(String flagDirection) {

        Bundle args = new Bundle();
        args.putString(KEY, flagDirection);

        TimingFragment fragment = new TimingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);


        Bundle bundle = getArguments();
        if (bundle != null) {
            flagDirection = Direction.valueOf(bundle.getString(KEY));
        }

        InputStream file = requireContext().getResources().openRawResource(R.raw.allstations);
        task = new StationAsyncTask(TimingFragment.this, new DBHelper(requireContext()), file, flagDirection);

    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_timing, container, false);

//        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);

        progressBar = view.findViewById(R.id.progressBar);

        if(savedInstanceState == null){
            task.execute();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_timing_fragment, menu);

        SearchManager searchManager = (SearchManager) requireContext().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.item_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().getComponentName()));
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
    public void onDestroy() {
        super.onDestroy();
        task.cancel(true);
    }

    @Override
    public void onClick(Station station) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_STATION, station);
        intent.putExtra(EXTRA_FLAG, flagDirection.name());
        requireActivity().setResult(RESULT_OK, intent);
        requireActivity().finish();
    }

    @Override
    public void onInfoClick(Station station) {
        Intent intentInfo = InfoActivity.newIntent(requireContext(), station);
        startActivity(intentInfo);
    }

    private static class StationAsyncTask extends AsyncTask<String, Integer, List<Station>> {
        private final WeakReference<TimingFragment> weakReference;
        private final DBHelper helper;
        private final InputStream file;
        private int flagDirection;


        private StationAsyncTask(TimingFragment activity, DBHelper helper, InputStream file, Direction flagDirection) {
            weakReference = new WeakReference<>(activity);
            this.helper = helper;
            this.file = file;
            this.flagDirection = flagDirection.getSqlValue();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            TimingFragment activity = weakReference.get();

            activity.progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Station> doInBackground(String... strings) {
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            List<Station> allStations = new ArrayList<>();
//            for (int i = 0; i < 100; i++) {
//                try {
//                    Thread.sleep(100);
//                    publishProgress(i);
//                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt();
//                    break;
//                }
//            }

            try {
                if (!DBUtil.selectCity(helper).moveToFirst()) {
                    TimingFragment activity = weakReference.get();
                    if (activity.progressBar.getProgress() == 0) {
//                        int i = 0;
//                        activity.progressBar.incrementProgressBy(i);
                        BufferedReader rd = new BufferedReader(new InputStreamReader(file));
                        Gson gson = new Gson();
                        CityTable cityTable = gson.fromJson(rd, CityTable.class);
//                        for(i = 0;i <= 50;i++){
//                                activity.progressBar.incrementProgressBy(1);
//                        }

                        DBUtil.recCityToDB(helper, cityTable.getCitiesFrom(), Direction.FROM.getSqlValue());
                        DBUtil.recCityToDB(helper, cityTable.getCitiesTo(), Direction.TO.getSqlValue());
//                        for(i = 50;i <= 75;i++){
//                            activity.progressBar.incrementProgressBy(1);
//                        }

                        if (flagDirection == Direction.FROM.getSqlValue()) {
                            for (City city : cityTable.getCitiesFrom()) {
                                allStations.addAll(city.getStations());
                            }
                        } else {
                            for (City city : cityTable.getCitiesTo()) {
                                allStations.addAll(city.getStations());
                            }
                        }
//                        for(i = 75;i <= 100;i++){
//                            activity.progressBar.incrementProgressBy(1);
//                        }
                    }
                } else {
                    allStations = DBUtil.readDB(helper, flagDirection);
                }
            } catch (Exception e) {
                Log.d("AsyncTask", String.valueOf(R.string.msg_error_json));
            }
            return allStations;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            TimingFragment activity = weakReference.get();
            activity.progressBar.setProgress(values[0]);

        }

        @Override
        protected void onPostExecute(List<Station> stations) {
            super.onPostExecute(stations);
            TimingFragment ti = weakReference.get();
            ti.progressBar.setVisibility(View.INVISIBLE);
            if (ti != null) {
                ti.adapter.setData(stations);
            } else {
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
