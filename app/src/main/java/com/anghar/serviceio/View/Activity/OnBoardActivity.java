package com.anghar.serviceio.View.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.anghar.serviceio.View.Fragment.OnBoardInfoFragment;
import com.anghar.serviceio.databinding.ActivityOnboardBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class OnBoardActivity extends AppCompatActivity {

    ActivityOnboardBinding binding;
    OnboardStateAdapter onboardStateAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityOnboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setUpViewPager();

        binding.onboardNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextPage();
            }
        });

        binding.onboardBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.pager.setCurrentItem(binding.pager.getCurrentItem() - 1);
            }
        });


    }

    void setUpViewPager(){
        onboardStateAdapter = new OnboardStateAdapter(this);
        binding.pager.setAdapter(onboardStateAdapter);
        new TabLayoutMediator(binding.tabLayout, binding.pager, (tab, position) -> {
            tab.setText("");
        }).attach();
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) { handleTabChange(tab.getPosition());}
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    void handleTabChange(int position){
        if(position == 2) binding.onboardNextBtn.setText("LET'S GET STARTED");
        else binding.onboardNextBtn.setText("NEXT");

        if (position > 0)
            binding.onboardBackBtn.setVisibility(View.VISIBLE);
        else
            binding.onboardBackBtn.setVisibility(View.INVISIBLE);
    }


    void nextPage(){
        int curPos = binding.pager.getCurrentItem();
        if(curPos == 2){
            startActivity(new Intent(this,MainActivity.class));
            finish();
        } else
        binding.pager.setCurrentItem(++curPos);
    }


    public static class OnboardStateAdapter extends FragmentStateAdapter{

        public OnboardStateAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Fragment fragment = new OnBoardInfoFragment(position);
            return fragment;
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }

}
