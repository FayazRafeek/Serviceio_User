package com.anghar.serviceio.Model.Repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.anghar.serviceio.BaseApplication;
import com.anghar.serviceio.Model.Data.BasicResponse;
import com.anghar.serviceio.Model.Data.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserDataRepo {


    //Static Instance
    public static UserDataRepo INSTANCE;
    public static UserDataRepo getInstance() {
        if(INSTANCE == null) INSTANCE = new UserDataRepo();
        return INSTANCE;
    }

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    Context context;
    public UserDataRepo() {
        this.context = BaseApplication.getContext();
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }


    //Fetch User Data
    public LiveData<BasicResponse> fetchUserData(){
        String userId = firebaseAuth.getUid();
        if(userId == null) return null;

        MutableLiveData<BasicResponse> live = new MutableLiveData<>();
        BasicResponse resp = new BasicResponse("LOADING");
        live.setValue(resp);

        firestore
                .collection("Users")
                .document(userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(task.isSuccessful()){
                            User user = task.getResult().toObject(User.class);
                            resp.setData(user);
                            resp.setStatus("SUCCESS");
                        } else { resp.setStatus("ERROR"); resp.setError(task.getException());}

                        live.setValue(resp);
                    }
                });

        return live;
    }
}
