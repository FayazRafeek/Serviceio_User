package com.anghar.serviceio.View.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.anghar.serviceio.Model.Data.Invite;
import com.anghar.serviceio.Model.Data.Work;
import com.anghar.serviceio.R;
import com.anghar.serviceio.databinding.WorkListLayoutBinding;
import com.anghar.serviceio.databinding.WorkerListLayoutBinding;

import java.util.ArrayList;
import java.util.List;

public class WorkListAdapter extends RecyclerView.Adapter<WorkListAdapter.WorkListVH> {

    Context context;
    List<Work> works = new ArrayList<>();
    List<Invite> invites = new ArrayList<>();
    WorkClick listner;
    String type = "HOME";
    String dataType = "WORK";



    public WorkListAdapter(Context context, WorkClick listner,String type) {
        this.context = context;
        this.listner = listner;
        this.type = type;
        this.dataType = type;
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

        if(dataType.equals("INVITE")){
            Invite invite = invites.get(position);
            String title = invite.getWork().getComplaint();
            holder.binding.workTitle.setText(title);
            holder.binding.workAddress.setText("From : " + invite.getUserName());
            holder.binding.getRoot().setCardBackgroundColor(ContextCompat.getColor(context, R.color.list_color));

            holder.binding.addressIcon.setVisibility(View.GONE);
            holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listner.onInviteClick(invite);
                }
            });

        } else {
            Work item = works.get(position);
            if(type.equals("HOME")){
                holder.binding.workCategory.setVisibility(View.GONE);
            } else {
                holder.binding.workCategory.setVisibility(View.VISIBLE);
                holder.binding.workCategory.setText(item.getCategory());
            }

            String title = item.getComplaint();
            String address = item.getAddress();
            holder.binding.workTitle.setText(title);
            holder.binding.workAddress.setText(address);
            holder.binding.workCategory.setText(item.getCategory());
            holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listner.onWorkClick(item);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return dataType.equals("INVITE") ? invites.size() : works.size();
    }

    public void updateList(List<Work> items ){
        this.works = items;
        notifyDataSetChanged();
    }

    public void updateInviteList(List<Invite> items ){
        this.invites = items;
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
        void onInviteClick(Invite work);
    }
}
