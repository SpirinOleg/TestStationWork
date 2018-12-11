package com.example.a123.testStation.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.a123.testStation.R;
import com.example.a123.testStation.fragments.DatePickerFragment;
import com.example.a123.testStation.fragments.ScheduleFragment;
import com.example.a123.testStation.fragments.AboutFragment;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DatePickerFragment.OnDatePickerListener {

    private int selectedItem = -1;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
//        navigationView.getMenu().performIdentifierAction(R.id.timing, 0);

        if(savedInstanceState == null){

            navigationView.getMenu().performIdentifierAction(R.id.timing, 0);
            //setTitle(navigationView.getCheckedItem().getTitle());
            //getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, new ScheduleFragment(), ScheduleFragment.FRAGMENT_TAG).commit();
        }
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        item.setChecked(true);
        setTitle(item.getTitle());
        if (selectedItem != id) {
            if (id == R.id.timing) {
                getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, new ScheduleFragment(), ScheduleFragment.FRAGMENT_TAG).commit();
            } else {
                getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, new AboutFragment()).commit();
            }
            selectedItem = id;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDateChanged(Calendar calendar) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(ScheduleFragment.FRAGMENT_TAG);
        if (fragment instanceof ScheduleFragment) {
            ((ScheduleFragment) fragment).setDate(calendar);
        }
    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putString("Title", getTitle().toString());
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        setTitle(savedInstanceState.getString("Title"));
//    }
}