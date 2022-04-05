package com.anghar.serviceio.Model;

import com.anghar.serviceio.Model.Data.Worker;

public class AppSingleton {

    public static AppSingleton INSTANCE;

    public static AppSingleton getINSTANCE() {
        if(INSTANCE == null) INSTANCE = new AppSingleton();
        return INSTANCE;
    }


    String userType = "USER";

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserType() {
        return userType;
    }

    Worker selectedWorker;

    public Worker getSelectedWorker() {
        return selectedWorker;
    }

    public void setSelectedWorker(Worker selectedWorker) {
        this.selectedWorker = selectedWorker;
    }
}
