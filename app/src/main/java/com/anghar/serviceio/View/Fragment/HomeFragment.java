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

import com.anghar.serviceio.View.Activity.CategoryActivity;
import com.anghar.serviceio.View.Adapter.CategoryAdapter;
import com.anghar.serviceio.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements CategoryAdapter.CategoryAction {

    FragmentHomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpCategiry();
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
}
