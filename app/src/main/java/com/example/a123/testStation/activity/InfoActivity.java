package com.example.a123.testStation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a123.testStation.R;
import com.example.a123.testStation.model.Station;


public class InfoActivity extends AppCompatActivity {

    private static final String EXTRA_INFO_STATION = "com.example.a123.testStation.activity.InfoActivity";

    public static Intent newIntent(Context packageContext, Station station) {
        Intent intent = new Intent(packageContext, InfoActivity.class);
        intent.putExtra(EXTRA_INFO_STATION, station);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Station station = getIntent().getParcelableExtra(EXTRA_INFO_STATION);

        if (station != null) {
            TextView station_title = findViewById(R.id.txt_station);
            TextView city_title = findViewById(R.id.txt_city);
            TextView district_title = findViewById(R.id.txt_district);
            TextView country_title = findViewById(R.id.txt_country);

            station_title.setText(station.getStationTitle());
            city_title.setText(station.getCityTitle());
            district_title.setText(station.getDistrictTitle());
            country_title.setText(station.getCountryTitle());
        } else {
            Toast.makeText(this, R.string.msg_error_no_data, Toast.LENGTH_SHORT).show();
        }

    }

}
