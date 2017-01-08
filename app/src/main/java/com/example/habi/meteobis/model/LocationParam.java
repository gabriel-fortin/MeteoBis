package com.example.habi.meteobis.model;

import java.util.Locale;

public class LocationParam {
    public final int row;
    public final int col;

    public LocationParam(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public String toString() {
        return String.format(Locale.UK, "(%d, %d)", row, col);
    }

    public static LocationParam KRAKOW = new LocationParam(466, 232);

}
