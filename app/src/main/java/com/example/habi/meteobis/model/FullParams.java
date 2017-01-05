package com.example.habi.meteobis.model;

import org.joda.time.DateTime;

import java.util.Locale;

/**
 * Created by Gabriel Fortin
 */
public class FullParams {
    public final int row;
    public final int col;
    public final DateTime date;
    public final int interval;
    public final String baseUrl;  // OR different meteogram service

    FullParams(FullParams fp) {
        this.row = fp.row;
        this.col = fp.col;
        this.date = fp.date;
        this.interval = fp.interval;
        this.baseUrl = fp.baseUrl;
    }

    public FullParams(int row, int col, DateTime date) {
        this.row = row;
        this.col = col;
        this.date = date;
        this.interval = 0;  // TODO: use 'interval'
        this.baseUrl = null;  // TODO: use 'baseUrl'
    }

    @Override
    public String toString() {
        return String.format(Locale.UK, "(%d, %d, %s)", row, col, date.toString());
    }
}
