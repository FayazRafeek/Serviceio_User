package com.anghar.serviceio.View.Activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.anghar.serviceio.databinding.ActivityWorkerDetailBinding;

public class WorkDetailActivity extends AppCompatActivity {


    ActivityWorkerDetailBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityWorkerDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}
