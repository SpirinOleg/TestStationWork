package com.example.a123.testStation.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a123.testStation.Direction;
import com.example.a123.testStation.R;
import com.example.a123.testStation.activity.TimingActivity;
import com.example.a123.testStation.model.Station;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;


public class ScheduleFragment extends Fragment {

    public static final String FRAGMENT_TAG = "ScheduleFragment";
    public static final String KEY = "DIRECTION_EXTRA";
    public static final String EXTRA_STATION = "STATION";
    public static final String EXTRA_FLAG = "FLAG";
    private static final int DIRECTION_TYPE = 1;
    private EditText departureStation;
    private EditText arrivalStation;
    private EditText datePicker;

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setRetainInstance(false);
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        departureStation = view.findViewById(R.id.departureStationAddress);
        arrivalStation = view.findViewById(R.id.arrivalStationAddress);
        datePicker = view.findViewById(R.id.pickerDate);

        departureStation.setOnClickListener(view1 -> showStation(Direction.FROM));

        arrivalStation.setOnClickListener(view12 -> showStation(Direction.TO));

        datePicker.setOnClickListener(view13 -> selectDate());

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DIRECTION_TYPE) {
            if (resultCode == RESULT_OK) {
                Station station = data.getParcelableExtra(EXTRA_STATION);
                Direction flag = Direction.valueOf(data.getStringExtra(EXTRA_FLAG));
                if (flag == Direction.FROM) {
                    departureStation.setText(station.getStationTitle());
                } else {
                    arrivalStation.setText(station.getStationTitle());
                }
            } else {
                Toast.makeText(getContext(), R.string.msg_error_dep, Toast.LENGTH_SHORT).show();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showStation(Direction d) {
        Intent intent = new Intent(getActivity(), TimingActivity.class);
        intent.putExtra(KEY, d.name());
        startActivityForResult(intent, DIRECTION_TYPE);
    }

    private void selectDate() {
        DialogFragment dateDialog = new DatePickerFragment();
        if (getFragmentManager() != null) {
            dateDialog.show(getFragmentManager(), "datePicker");
        } else {
            Toast.makeText(getContext(), R.string.msg_error_no_date, Toast.LENGTH_LONG).show();
        }
    }

    public void setDate(Calendar calendar) {
        datePicker.setText(new SimpleDateFormat("dd MMM yyyy", new Locale("ru")).format(calendar.getTime()));
    }

}
