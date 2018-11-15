package com.example.a123.teststation;

import com.example.a123.teststation.model.Station;

public interface OnItemRecyclerClick {
    void onClick(int position, Station station);

    void onInfoClick(Station station);
}
