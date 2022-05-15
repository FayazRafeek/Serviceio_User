package com.anghar.serviceio.View.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.anghar.serviceio.Model.AppSingleton;
import com.anghar.serviceio.Model.Data.BasicResponse;
import com.anghar.serviceio.Model.Data.User;
import com.anghar.serviceio.Model.Data.Worker;
import com.anghar.serviceio.R;
import com.anghar.serviceio.View.Activity.AuthActivity;
import com.anghar.serviceio.View.Activity.MainActivity;
import com.anghar.serviceio.Viewmodel.MainViewModel;
import com.anghar.serviceio.databinding.FragmentProfileBinding;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;
    MainViewModel mainViewModel;
    User user; Worker worker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);

        userType = AppSingleton.getINSTANCE().getUserType();
        if(userType.equals("USER")){

            binding.category.setVisibility(View.GONE);
            binding.userType.setVisibility(View.GONE);
            binding.bio.setVisibility(View.GONE);
            binding.categoryTitle.setText("User Profile");
            mainViewModel.fetchUserData().observe(getViewLifecycleOwner(),
                    new Observer<BasicResponse>() {
                        @Override
                        public void onChanged(BasicResponse basicResponse) {
                            switch (basicResponse.getStatus()){
                                case "LOADING" : showLoading(); break;
                                case "SUCCESS" : user =(User)basicResponse.getData(); updateUi(); break;
                                case "ERROR" : showError(basicResponse.getError()); break;
                            }
                        }
                    });
        } else {

            binding.category.setVisibility(View.VISIBLE);
            binding.userType.setVisibility(View.GONE);
            binding.categoryTitle.setText("Worker Profile");
            mainViewModel.fetchWorkerData().observe(getViewLifecycleOwner(),
                    new Observer<BasicResponse>() {
                        @Override
                        public void onChanged(BasicResponse basicResponse) {
                            switch (basicResponse.getStatus()){
                                case "LOADING" : showLoading(); break;
                                case "SUCCESS" : worker =(Worker) basicResponse.getData(); updateUi(); break;
                                case "ERROR" : showError(basicResponse.getError()); break;
                            }
                        }
                    });
        }




        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLogout();
            }
        });

        binding.switchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSwitching();
            }
        });
    }


    void showLoading(){
        binding.profileProgress.setVisibility(View.VISIBLE);
        binding.dataParent.setVisibility(View.GONE);
        binding.switchBtn.setVisibility(View.GONE);
    }
    void hideLoading(){
        binding.profileProgress.setVisibility(View.GONE);
        binding.dataParent.setVisibility(View.VISIBLE);
    }

    String userType = "USER";
    void updateUi(){
        hideLoading();

        if(userType.equals("USER")){
            if(user != null){
                binding.userName.setText(user.getName());
                binding.userEmail.setText(user.getEmail());
                binding.userPhone.setText(user.getPhone());

                binding.switchBtn.setVisibility(View.VISIBLE);

                userType = AppSingleton.getINSTANCE().getUserType();
                user.setUserType(userType);

                binding.userType.setText("User Type : " + user.getUserType());
                binding.switchBtn.setText("SWITCH TO WORKER");
            }

        } else {

            if(worker != null){
                binding.userName.setText(worker.getDisplayName());
                binding.userPhone.setText(worker.getPhone());
                binding.userEmail.setText(worker.getWebsite());
                binding.switchBtn.setVisibility(View.VISIBLE);
                Glide.with(getActivity())
                        .load(worker.getProfUrl())
                        .centerCrop()
                        .into(binding.profileImage);


                binding.userType.setVisibility(View.GONE);
                binding.userPhone.setVisibility(View.GONE);
                binding.bio.setText(worker.getBio());
                binding.category.setText(worker.getCategory());
                binding.switchBtn.setText("SWITCH TO USER");
            }

        }



    }

    void showError(Throwable error){
        hideLoading();
        Toast.makeText(getContext(), "Failed to get user!", Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
    }


    void startLogout(){
        FirebaseAuth.getInstance().signOut();
        SharedPreferences pref = getContext().getSharedPreferences(getContext().getString(R.string.AUTH_PREF_FILE), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(getString(R.string.IS_LOGIN_KEY), false);
        editor.apply();

        startActivity(new Intent(getActivity(), AuthActivity.class));
        getActivity().finish();
    }

    void startSwitching(){
        userType = AppSingleton.getINSTANCE().getUserType();
        if(userType.equals("USER")){
            AppSingleton.getINSTANCE().setUserType("WORKER");
            Toast.makeText(getActivity(), "Switching user to worker...", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), "Switching successfull", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), MainActivity.class)); getActivity().finish();
                }
            },2000);

        } else {
            AppSingleton.getINSTANCE().setUserType("USER");
            Toast.makeText(getActivity(), "Switching worker to user...", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), "Switching successfull", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), MainActivity.class)); getActivity().finish();
                }
            },2000);
        }
        userType = AppSingleton.getINSTANCE().getUserType();
        updateUi();
    }

}
