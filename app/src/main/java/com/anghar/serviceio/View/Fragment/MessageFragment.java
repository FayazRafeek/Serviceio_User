package com.anghar.serviceio.View.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.anghar.serviceio.Model.AppSingleton;
import com.anghar.serviceio.Model.Data.Work;
import com.anghar.serviceio.View.Adapter.WorkListAdapter;
import com.anghar.serviceio.databinding.FragmentMessageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MessageFragment extends Fragment implements WorkListAdapter.WorkClick {

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
        } else {
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
                .collection("Workers")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("Invite")
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
                            updateWorkInviteList(works);
                        } else {
                            Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    WorkListAdapter inviteAdapter;
    void updateWorkInviteList(List<Work> works){

        if(inviteAdapter == null){
            inviteAdapter = new WorkListAdapter(getContext(),this);
            binding.workInviteRecycler.setAdapter(inviteAdapter);
            binding.workInviteRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        inviteAdapter.updateList(works);

        if(!works.isEmpty()){
            binding.inviteLabel.setVisibility(View.VISIBLE);
            binding.workInviteRecycler.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void onWorkClick(Work work) {

    }
}
