package com.example.habi.meteobis.model;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.Locale;

/**
 * Created by Gabriel Fortin
 */
public class FullParams {
    public final int row;
    public final int col;
    public final DateTime date;
    public final ForecastModel model;

    FullParams(FullParams fp) {
        this.row = fp.row;
        this.col = fp.col;
        this.date = fp.date;
        this.model = fp.model;
    }

    public FullParams(int row, int col, DateTime date, ForecastModel model) {
        this.row = row;
        this.col = col;
        this.date = date;
        this.model = model;
    }

    @Override
    public String toString() {
        return String.format(Locale.UK,
                "(%s, %d, %d, %s)",
                model,
                row,
                col,
                DateTimeFormat.shortDateTime().print(date)
        );
    }
}
