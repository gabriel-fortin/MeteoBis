package com.example.habi.meteobis.dagger;

import com.example.habi.meteobis.MainActivity;
import com.example.habi.meteobis.mvp.MeteogramPresenter;
import com.example.habi.meteobis.network.UmMeteogramService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {MeteogramServiceModule.class, MeteogramPresenterModule.class})
public interface MeteogramComponent {
    UmMeteogramService provideUmMeteogramService();
    MeteogramPresenter provideMeteogramPresenter();
    void inject(MainActivity.MeteogramFragment mf);
}
