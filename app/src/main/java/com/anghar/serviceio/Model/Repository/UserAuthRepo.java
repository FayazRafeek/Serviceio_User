package com.anghar.serviceio.Model.Repository;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.anghar.serviceio.BaseApplication;
import com.anghar.serviceio.Model.Data.BasicResponse;
import com.anghar.serviceio.Model.Data.User;
import com.anghar.serviceio.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserAuthRepo {

    private static final String TAG = "333 UserRepo";

    //Static Instance
    public static UserAuthRepo INSTANCE;
    public static UserAuthRepo getInstance() {
        if(INSTANCE == null) INSTANCE = new UserAuthRepo();
        return INSTANCE;
    }

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    Context context;
    public UserAuthRepo() {
        this.context = BaseApplication.getContext();
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    //REGISTER
    public LiveData<BasicResponse> startUserRegister(String name,String email,String password, String phone) {

        BasicResponse resp = new BasicResponse("LOADING");

        MutableLiveData<BasicResponse> regLive = new MutableLiveData<>(resp);

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            User brand = new User(user.getUid(),name, email,phone);

                            firestore.collection("Users")
                                    .document(user.getUid())
                                    .set(brand)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                saveAuthState(user,name);
                                                resp.setStatus("SUCCESS");
                                            } else resp.setStatus("ERROR");
                                            regLive.setValue(resp);
                                        }
                                    });

                        } else resp.setStatus("ERROR");

                        regLive.setValue(resp);
                    }
                });

        return regLive;
    }

    //EMAIL VERIFICATION (REGISTER)
    public LiveData<BasicResponse> verifyUserEmailRegister(String email, String code){

        BasicResponse resp = new BasicResponse("LOADING");
        MutableLiveData<BasicResponse> regLive = new MutableLiveData<>(resp);

        return regLive;
    }

    //LOGIN
    public LiveData<BasicResponse> startUserLogin(String email,String password) {

        BasicResponse resp = new BasicResponse("LOADING");

        MutableLiveData<BasicResponse> loginLive = new MutableLiveData<>(resp);

        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            saveAuthState(firebaseAuth.getCurrentUser(),"");
                            resp.setStatus("SUCCESS");
                        }
                        else resp.setStatus("ERROR"); resp.setError(task.getException());

                        loginLive.setValue(resp);
                    }
                });
        return loginLive;

    }



    //Save Auth State
    private Boolean saveAuthState(FirebaseUser user, String name){

        if(user != null){
            SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.AUTH_PREF_FILE),Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean(getString(R.string.IS_LOGIN_KEY), true);
            editor.putString("USER_ID_KEY",user.getUid());
            editor.putString("USER_NAME_KEY",user.getDisplayName() == null ? name : user.getDisplayName());
            editor.putString("USER_EMAIL_KEY",user.getEmail());
            editor.apply();
            return true;
        } else return false;

    }




    String getString(int id){ return  context.getString(id);}

}
