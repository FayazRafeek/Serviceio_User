package com.anghar.serviceio.Model;

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
}
