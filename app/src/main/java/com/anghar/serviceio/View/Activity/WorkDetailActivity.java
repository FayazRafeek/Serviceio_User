package com.anghar.serviceio.View.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.anghar.serviceio.Model.AppSingleton;
import com.anghar.serviceio.Model.Data.Invite;
import com.anghar.serviceio.Model.Data.Work;
import com.anghar.serviceio.Model.Data.WorkInterest;
import com.anghar.serviceio.View.Adapter.InterestAdapter;
import com.anghar.serviceio.databinding.ActivityWorkDetailBinding;
import com.anghar.serviceio.databinding.ActivityWorkerDetailBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WorkDetailActivity extends AppCompatActivity implements InterestAdapter.OnInterestAction {


    ActivityWorkDetailBinding binding;
    Work work;
    Invite invite;
    String userType;
    String workType = "NORMAL";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityWorkDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        workType = getIntent().getStringExtra("WORK_TYPE");
        if(workType == null) workType ="";
        if(workType.equals("INVITE")){
            invite = AppSingleton.getINSTANCE().getSelectedInvite();
            work = invite.getWork();
            binding.proposalLabel.setVisibility(View.GONE);
            binding.category.setText("Invite Details");
            refetch();
        } else
        work = AppSingleton.getINSTANCE().getSelectedWork();

        if(work == null) finish();

        updateUi();

        binding.backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        userType = AppSingleton.getINSTANCE().getUserType();

        if(userType.equals("USER")){
            binding.btnParent.setVisibility(View.GONE);
            binding.contactBtn.setVisibility(View.GONE);
            binding.fromParent.setVisibility(View.GONE);

            if(workType.equals("INVITE")) {
                binding.sendIntBtn.setVisibility(View.GONE);
                binding.category.setText("Invite Detail");
            } else fetchInterests();
        } else {

            binding.proposalLabel.setVisibility(View.GONE);
            binding.btnParent.setVisibility(View.VISIBLE);

            if(workType.equals("INVITE")){
                binding.sendIntBtn.setVisibility(View.GONE);
                binding.actionBtnParent.setVisibility(View.VISIBLE);
                binding.category.setText("Invite Detail");
            } else
                binding.sendIntBtn.setVisibility(View.VISIBLE);
        }


        binding.sendIntBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppSingleton.getINSTANCE().setSelectedWork(work);
                startActivity(new Intent(WorkDetailActivity.this,NewInterestActivity.class));
            }
        });


        binding.acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptInvite();
            }
        });

        binding.rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rejectInvite();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    void fetchInterests(){

        binding.proposalSwipe.setRefreshing(true);
        FirebaseFirestore.getInstance()
                .collection("Interests")
                .whereEqualTo("workId", work.getWorkId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        binding.proposalSwipe.setRefreshing(false);
                        if(task.isSuccessful()){
                            List<WorkInterest> workInterests = new ArrayList<>();
                            for (DocumentSnapshot doc : task.getResult()){
                                workInterests.add(doc.toObject(WorkInterest.class));
                            }
                            updateWorkProposalList(workInterests);
                        } else {
                            Toast.makeText(WorkDetailActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("333", "onComplete: Proposal error " + task.getException());
                        }
                    }
                });
    }

    InterestAdapter workProposalAdater;
    void updateWorkProposalList(List<WorkInterest> interests){

        if(workProposalAdater == null){
            workProposalAdater = new InterestAdapter(this,this);
            binding.proposalRecycler.setAdapter(workProposalAdater);
            binding.proposalRecycler.setLayoutManager(new LinearLayoutManager(this));
        }
        workProposalAdater.updateLis(interests);

        if(interests.size() == 0) binding.emptyUi.setVisibility(View.VISIBLE);
        else binding.emptyUi.setVisibility(View.GONE);
    }

    void updateUi(){
        binding.usernameTxt.setText(work.getUsername());
        binding.phoneTxt.setText(work.getPhone());
        binding.compTxt.setText(work.getComplaint());
        binding.addresTxt.setText(work.getAddress());

        if(workType.equals("INVITE")){
            binding.status.setVisibility(View.VISIBLE);
            binding.status.setText(invite.getStatus());

            if(invite.getStatus().equals("ACCEPTED") || invite.getStatus().equals("REJECTED")){
                binding.btnParent.setVisibility(View.GONE);
            } else
                binding.btnParent.setVisibility(View.VISIBLE);

        } else binding.status.setVisibility(View.GONE);

        Date date = new Date();
        date.setTime(work.getPostedDate());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String postedDate = sdf.format(date);

        binding.postedDate.setText("Posted on : " + postedDate);

        binding.contactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(work.getPhone() == null) return;
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + work.getPhone()));
                startActivity(intent);
            }
        });
    }

    void acceptInvite(){

        FirebaseFirestore.getInstance()
                .collection("Invites")
                .document(invite.getInviteId())
                .update("status","ACCEPTED")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(WorkDetailActivity.this, "Accepted invites", Toast.LENGTH_SHORT).show();
                            refetch();
                        } else {
                            Toast.makeText(WorkDetailActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    void rejectInvite(){

        FirebaseFirestore.getInstance()
                .collection("Invites")
                .document(invite.getInviteId())
                .update("status","REJECTED")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(WorkDetailActivity.this, "Rejected invite", Toast.LENGTH_SHORT).show();
                            refetch();
                        } else {
                            Toast.makeText(WorkDetailActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    void refetch(){

        FirebaseFirestore.getInstance()
                .collection("Invites")
                .document(invite.getInviteId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            invite = task.getResult().toObject(Invite.class);
                            work = invite.getWork();
                            updateUi();
                        }
                    }
                });
    }

    @Override
    public void onInterestClick(WorkInterest item) {

        AppSingleton.getINSTANCE().setSelectedProposal(item);
        startActivity(new Intent(this, ProposalDetailActivity.class));
    }
}
