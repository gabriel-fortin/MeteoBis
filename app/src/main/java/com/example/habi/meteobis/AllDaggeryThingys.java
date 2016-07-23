package com.example.habi.meteobis;

import dagger.Component;

@Component(modules = MeteogramServiceModule.class)
public interface AllDaggeryThingys {
    UmMeteogramService gimmeDaService();
    void inject(MainActivity.MeteogramFragment mf);
}
