package com.example.a123.testStation.activity;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import com.example.a123.testStation.R;

import static com.example.a123.testStation.fragments.ScheduleFragment.KEY;


public class TimingActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timing_activity);

        Bundle bundle = getIntent().getExtras();

        if(savedInstanceState == null) {
            if (bundle != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.timing_container, TimingFragment.newInstance(bundle.getString(KEY))).commit();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
