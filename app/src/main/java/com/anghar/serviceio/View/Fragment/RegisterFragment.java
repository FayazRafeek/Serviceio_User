package com.anghar.serviceio.View.Fragment;

import android.content.Intent;
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

import com.anghar.serviceio.Model.Data.BasicResponse;
import com.anghar.serviceio.View.Activity.OnBoardActivity;
import com.anghar.serviceio.Viewmodel.AuthViewModel;
import com.anghar.serviceio.databinding.FragmentRegisterBinding;

public class RegisterFragment extends Fragment {

    FragmentRegisterBinding binding;
    AuthViewModel authViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);


        // Button Click Handles
        binding.loginSwitchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authViewModel.getAUTH_TYPE().setValue("LOGIN");
            }
        });

        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //:TODO Ui Presentation
                startRegister();
            }
        });

    }


    String name,email,password,phone;
    void startRegister() {

        showLoading();

        name = binding.regNameInp.getText().toString();
        email = binding.regEmailInp.getText().toString();
        phone = binding.regPhoneInp.getText().toString();
        password = binding.regPassInp.getText().toString();

        //:TODO Input Validation
        authViewModel.startRegister(name,email,password,phone)
                .observe(this, new Observer<BasicResponse>() {
                    @Override
                    public void onChanged(BasicResponse basicResponse) {
                        switch (basicResponse.getStatus()){
                            case "LOADING" :
                                showLoading();
                                break;
                            case "SUCCESS" :
                                hideLoading();
                                Toast.makeText(getActivity(), "Registration was successfull", Toast.LENGTH_SHORT).show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        startOnBoardProcess();
                                    }
                                },600);
                                break;
                            case "ERROR" :
                                hideLoading();
                                Toast.makeText(getActivity(), "Registration has failed", Toast.LENGTH_SHORT).show();
                                Toast.makeText(getActivity(), basicResponse.getError().getMessage(), Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });

    }

    void startOnBoardProcess(){
        Intent intent = new Intent(getActivity(), OnBoardActivity.class);
        startActivity(intent);
        getActivity().finish();
    }


    void showLoading() { binding.registerProgress.setVisibility(View.VISIBLE); }
    void hideLoading() { binding.registerProgress.setVisibility(View.GONE); }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
