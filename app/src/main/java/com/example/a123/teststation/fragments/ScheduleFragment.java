package com.example.a123.teststation.fragments;

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

import com.example.a123.teststation.R;
import com.example.a123.teststation.activity.TimingActivity;
import com.example.a123.teststation.model.Station;

import static android.app.Activity.RESULT_OK;


public class ScheduleFragment extends Fragment {

    public static final String KEY = "DIRECTION_EXTRA";
    public static final int DIRECTION_DEP = 0;//отправление
    public static final int DIRECTION_ARR = 1;//прибытие
    public static int DIRECTION_TYPE = 1;

    private EditText departureStation;
    private EditText arrivalStation;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        departureStation = view.findViewById(R.id.departureStationAddress);
        arrivalStation = view.findViewById(R.id.arrivalStationAddress);
        EditText datePicker = view.findViewById(R.id.pickerdate);


        departureStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStation(view, 0);
            }
        });

        arrivalStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStation(view, 1);
            }
        });

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDate(view);
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DIRECTION_TYPE) {
            if (resultCode == RESULT_OK) {
                Station station = data.getParcelableExtra("STATION");
                boolean flag = data.getBooleanExtra("flag", false);
                if (flag) {
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

    private void showStation(View view, int flag) {
        Intent intent = new Intent(getActivity(), TimingActivity.class);
        if (flag == 0) {
            intent.putExtra(KEY, DIRECTION_DEP);
        } else {
            intent.putExtra(KEY, DIRECTION_ARR);
        }
        startActivityForResult(intent, DIRECTION_TYPE);
    }

    public void selectDate(View view) {
        DialogFragment dateDialog = new DatePickerFragment();
        dateDialog.show(getFragmentManager(), "datePicker");
    }

}
