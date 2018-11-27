package com.example.a123.testStation.model;

import android.graphics.Point;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class City {

    @SerializedName("countryTitle")
    private String countryTitle;
    @SerializedName("point")
    private Point point;
    @SerializedName("districtTitle")
    private String districtTitle;
    @SerializedName("cityId")
    private Integer cityId;
    @SerializedName("cityTitle")
    private String cityTitle;
    @SerializedName("regionTitle")
    private String regionTitle;
    @SerializedName("stations")
    private List<Station> stations = null;

    public String getCountryTitle() {
        return countryTitle;
    }

    public void setCountryTitle(String countryTitle) {
        this.countryTitle = countryTitle;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public String getDistrictTitle() {
        return districtTitle;
    }

    public void setDistrictTitle(String districtTitle) {
        this.districtTitle = districtTitle;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getCityTitle() {
        return cityTitle;
    }

    public void setCityTitle(String cityTitle) {
        this.cityTitle = cityTitle;
    }

    public String getRegionTitle() {
        return regionTitle;
    }

    public void setRegionTitle(String regionTitle) {
        this.regionTitle = regionTitle;
    }

    public List<Station> getStations() {
        return stations;
    }

    public void setStations(List<Station> stations) {
        this.stations = stations;
    }

}
