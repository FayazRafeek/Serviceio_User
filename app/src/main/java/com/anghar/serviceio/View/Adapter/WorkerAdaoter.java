package com.anghar.serviceio.View.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anghar.serviceio.Model.Data.Worker;
import com.anghar.serviceio.databinding.WorkerListLayoutBinding;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class WorkerAdaoter extends RecyclerView.Adapter<WorkerAdaoter.WorkerVH> {

    Context context;
    List<Worker> workers = new ArrayList<>();

    public WorkerAdaoter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public WorkerVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        WorkerListLayoutBinding binding = WorkerListLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new WorkerVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkerVH holder, int position) {

        Worker item = workers.get(position);

        Glide.with(context)
                .load(item.getProfUrl())
                .centerCrop()
                .into(holder.binding.workerImg);


        holder.binding.workerName.setText(item.getDisplayName());
    }

    @Override
    public int getItemCount() {
        return workers.size();
    }

    public void updateList(List<Worker> worker){
        this.workers = worker;
        notifyDataSetChanged();
    }

    class WorkerVH extends RecyclerView.ViewHolder{

        WorkerListLayoutBinding binding;

        public WorkerVH(@NonNull WorkerListLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
