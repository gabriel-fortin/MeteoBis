package com.example.habi.meteobis;

import com.squareup.okhttp.ResponseBody;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

public interface UmMeteogramService {
    @GET("um/metco/mgram_pict.php?ntype=0u&lang=pl")
    Observable<byte[]> getByDate(@Query("fdate") String date,
                                            @Query("col") int col,
                                            @Query("row") int row);

    // aa… zostawię bo szkoda mi usuwać
//    Observable<ImgData> getByDate(@Query("fdate") String date);
//    Call<ResponseBody> getByDate(@Query("fdate") String date);
}
