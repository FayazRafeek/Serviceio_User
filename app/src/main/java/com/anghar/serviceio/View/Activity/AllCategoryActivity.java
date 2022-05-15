package com.anghar.serviceio.View.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.anghar.serviceio.Model.AppSingleton;
import com.anghar.serviceio.Model.Data.Category;
import com.anghar.serviceio.View.Adapter.CategoryAdapter;
import com.anghar.serviceio.View.Adapter.CategoryListAdapter;
import com.anghar.serviceio.databinding.ActivityAllCategoryBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AllCategoryActivity extends AppCompatActivity implements CategoryAdapter.CategoryAction {

    ActivityAllCategoryBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAllCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fetchcategory();

        binding.swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchcategory();
            }
        });
    }

    void fetchcategory(){
        binding.swipe.setRefreshing(true);
        FirebaseFirestore.getInstance().collection("Category")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        binding.swipe.setRefreshing(false);
                        if(task.isSuccessful()){
                            List<Category> categories = new ArrayList<>();
                            for(DocumentSnapshot d : task.getResult())
                                categories.add(d.toObject(Category.class));

                            updateReycler(categories);
                        }
                    }
                });
    }

    CategoryListAdapter categoryListAdapter;
    void updateReycler(List<Category> list){
        if(categoryListAdapter == null){
            categoryListAdapter = new CategoryListAdapter(this,this);
            binding.cat.setLayoutManager(new LinearLayoutManager(this));
            binding.cat.setAdapter(categoryListAdapter);
        }

        categoryListAdapter.updateList(list);
    }

    @Override
    public void onCatClick(Category category) {
        Intent intent = new Intent(this, CategoryActivity.class);
        AppSingleton.getINSTANCE().setSelectedCategory(category);
        intent.putExtra("CATEGORY",category.getTitle());
        startActivity(intent);
    }
}
