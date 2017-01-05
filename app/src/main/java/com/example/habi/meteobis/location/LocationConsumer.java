package com.example.habi.meteobis.location;

import com.example.habi.meteobis.model.LocationParam;

/**
 * Created by Gabriel Fortin
 */

public interface LocationConsumer {
    void consume(LocationParam locationParam);
}
