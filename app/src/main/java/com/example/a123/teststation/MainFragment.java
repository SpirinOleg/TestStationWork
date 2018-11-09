package com.example.a123.teststation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import static android.app.Activity.RESULT_OK;

public class MainFragment extends Fragment {

    public static final String STATION_NAME = "stationName";
    public static final String STATION_FILE = "station";
    public static boolean DIRECTION_TYPE;
    public static final String KEY = "booleanKey";
    public static final int DIRECTION_DEP = 0;//отправление
    public static final int DIRECTION_ARR = 1;//прибытие

    EditText departureStation;
    EditText arrivalStation;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        return inflater.inflate(R.layout.main_fragment, container, false);
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

         departureStation = view.findViewById(R.id.departureStationAddress);
         arrivalStation = view.findViewById(R.id.arrivalStationAddress);



        view.findViewById(R.id.departureStationAddress).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStaionFrom(view);//вызов метода для отображения данных для станций отправления
            }
        });

        view.findViewById(R.id.arrivalStationAddress).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStaionTo(view);//вызов метода для отображения данных для станций прибытия
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            Station station =  data.getParcelableExtra("STATION");
            switch (requestCode){
                case DIRECTION_DEP:
                    departureStation.setText(station.getStationTitle());
                    break;
                case DIRECTION_ARR:
                    arrivalStation.setText(station.getStationTitle());
                    break;
                    default:
                        break;
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showStaionFrom(View view){
        Intent intent = new Intent(getActivity(),TimingFragment.class);
        intent.putExtra(KEY, DIRECTION_TYPE);
        startActivityForResult(intent, DIRECTION_DEP);

/*
        Bundle bundle = new Bundle();
        bundle.putBoolean("flag", true);

        Activity timingFragment = new TimingFragment();
        timingFragment.setArguments(bundle);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainContainer, timingFragment);

        transaction.addToBackStack(null);
        transaction.commit();*/
    }

    private void showStaionTo(View view){
        Intent intent = new Intent(getActivity(),TimingFragment.class);
        intent.putExtra(KEY, DIRECTION_TYPE);
        startActivityForResult(intent, DIRECTION_ARR);

/*        Bundle bundle = new Bundle();
        bundle.putBoolean("flag", false);

        Activity timingFragment = new TimingFragment();
        timingFragment.setArguments(bundle);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainContainer, timingFragment);

        transaction.addToBackStack(null);
        transaction.commit();*/
    }



}
