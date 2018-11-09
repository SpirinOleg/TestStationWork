package com.example.a123.teststation;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AboutFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,@Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        TextView tvInfo = (TextView) view.findViewById(R.id.about_fragment);
        tvInfo.setText("Версия приложения: " + BuildConfig.VERSION_NAME);
    }
}
