package com.example.a123.testStation.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Station implements Parcelable {

    public static final Parcelable.Creator<Station> CREATOR = new Parcelable.Creator<Station>() {
        @Override
        public Station createFromParcel(Parcel source) {
            return new Station(source);
        }

        @Override
        public Station[] newArray(int size) {
            return new Station[size];
        }
    };
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
    @SerializedName("stationId")
    private Integer stationId;
    @SerializedName("stationTitle")
    private String stationTitle;

    public Station() {
    }

    private Station(Parcel in) {
        this.countryTitle = in.readString();
        this.point = in.readParcelable(Point.class.getClassLoader());
        this.districtTitle = in.readString();
        this.cityId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.cityTitle = in.readString();
        this.regionTitle = in.readString();
        this.stationId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.stationTitle = in.readString();
    }

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

    public Integer getStationId() {
        return stationId;
    }

    public void setStationId(Integer stationId) {
        this.stationId = stationId;
    }

    public String getStationTitle() {
        return stationTitle;
    }

    public void setStationTitle(String stationTitle) {
        this.stationTitle = stationTitle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.countryTitle);
        dest.writeParcelable(this.point, flags);
        dest.writeString(this.districtTitle);
        dest.writeValue(this.cityId);
        dest.writeString(this.cityTitle);
        dest.writeString(this.regionTitle);
        dest.writeValue(this.stationId);
        dest.writeString(this.stationTitle);
    }
}
