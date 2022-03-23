package com.anghar.serviceio.View.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.anghar.serviceio.Model.AppSingleton;
import com.anghar.serviceio.Model.Data.User;
import com.anghar.serviceio.Model.Data.Worker;
import com.anghar.serviceio.View.Activity.CategoryActivity;
import com.anghar.serviceio.View.Activity.WorkerProfileCreateActivity;
import com.anghar.serviceio.View.Adapter.CategoryAdapter;
import com.anghar.serviceio.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements CategoryAdapter.CategoryAction {

    FragmentHomeBinding binding;
    String userType;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userType = AppSingleton.getINSTANCE().getUserType();

        if(userType.equals("USER")){
            checkUserProfileStatus();
            setUpCategiry();
        } else {
            checkWorkerProfileStatus();
        }


        binding.incompParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userType.equals("WORKER")){
                    startActivity(new Intent(getActivity(), WorkerProfileCreateActivity.class));
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if(userType.equals("USER")){
            checkUserProfileStatus();
        } else {
            checkWorkerProfileStatus();
        }

    }

    CategoryAdapter categoryAdapter;
    void setUpCategiry(){

        if(categoryAdapter == null){
            categoryAdapter = new CategoryAdapter(getContext(), this);
            binding.categoryRecycler.setAdapter(categoryAdapter);
            binding.categoryRecycler.setLayoutManager(new GridLayoutManager(getContext(),2));
        }

        List<String>  categories = new ArrayList<>();
        categories.add("Electrical");
        categories.add("Plumbing");
        categories.add("Construction");
        categories.add("Gardening");
        categories.add("Cleaning");
        categories.add("Day Care");

        categoryAdapter.updateList(categories);
    }


    @Override
    public void onCatClick(String category) {
        Intent intent = new Intent(getActivity(), CategoryActivity.class);
        intent.putExtra("CATEGORY",category);
        startActivity(intent);
    }


    User user;
    Worker worker;
    void checkUserProfileStatus(){

        String userId = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore.getInstance()
                .collection("Users")
                .document(userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful() && task.getResult().exists()){
                            user = task.getResult().toObject(User.class);
//                            saveUserLocally(user);

                            showUserWelcomeUi();
                        } else {
                            showUserProfIncpUi();
                        }
                    }
                });

    }


    void showUserWelcomeUi(){
        if(user == null && worker == null) return;
        if(userType.equals("USER"))
            binding.welName.setText(user.getName() + ",");
        else
            binding.welName.setText(worker.getDisplayName() + ",");
        binding.userWelcomeParent.setVisibility(View.VISIBLE);
        binding.incompParent.setVisibility(View.GONE);
    }


    void showUserProfIncpUi(){
        binding.incompParent.setVisibility(View.VISIBLE);
        binding.userWelcomeParent.setVisibility(View.GONE);
    }

    //


    void checkWorkerProfileStatus(){

        String userId = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore.getInstance()
                .collection("Workers")
                .document(userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful() && task.getResult().exists()){
                            worker = task.getResult().toObject(Worker.class);
//                            saveUserLocally(user);
                            showUserWelcomeUi();
                        } else {
                            showUserProfIncpUi();
                        }
                    }
                });
    }

}
