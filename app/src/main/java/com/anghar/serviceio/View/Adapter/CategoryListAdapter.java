package com.anghar.serviceio.View.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.anghar.serviceio.Model.Data.Category;
import com.anghar.serviceio.R;
import com.anghar.serviceio.databinding.CatLayoutListBinding;
import com.anghar.serviceio.databinding.CategoryListItemBinding;

import java.util.ArrayList;
import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.CategoryListVH> {


    List<Category> categories = new ArrayList<>();
    Context context;
    CategoryAdapter.CategoryAction listner;

    public CategoryListAdapter(Context context, CategoryAdapter.CategoryAction listner) {
        this.context = context;
        this.listner = listner;
    }

    @NonNull
    @Override
    public CategoryListVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CatLayoutListBinding binding = CatLayoutListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new CategoryListVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryListVH holder, int position) {

        Category cat = categories.get(position);

        holder.binding.title.setText(cat.getTitle());
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listner.onCatClick(cat);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void updateList(List<Category> category){
        this.categories = category;
        notifyDataSetChanged();
    }

    public static class CategoryListVH extends RecyclerView.ViewHolder{

        CatLayoutListBinding binding;

        public CategoryListVH(@NonNull CatLayoutListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
