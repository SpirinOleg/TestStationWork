package com.example.a123.testStation.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;


public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Nullable
    private OnDatePickerListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDatePickerListener) {
            listener = (OnDatePickerListener) context;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(requireContext(), this, year, month, day);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (getActivity() == null) return;
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        if (listener != null) {
            listener.onDateChanged(calendar);
        }
    }

    public interface OnDatePickerListener {
        void onDateChanged(Calendar calendar);
    }
}
