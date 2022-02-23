package com.anghar.serviceio.Viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.anghar.serviceio.Model.Data.BasicResponse;
import com.anghar.serviceio.Model.Repository.UserAuthRepo;

public class AuthViewModel extends ViewModel {

    public UserAuthRepo userAuthRepo;
    public MutableLiveData<String> AUTH_TYPE;


    public AuthViewModel() {
        if(AUTH_TYPE == null){
            AUTH_TYPE = new MutableLiveData<>("CHOOSE");
        }

        userAuthRepo = UserAuthRepo.getInstance();
    }

    public MutableLiveData<String> getAUTH_TYPE() {
        if(AUTH_TYPE == null)
            AUTH_TYPE = new MutableLiveData<>("CHOOSE");
        return AUTH_TYPE;
    }


    String email,password;
    public LiveData<BasicResponse> startRegister(String name, String email, String password, String phone){
        this.email = email;
        this.password = password;
        LiveData<BasicResponse> resp =  userAuthRepo.startUserRegister(name,email,password, phone);
        return resp;
    }

    public LiveData<BasicResponse> verifyRegsiterEmail(String code){
        LiveData<BasicResponse> resp =  userAuthRepo.verifyUserEmailRegister(email,code);
        return resp;
    }

    public LiveData<BasicResponse> startLogin(String email, String password){
        LiveData<BasicResponse> resp =  userAuthRepo.startUserLogin(email,password);
        return resp;
    }

    public LiveData<BasicResponse> startLogin(){
        LiveData<BasicResponse> resp =  userAuthRepo.startUserLogin(email,password);
        return resp;
    }



}
