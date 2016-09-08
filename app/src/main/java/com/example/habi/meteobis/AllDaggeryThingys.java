package com.example.habi.meteobis;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = MeteogramServiceModule.class)
public interface AllDaggeryThingys {
    UmMeteogramService gimmeDaService();
    void inject(MainActivity.MeteogramFragment mf);
}
