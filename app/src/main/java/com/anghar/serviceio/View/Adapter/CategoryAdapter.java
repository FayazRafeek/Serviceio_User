package com.anghar.serviceio.View.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.anghar.serviceio.R;
import com.anghar.serviceio.databinding.CategoryListItemBinding;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryVH> {

    List<String> categories = new ArrayList<>();
    Context context;
    CategoryAction listner;

    public CategoryAdapter(Context context, CategoryAction listner) {
        this.context = context;
        this.listner = listner;
    }

    @NonNull
    @Override
    public CategoryVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CategoryListItemBinding binding = CategoryListItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new CategoryVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryVH holder, int position) {

        String cat = categories.get(position);

        holder.binding.catTitle.setText(cat);
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listner.onCatClick(cat);
            }
        });

        switch (cat){
            case "Construction" : holder.binding.catImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.construction_image)); break;
            case "Plumbing" : holder.binding.catImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.plumbing_image)); break;
            case "Electrical" : holder.binding.catImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.electrical_image)); break;
            case "Gardening" : holder.binding.catImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.garden_iameg)); break;
            case "Cleaning" : holder.binding.catImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.cleaning_image)); break;
            case "Day Care" : holder.binding.catImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.daycare_image)); break;
        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void updateList(List<String> category){
        this.categories = category;
        notifyDataSetChanged();
    }

    public static class CategoryVH extends RecyclerView.ViewHolder{

        CategoryListItemBinding binding;

        public CategoryVH(@NonNull CategoryListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface CategoryAction {
        void onCatClick(String category);
    }
}
