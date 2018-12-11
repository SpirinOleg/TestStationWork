package com.example.a123.testStation.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.a123.testStation.OnItemRecyclerClick;
import com.example.a123.testStation.R;
import com.example.a123.testStation.model.Station;

import java.util.ArrayList;
import java.util.List;

public class TimingAdapter extends RecyclerView.Adapter<TimingAdapter.ViewHolder> implements Filterable {

    private final OnItemRecyclerClick listener;
    private List<Station> stations;
    private List<Station> stationsFilter;


    public TimingAdapter(OnItemRecyclerClick listener) {
        stations = new ArrayList<>();
        stationsFilter = new ArrayList<>();
        this.listener = listener;
    }

    public void setData(List<Station> data) {
        stations = data;
        stationsFilter = data;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public TimingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TimingAdapter.ViewHolder holder, final int position) {
        holder.citiesName.setText(stationsFilter.get(position).getStationTitle());
        holder.itemView.setOnClickListener(view -> listener.onClick(stationsFilter.get(position)));


        holder.infoStation.setOnClickListener(view -> listener.onInfoClick(stations.get(position)));

    }

    @Override
    public int getItemCount() {
        return stationsFilter.size();
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
                    List<Station> filterList = new ArrayList<>();
                    for (Station station : stations) {
                        if (station.getStationTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filterList.add(station);
                        }
                    }
                    filterResults.values = filterList;
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                stationsFilter = toListIsStations(filterResults.values);
                notifyDataSetChanged();
            }

            private List<Station> toListIsStations(@Nullable Object object) {
                List<Station> stationList = new ArrayList<>();
                if (object instanceof List) {
                    List stations = (List) object;
                    for (int i = 0; i < stations.size(); i++) {
                        if (stations.get(i) instanceof Station) {
                            stationList.add((Station) stations.get(i));
                        } else {
                            return new ArrayList<>();
                        }
                    }
                    return stationList;
                }
                return new ArrayList<>();
            }
        };
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView citiesName;
        private final ImageButton infoStation;

        private ViewHolder(View itemView) {
            super(itemView);
            citiesName = itemView.findViewById(R.id.stationName);
            infoStation = itemView.findViewById(R.id.image_info_station);
        }

    }
}
