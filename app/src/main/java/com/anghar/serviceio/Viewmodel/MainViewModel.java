package com.anghar.serviceio.Viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    public MainViewModel() {
    }

    MutableLiveData<Integer> homeScreenLive = new MutableLiveData<>(1);

    public LiveData<Integer> getHomeScreenLive() {
        return homeScreenLive;
    }

    public void setHomeScreenLive(Integer homeScreenLive) {
        this.homeScreenLive.setValue(homeScreenLive);
    }
}
