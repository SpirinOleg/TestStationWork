package com.example.a123.teststation.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.a123.teststation.OnItemRecyclerClick;
import com.example.a123.teststation.R;
import com.example.a123.teststation.model.Station;

import java.util.ArrayList;
import java.util.List;

public class TimingAdapter extends RecyclerView.Adapter<TimingAdapter.ViewHolder> implements Filterable {

    private List<Station> stations;
    private List<Station> stationsFiltred;
    private OnItemRecyclerClick listener;


    public TimingAdapter(List<Station> data, OnItemRecyclerClick listener) {
        stations = data;
        stationsFiltred = data;
        this.listener = listener;
    }


    @Override
    public TimingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TimingAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.citiesName.setText(stationsFiltred.get(position).getStationTitle());
        holder.itemView.setOnClickListener(view -> listener.onClick(position, stationsFiltred.get(position)));


        holder.infoStation.setOnClickListener(view -> listener.onInfoClick(stations.get(position)));

    }

    @Override
    public int getItemCount() {
        return stationsFiltred.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                FilterResults filterResults = new FilterResults();
                if (charString.isEmpty()) {
                    filterResults.values = stations;
                } else {
                    List<Station> filtredList = new ArrayList<>();
                    for (Station station : stations) {
                        if (station.getStationTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filtredList.add(station);
                        }
                    }
                    filterResults.values = filtredList;
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                stationsFiltred = (ArrayList<Station>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView citiesName;
        private ImageButton infoStation;

        public ViewHolder(View itemView) {
            super(itemView);
            citiesName = itemView.findViewById(R.id.stationName);
            infoStation = itemView.findViewById(R.id.image_info_station);
        }

    }
}
