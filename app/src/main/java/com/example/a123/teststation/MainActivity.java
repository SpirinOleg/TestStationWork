package com.example.a123.teststation;

import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, new AboutFragment()).addToBackStack(null).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
       /* int id = item.getItemId();
        Fragment f = getSupportFragmentManager().getFragments().get(0);
        if (id == R.id.timing) {
            if (!(f instanceof ScheduleFragment)) setMainFragment();
        } else {
            if (!(f instanceof AboutFragment)) setAboutFragment(false);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;*/
        int id = item.getItemId();
        //Fragment f = getSupportFragmentManager().getFragments().get(0);
        if (id == R.id.timing) {
            getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, new ScheduleFragment()).addToBackStack(null).commit();
            //if (!(f instanceof ScheduleFragment)) setMainFragment();
        } else {
            //if (!(f instanceof AboutFragment)) setAboutFragment(false);
            getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, new AboutFragment()).addToBackStack(null).commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

/*    private void setMainFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer,new ScheduleFragment()).addToBackStack(null).commit();
        *//*Fragment mainFragment = new ScheduleFragment();
        getSupportFragmentManager().popBackStack("aboutFragment", 0);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainContainer, mainFragment);
        transaction.addToBackStack("mainFragment");
        transaction.commit();*//*
    }*/

    //private void setAboutFragment(boolean firstTime)
/*        private void setAboutFragment(){
        getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer,new AboutFragment()).addToBackStack(null).commit();
*//*        Fragment aboutFragment = new AboutFragment();
        getSupportFragmentManager().popBackStack("aboutFragment", 0);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainContainer, aboutFragment);
        if (!firstTime)
            transaction.addToBackStack("aboutFragment");
        transaction.commit();*//*
    }*/

/*    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            //openQuitDialog();
        }
    }*/



/*    private void openQuitDialog() {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(
                MainActivity.this);
        quitDialog.setTitle("Выход: Вы уверены?");
        quitDialog.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO
                finish();
            }
        });
        quitDialog.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO
            }
        });
        quitDialog.show();
    }*/

    //метод вызова pickerdate. Привязано к кнопке pickerdate


}