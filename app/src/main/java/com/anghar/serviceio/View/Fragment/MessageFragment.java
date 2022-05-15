package com.anghar.serviceio.View.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.anghar.serviceio.Model.AppSingleton;
import com.anghar.serviceio.Model.Data.Invite;
import com.anghar.serviceio.Model.Data.Work;
import com.anghar.serviceio.Model.Data.WorkInterest;
import com.anghar.serviceio.View.Activity.ProposalDetailActivity;
import com.anghar.serviceio.View.Activity.WorkDetailActivity;
import com.anghar.serviceio.View.Adapter.InterestAdapter;
import com.anghar.serviceio.View.Adapter.WorkListAdapter;
import com.anghar.serviceio.databinding.FragmentMessageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MessageFragment extends Fragment implements WorkListAdapter.WorkClick, InterestAdapter.OnInterestAction {

    FragmentMessageBinding binding;
    String userType = "USER";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMessageBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userType = AppSingleton.getINSTANCE().getUserType();

        if(userType.equals("USER")){
            fetchUserWorks();
            fetchUserInvites();
        } else {
            binding.hero.setVisibility(View.GONE);
            fetchProposals();
            fetchInvites();
        }
    }


    void fetchUserWorks(){

        binding.messSwipe.setRefreshing(true);

        FirebaseFirestore.getInstance()
                .collection("Works")
                .whereEqualTo("userId", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        binding.messSwipe.setRefreshing(false);
                        if(task.isSuccessful()){

                            List<Work> works = new ArrayList<>();
                            for (DocumentSnapshot doc : task.getResult()){
                                Work work = doc.toObject(Work.class);
                                works.add(work);
                            }

                            updtaeWorkrecycler(works);
                        } else {
                            Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    WorkListAdapter workListAdapter;
    void updtaeWorkrecycler(List<Work> works){

        if(workListAdapter == null){
            workListAdapter = new WorkListAdapter(getContext(),this,"FEED");
            binding.workRecycler.setAdapter(workListAdapter);
            binding.workRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        workListAdapter.updateList(works);
        binding.workRecycler.setVisibility(View.VISIBLE);

    }

    void fetchInvites(){

        binding.messSwipe.setRefreshing(true);
        FirebaseFirestore.getInstance()
                .collection("Invites")
                .whereEqualTo("workerId",FirebaseAuth.getInstance().getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            return;
                        }
                        List<Invite> works = new ArrayList<>();
                        for (DocumentSnapshot doc : value){
                            Invite work = doc.toObject(Invite.class);
                            works.add(work);
                        }
                        updateWorkInviteList(works);
                    }
                });
    }

    void fetchProposals(){

        FirebaseFirestore.getInstance()
                .collection("Interests")
                .whereEqualTo("workerId",FirebaseAuth.getInstance().getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        binding.messSwipe.setRefreshing(false);
                        if(error != null){
                            Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        List<WorkInterest> workInterests = new ArrayList<>();
                        for (DocumentSnapshot doc : value){
                            workInterests.add(doc.toObject(WorkInterest.class));
                        }
                        updateWorkProposalList(workInterests);

                    }
                });
    }

    void fetchUserInvites(){

        binding.messSwipe.setRefreshing(true);
        FirebaseFirestore.getInstance()
                .collection("Invites")
                .whereEqualTo("userId",FirebaseAuth.getInstance().getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            return;
                        }
                        List<Invite> works = new ArrayList<>();
                        for (DocumentSnapshot doc : value){
                            Invite work = doc.toObject(Invite.class);
                            works.add(work);
                        }
                        updateWorkInviteList(works);
                    }
                });
    }

    WorkListAdapter inviteAdapter;
    void updateWorkInviteList(List<Invite> works){

        if(inviteAdapter == null){
            inviteAdapter = new WorkListAdapter(getContext(),this,"INVITE");
            binding.workInviteRecycler.setAdapter(inviteAdapter);
            binding.workInviteRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        inviteAdapter.updateInviteList(works);

        if(!works.isEmpty()){
            binding.inviteLabel.setVisibility(View.VISIBLE);
            binding.workInviteRecycler.setVisibility(View.VISIBLE);
        }
        if(userType.equals("USER"))
        binding.inviteLabel.setText("Invites sent");
        else
            binding.inviteLabel.setText("Invites Recieved");
    }

    InterestAdapter workProposalAdater;
    void updateWorkProposalList(List<WorkInterest> interests){

        if(workProposalAdater == null){
            workProposalAdater = new InterestAdapter(getContext(),this);
            binding.workProposalRecycler.setAdapter(workProposalAdater);
            binding.workProposalRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        workProposalAdater.updateLis(interests);
        if(interests.size() > 0){
            binding.proposalLabel.setVisibility(View.VISIBLE);
            binding.workProposalRecycler.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onWorkClick(Work work) {
        AppSingleton.getINSTANCE().setSelectedWork(work);
        Intent intent = new Intent(getActivity(), WorkDetailActivity.class);
        intent.putExtra("TYPE",userType);
        startActivity(intent);
    }

    @Override
    public void onInviteClick(Invite work) {
        AppSingleton.getINSTANCE().setSelectedInvite(work);
        Intent intent = new Intent(getActivity(), WorkDetailActivity.class);
        intent.putExtra("WORK_TYPE","INVITE");
        startActivity(intent);
    }

    @Override
    public void onInterestClick(WorkInterest item) {
        AppSingleton.getINSTANCE().setSelectedProposal(item);
        startActivity(new Intent(getActivity(), ProposalDetailActivity.class));
    }
}
