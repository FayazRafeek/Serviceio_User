package com.anghar.serviceio.View.Activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.anghar.serviceio.R;
import com.anghar.serviceio.View.Fragment.HomeFragment;
import com.anghar.serviceio.View.Fragment.MessageFragment;
import com.anghar.serviceio.View.Fragment.ProfileFragment;
import com.anghar.serviceio.Viewmodel.MainViewModel;
import com.anghar.serviceio.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    MainViewModel mainViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setUpBottomNav();

        mainViewModel.getHomeScreenLive().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                changeFragment(integer);
            }
        });
    }


    void setUpBottomNav(){

        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home: changeFragment(1); break;
                    case R.id.nav_message: changeFragment(0); break;
                    case R.id.nav_profile: changeFragment(2); break;
                }
                return true;
            }
        });
    }

    HomeFragment homeFragment;
    void changeFragment(int pos){

        if(homeFragment == null)
            homeFragment = new HomeFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(binding.homeFragContainer.getId(),pos == 1? homeFragment : pos == 0 ? new MessageFragment() : new ProfileFragment())
                .commit();


    }


}
