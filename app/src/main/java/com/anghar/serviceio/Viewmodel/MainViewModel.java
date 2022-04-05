package com.anghar.serviceio.Viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.anghar.serviceio.Model.Data.BasicResponse;
import com.anghar.serviceio.Model.Repository.UserDataRepo;

public class MainViewModel extends ViewModel {

    UserDataRepo userDataRepo;
    public MainViewModel() {
        userDataRepo = UserDataRepo.getInstance();
    }

    MutableLiveData<Integer> homeScreenLive = new MutableLiveData<>(1);

    public LiveData<Integer> getHomeScreenLive() {
        return homeScreenLive;
    }

    public void setHomeScreenLive(Integer homeScreenLive) {
        this.homeScreenLive.setValue(homeScreenLive);
    }


    //Fetch User Profile info
    public LiveData<BasicResponse> fetchUserData(){
        return userDataRepo.fetchUserData();
    }

    public LiveData<BasicResponse> fetchWorkerData(){
        return userDataRepo.fetchWorkerData();
    }
}
