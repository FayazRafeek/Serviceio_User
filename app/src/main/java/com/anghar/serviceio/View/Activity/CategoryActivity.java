package com.anghar.serviceio.View.Activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.anghar.serviceio.databinding.ActivityCategoryBinding;

public class CategoryActivity extends AppCompatActivity {

    ActivityCategoryBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCategoryBinding.inflate(getLayoutInflater());
    }
}
