package com.example.habi.meteobis.dagger;

import com.example.habi.meteobis.MainActivity;
import com.example.habi.meteobis.network.ToByteArrayConverterFactory;
import com.example.habi.meteobis.network.UmMeteogramService;

import dagger.Module;
import dagger.Provides;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

@Module
public class MeteogramServiceModule {

    @Provides
    static UmMeteogramService provideUmMeteogramService() {
        return new Retrofit.Builder()
                .baseUrl(MainActivity.BASE_URL)
                .addConverterFactory(new ToByteArrayConverterFactory())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
                .create(UmMeteogramService.class);
    }
}
