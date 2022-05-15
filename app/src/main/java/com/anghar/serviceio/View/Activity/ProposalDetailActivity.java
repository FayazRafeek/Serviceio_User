package com.anghar.serviceio.View.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.anghar.serviceio.Model.AppSingleton;
import com.anghar.serviceio.Model.Data.WorkInterest;
import com.anghar.serviceio.databinding.ActivityProposalDetailBinding;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class ProposalDetailActivity extends AppCompatActivity {

    ActivityProposalDetailBinding binding;
    WorkInterest workInterest;
    String userType = "USER";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProposalDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        workInterest = AppSingleton.getINSTANCE().getSelectedProposal();
        if(workInterest == null) finish();

        userType = AppSingleton.getINSTANCE().getUserType();
        setupUi();

        refetchData();

        binding.acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptOffer();
            }
        });
        binding.rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rejectOffer();
            }
        });
    }

    void setupUi(){

        binding.status.setText(workInterest.getStatus());

        binding.workLayout.workTitle.setText(workInterest.getWork().getComplaint());
        binding.workLayout.workCategory.setText(workInterest.getWork().getCategory());
        binding.workLayout.workAddress.setVisibility(View.GONE);
        binding.workLayout.addressIcon.setVisibility(View.GONE);

        binding.workerLayout.workerName.setText(workInterest.getWorker().getDisplayName());
        binding.workerLayout.workerSub.setVisibility(View.GONE);

        binding.workLayout.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppSingleton.getINSTANCE().setSelectedWork(workInterest.getWork());
                startActivity(new Intent(ProposalDetailActivity.this,WorkDetailActivity.class));
                finish();
            }
        });

        Glide.with(this)
                .load(workInterest.getWorker().getProfUrl())
                .centerCrop()
                .into(binding.workerLayout.workerImg);

        binding.proposal.setText(workInterest.getProposal());
        binding.amount.setText("Estimated amount : " + workInterest.getAmount());
        binding.dates.setText("Available from : " + workInterest.getFromDate() + " to " + workInterest.getToDate());

        if(workInterest.getStatus().equals("ACCEPTED") || workInterest.getStatus().equals("REJECTED"))
            binding.btnParent.setVisibility(View.GONE);
        else binding.btnParent.setVisibility(View.VISIBLE);

        if(userType.equals("WORKER")){
            binding.btnParent.setVisibility(View.GONE);
        } else {
            updateReadStatus();
        }


    }

    void refetchData(){

        FirebaseFirestore.getInstance().collection("Interests")
                .document(workInterest.getInterestId())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            Toast.makeText(ProposalDetailActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        workInterest = value.toObject(WorkInterest.class);
                        setupUi();
                    }
                });

    }

    void updateReadStatus(){
        if(workInterest.getStatus().equals("SENT")){
            FirebaseFirestore.getInstance().collection("Interests")
                    .document(workInterest.getInterestId())
                    .update("status","VIEWED");
        }
    }
    void acceptOffer(){

        FirebaseFirestore.getInstance().collection("Interests")
                .document(workInterest.getInterestId())
                .update("status","ACCEPTED")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ProposalDetailActivity.this, "Proposal accepted successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ProposalDetailActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    void rejectOffer(){

        FirebaseFirestore.getInstance().collection("Interests")
                .document(workInterest.getInterestId())
                .update("status","REJECTED")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ProposalDetailActivity.this, "Proposal has been rejected", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ProposalDetailActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}


