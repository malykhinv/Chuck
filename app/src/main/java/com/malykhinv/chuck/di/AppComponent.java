package com.malykhinv.chuck.di;

import android.content.Context;

import com.malykhinv.chuck.api.IcndbApi;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    Context getContext();
    IcndbApi getApi();
}
