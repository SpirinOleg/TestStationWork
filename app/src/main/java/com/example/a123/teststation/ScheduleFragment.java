package com.example.a123.teststation;

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

import static android.app.Activity.RESULT_OK;


public class ScheduleFragment extends Fragment {

    public static int DIRECTION_TYPE = 1;
    public static final String KEY = "DIRECTION_EXTRA";
    public static final int DIRECTION_DEP = 0;//отправление
    public static final int DIRECTION_ARR = 1;//прибытие
    public static final String MSG = "";

    private EditText departureStation;
    private EditText arrivalStation;
    private EditText datePicker;


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
         datePicker = view.findViewById(R.id.pickerdate);



        departureStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStaionFrom(view);//вызов метода для отображения данных для станций отправления
            }
        });

        arrivalStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStaionTo(view);//вызов метода для отображения данных для станций прибытия
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
//        if(requestCode == DIRECTION_TYPE ){
//            if(resultCode == RESULT_OK){
//                Station station = data.getParcelableExtra("STATION");
//                departureStation.setText(station.getStationTitle());
//            } else {
//                Toast.makeText(getContext(), R.string.msg_error_dep, Toast.LENGTH_SHORT).show();
//            }
//
//        }


        if(resultCode == RESULT_OK){
            Station station =  data.getParcelableExtra("STATION");
            switch (requestCode){
                case DIRECTION_DEP: {
                    departureStation.setText(station.getStationTitle());
                    break;
                }
                case DIRECTION_ARR: {
                    arrivalStation.setText(station.getStationTitle());
                    break;
                }

                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

/*    private void showStation(View view){
        Intent intent = new Intent(getActivity(),TimingActivity.class);
        //intent.putExtra(KEY, DIRECTION_TYPE);
        startActivityForResult(intent, DIRECTION_TYPE);
    }*/

    private void showStaionFrom(View view){
        Intent intent = new Intent(getActivity(),TimingActivity.class);
        intent.putExtra(KEY, DIRECTION_DEP);
        startActivityForResult(intent, DIRECTION_DEP);

    }

    private void showStaionTo(View view){
        Intent intent = new Intent(getActivity(),TimingActivity.class);
        intent.putExtra(KEY, DIRECTION_ARR);
        startActivityForResult(intent, DIRECTION_ARR);

    }

    public void selectDate(View view) {
        DialogFragment dateDialog = new DatePickerFragment();
        dateDialog.show(getFragmentManager(), "datePicker");
    }

}
