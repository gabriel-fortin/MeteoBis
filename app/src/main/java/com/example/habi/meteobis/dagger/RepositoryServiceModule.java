package com.example.habi.meteobis.dagger;

import com.example.habi.meteobis.main.Config;
import com.example.habi.meteobis.network.ByteArrayConverterFactory;
import com.example.habi.meteobis.network.UmMeteogramRetrofitService;

import dagger.Module;
import dagger.Provides;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

@Module
public class RepositoryServiceModule {

    @Provides
    static UmMeteogramRetrofitService provideUmMeteogramService() {
        return new Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .addConverterFactory(new ByteArrayConverterFactory())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
                .create(UmMeteogramRetrofitService.class);
    }
}
