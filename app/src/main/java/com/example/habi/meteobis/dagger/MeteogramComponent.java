package com.example.habi.meteobis.dagger;

import com.example.habi.meteobis.meteogram.MeteoFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        RepositoryServiceModule.class,
        MeteogramPresenterModule.class,
        LocationModule.class
})
public interface MeteogramComponent {
    void inject(MeteoFragment mf);
}
