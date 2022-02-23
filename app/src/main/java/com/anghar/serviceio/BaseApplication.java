package com.anghar.serviceio;

import android.app.Application;

public class BaseApplication extends Application {

    private static BaseApplication context;
    public static BaseApplication getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;
    }
}
