package com.example.habi.meteobis.model;

public class LocationParam {
    public final int row;
    public final int col;

    public LocationParam(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public static LocationParam KRAKOW = new LocationParam(466, 232);

}
