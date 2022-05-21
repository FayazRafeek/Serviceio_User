package com.anghar.serviceio.View.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.anghar.serviceio.Model.AppSingleton;
import com.anghar.serviceio.Model.Data.Category;
import com.anghar.serviceio.Model.Data.Invite;
import com.anghar.serviceio.Model.Data.User;
import com.anghar.serviceio.Model.Data.Work;
import com.anghar.serviceio.Model.Data.Worker;
import com.anghar.serviceio.R;
import com.anghar.serviceio.View.Activity.AllCategoryActivity;
import com.anghar.serviceio.View.Activity.CategoryActivity;
import com.anghar.serviceio.View.Activity.NewWorkActivity;
import com.anghar.serviceio.View.Activity.WorkDetailActivity;
import com.anghar.serviceio.View.Activity.WorkerProfileCreateActivity;
import com.anghar.serviceio.View.Adapter.CategoryAdapter;
import com.anghar.serviceio.View.Adapter.WorkListAdapter;
import com.anghar.serviceio.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements CategoryAdapter.CategoryAction, WorkListAdapter.WorkClick {

    FragmentHomeBinding binding;
    String userType;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userType = AppSingleton.getINSTANCE().getUserType();

        if(userType.equals("USER")){
            checkUserProfileStatus();
            fetchCategories();
            binding.newWrkBtn.setVisibility(View.VISIBLE);
        } else {
            checkWorkerProfileStatus();
            binding.newWrkBtn.setVisibility(View.GONE);
            fetchWorks();
        }


        binding.incompParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userType.equals("WORKER")){
                    startActivity(new Intent(getActivity(), WorkerProfileCreateActivity.class));
                }
            }
        });

        binding.newWrkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), NewWorkActivity.class));
            }
        });

        binding.allCatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AllCategoryActivity.class));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if(userType.equals("USER")){
            checkUserProfileStatus();
        } else {
            checkWorkerProfileStatus();
        }

    }

    void fetchCategories(){

        FirebaseFirestore.getInstance().collection("Category").limit(6)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<Category> categories = new ArrayList<>();
                    for(DocumentSnapshot d : task.getResult()){
                        categories.add(d.toObject(Category.class));
                    }
                    setUpCategiry(categories);
                }
            }
        });
    }

    CategoryAdapter categoryAdapter;
    void setUpCategiry(List<Category> categories){
        if(categoryAdapter == null){
            categoryAdapter = new CategoryAdapter(getContext(), this);
            binding.categoryRecycler.setAdapter(categoryAdapter);
            binding.categoryRecycler.setLayoutManager(new GridLayoutManager(getContext(),2));
        }
        categoryAdapter.updateList(categories);

        binding.allCatBtn.setVisibility(View.VISIBLE);
    }


    @Override
    public void onCatClick(Category category) {
        Intent intent = new Intent(getActivity(), CategoryActivity.class);
        AppSingleton.getINSTANCE().setSelectedCategory(category);
        intent.putExtra("CATEGORY",category.getTitle());
        startActivity(intent);
    }


    User user;
    Worker worker;
    void checkUserProfileStatus(){

        String userId = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore.getInstance()
                .collection("Users")
                .document(userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful() && task.getResult().exists()){
                            user = task.getResult().toObject(User.class);
//                            saveUserLocally(user);

                            showUserWelcomeUi();
                        } else {
                            showUserProfIncpUi();
                        }
                    }
                });

    }


    void showUserWelcomeUi(){

        if(user == null && worker == null) return;
        if(userType.equals("USER"))
            binding.welName.setText(user.getName().split(" ")[0]);
        else
            binding.welName.setText(worker.getDisplayName().split(" ")[0] );
        binding.userWelcomeParent.setVisibility(View.VISIBLE);
        binding.incompParent.setVisibility(View.GONE);
    }


    void showUserProfIncpUi(){
        binding.incompParent.setVisibility(View.VISIBLE);
        binding.userWelcomeParent.setVisibility(View.GONE);
    }

    //


    void checkWorkerProfileStatus(){

        String userId = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore.getInstance()
                .collection("Workers")
                .document(userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful() && task.getResult().exists()){
                            worker = task.getResult().toObject(Worker.class);
                            saveWorkerLocally(worker);
                            showUserWelcomeUi();
                            fetchWorks();
                        } else {
                            showUserProfIncpUi();
                        }
                    }
                });
    }


    void fetchWorks(){

        if(worker == null) return;

        FirebaseFirestore
                .getInstance()
                .collection("Works")
                .whereEqualTo("category",worker.getCategory())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

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
            workListAdapter = new WorkListAdapter(getContext(),this);
            binding.categoryRecycler.setAdapter(workListAdapter);
            binding.categoryRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        workListAdapter.updateList(works);
        binding.categoryRecycler.setVisibility(View.VISIBLE);

        if (works.size() == 0){
            binding.emptyUi.setVisibility(View.VISIBLE);
        } else binding.emptyUi.setVisibility(View.GONE);
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

    void saveWorkerLocally(Worker worker){
        SharedPreferences pref = getContext().getSharedPreferences(getContext().getString(R.string.AUTH_PREF_FILE), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(worker);
        editor.putString("WORKER_DATA",json);
        editor.commit();
    }

    void saveUserLocally(User worker){
        SharedPreferences pref = getContext().getSharedPreferences(getContext().getString(R.string.AUTH_PREF_FILE), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(worker);
        editor.putString("USER_DATA",json);
        editor.commit();
    }
}
