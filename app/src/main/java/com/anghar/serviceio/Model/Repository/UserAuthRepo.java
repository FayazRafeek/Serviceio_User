package com.anghar.serviceio.Model.Repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

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
                                                saveAuthState(brand);
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


    //LOGIN
    public LiveData<BasicResponse> startUserLogin(String email,String password) {

        BasicResponse resp = new BasicResponse("LOADING");

        Log.d(TAG, "startUserLogin: Called");
        MutableLiveData<BasicResponse> loginLive = new MutableLiveData<>(resp);

        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "****    onComplete: Login success " + task.isSuccessful());
                        if(task.isSuccessful()){

                            firestore.collection("Users")
                                    .document(firebaseAuth.getCurrentUser().getUid())
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            Log.d(TAG, "onComplete: Login full complete ");
                                            if(task.isSuccessful()){
                                                saveAuthState(task.getResult().toObject(User.class));
                                                resp.setStatus("SUCCESS");
                                            } else {
                                                resp.setStatus("ERROR"); resp.setError(task.getException());
                                            }
                                            loginLive.setValue(resp);
                                        }
                                    });
                        }
                        else resp.setStatus("ERROR"); resp.setError(task.getException());

                        loginLive.setValue(resp);
                    }
                });
        return loginLive;

    }



    //Save Auth State
    private Boolean saveAuthState(User user){
        if(user != null){
            Log.d(TAG, "onComplete: Saving auth state");
            SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.AUTH_PREF_FILE),Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean(getString(R.string.IS_LOGIN_KEY), true);
            Gson gson = new Gson();
            String json = gson.toJson(user);
            editor.putString("USER_ID_KEY",user.getuId());
            editor.putString("USER_DATA",json);
            editor.commit();
            return true;
        } else return false;

    }


    String getString(int id){ return  context.getString(id);}

}
