package com.example.a123.teststation.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.a123.teststation.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        EditText toDate = getActivity().findViewById(R.id.pickerdate);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        toDate.setText(new SimpleDateFormat("dd MMM yyyy").format(calendar.getTime()));
    }
}
