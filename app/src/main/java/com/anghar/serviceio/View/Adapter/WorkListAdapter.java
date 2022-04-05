package com.anghar.serviceio.View.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.anghar.serviceio.Model.Data.Work;
import com.anghar.serviceio.databinding.WorkListLayoutBinding;
import com.anghar.serviceio.databinding.WorkerListLayoutBinding;

import java.util.ArrayList;
import java.util.List;

public class WorkListAdapter extends RecyclerView.Adapter<WorkListAdapter.WorkListVH> {

    Context context;
    List<Work> works = new ArrayList<>();
    WorkClick listner;
    String type = "HOME";

    public WorkListAdapter(Context context, WorkClick listner,String type) {
        this.context = context;
        this.listner = listner;
        this.type = type;
    }

    public WorkListAdapter(Context context, WorkClick listner) {
        this.context = context;
        this.listner = listner;
    }

    @NonNull
    @Override
    public WorkListVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        WorkListLayoutBinding binding = WorkListLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new WorkListVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkListVH holder, int position) {

        Work item = works.get(position);
        holder.binding.workTitle.setText(item.getComplaint());
        holder.binding.workAddress.setText(item.getAddress());

        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listner.onWorkClick(item);
            }
        });

        if(type.equals("HOME")){
            holder.binding.workCategory.setVisibility(View.GONE);
        } else {
            holder.binding.workCategory.setVisibility(View.VISIBLE);
            holder.binding.workCategory.setText(item.getCategory());
        }
    }

    @Override
    public int getItemCount() {
        return works.size();
    }

    public void updateList(List<Work> items ){
        this.works = items;
        notifyDataSetChanged();
    }

    class WorkListVH extends RecyclerView.ViewHolder {

        WorkListLayoutBinding binding;

        public WorkListVH(@NonNull WorkListLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface WorkClick {
        void onWorkClick(Work work);
    }
}
