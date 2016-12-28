package com.example.habi.meteobis.network;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

public interface UmMeteogramRetrofitService {
    @GET("um/metco/mgram_pict.php?ntype=0u&lang=pl")
    Observable<byte[]> getByDate(@Query("fdate") String date,
                                            @Query("col") int col,
                                            @Query("row") int row);

}
