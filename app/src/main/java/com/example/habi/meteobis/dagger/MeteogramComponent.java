package com.example.habi.meteobis.dagger;

import com.example.habi.meteobis.meteogram.MeteoFragment;
import com.example.habi.meteobis.mvp.MeteogramPresenter;
import com.example.habi.meteobis.network.UmMeteogramRetrofitService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        RepositoryServiceModule.class,
        MeteogramPresenterModule.class,
        LocationModule.class })
public interface MeteogramComponent {
    UmMeteogramRetrofitService provideUmMeteogramService();
    MeteogramPresenter provideMeteogramPresenter();

    void inject(MeteoFragment mf);
}
