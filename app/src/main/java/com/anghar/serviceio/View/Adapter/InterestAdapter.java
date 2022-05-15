package com.anghar.serviceio.View.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anghar.serviceio.Model.Data.WorkInterest;
import com.anghar.serviceio.databinding.InterestListItemBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InterestAdapter extends RecyclerView.Adapter<InterestAdapter.InterestVH> {

    Context context;
    OnInterestAction listner;

    public InterestAdapter(Context context, OnInterestAction listner) {
        this.context = context;
        this.listner = listner;
    }

    List<WorkInterest> workInterests = new ArrayList<>();

    public InterestAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public InterestVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        InterestListItemBinding binding = InterestListItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new InterestVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull InterestVH holder, int position) {

        WorkInterest item = workInterests.get(position);

        holder.binding.fromTxt.setText(item.getWorker().getDisplayName());
        holder.binding.propTxt.setText(item.getProposal());
        holder.binding.statusTxt.setText(item.getStatus());
        Date date = new Date();
        date.setTime(item.getTs());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String postedDate = sdf.format(date);
        holder.binding.dateTxt.setText("Sent on : " + postedDate);

        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listner.onInterestClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return workInterests.size();
    }

    public void updateLis(List<WorkInterest> list){
        workInterests = list;
        notifyDataSetChanged();
    }

    class InterestVH extends RecyclerView.ViewHolder{

        InterestListItemBinding binding;
        public InterestVH(@NonNull InterestListItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

    public interface OnInterestAction {
        void onInterestClick(WorkInterest item);
    }
}
