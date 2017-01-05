package com.example.habi.meteobis.location;

import com.example.habi.meteobis.model.LocationParams;

/**
 * Created by Gabriel Fortin
 */

public interface LocationConsumer {
    void consume(LocationParams locationParam);
}
