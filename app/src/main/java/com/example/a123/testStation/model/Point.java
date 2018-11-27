package com.example.a123.testStation.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Point implements Parcelable {
    public static final Parcelable.Creator<Point> CREATOR = new Parcelable.Creator<Point>() {
        @Override
        public Point createFromParcel(Parcel source) {
            return new Point(source);
        }

        @Override
        public Point[] newArray(int size) {
            return new Point[size];
        }
    };
    private Double longitude;
    private Double latitude;

    public Point() {
    }

    private Point(Parcel in) {
        this.longitude = (Double) in.readValue(Double.class.getClassLoader());
        this.latitude = (Double) in.readValue(Double.class.getClassLoader());
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.longitude);
        dest.writeValue(this.latitude);
    }
}
