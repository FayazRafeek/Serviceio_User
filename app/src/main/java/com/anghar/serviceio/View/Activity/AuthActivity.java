package com.anghar.serviceio.View.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.anghar.serviceio.View.Fragment.LoginFragment;
import com.anghar.serviceio.View.Fragment.RegisterFragment;
import com.anghar.serviceio.Viewmodel.AuthViewModel;
import com.anghar.serviceio.databinding.ActivityAuthBinding;

public class AuthActivity extends AppCompatActivity {

    ActivityAuthBinding binding;
    AuthViewModel authViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Viewmodel init
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        //Viewbinding
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Button Click handlers
        binding.loginChooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authViewModel.getAUTH_TYPE().setValue("LOGIN");
            }
        });

        binding.registerChooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authViewModel.getAUTH_TYPE().setValue("REGISTER");
            }
        });
        //

        authViewModel.getAUTH_TYPE().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s.equals("CHOOSE"))
                    showChoose();
                else
                    showAuthType(s);
            }
        });


    }

    void showChoose() {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
        binding.fragmentContainer.setVisibility(View.GONE);
        binding.authChooser.setVisibility(View.VISIBLE);
    }


    void showAuthType(String type) {
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(binding.fragmentContainer.getId(),type.equals("LOGIN") ? LoginFragment.class : RegisterFragment.class, null)
                .commit();
        binding.authChooser.setVisibility(View.GONE);
        binding.fragmentContainer.setVisibility(View.VISIBLE);

    }


}
