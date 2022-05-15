package com.anghar.serviceio.View.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.anghar.serviceio.Model.AppSingleton;
import com.anghar.serviceio.Model.Data.Worker;
import com.anghar.serviceio.View.Adapter.WorkerAdaoter;
import com.anghar.serviceio.databinding.ActivityCategoryBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity implements WorkerAdaoter.OnWorkerClick {

    ActivityCategoryBinding binding;
    String category;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        category = getIntent().getStringExtra("CATEGORY");
        if(category == null) finish();

        binding.categoryTitle.setText(category);

        binding.favBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        fetchWorkers();

        binding.catSipw.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchWorkers();
            }
        });
    }

    void fetchWorkers(){

        binding.catSipw.setRefreshing(true);
        FirebaseFirestore.getInstance().collection("Workers")
                .whereEqualTo("category",category)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        binding.catSipw.setRefreshing(false);
                        if(task.isSuccessful()){

                            List<Worker> workers = new ArrayList<>();
                            for(DocumentSnapshot doc : task.getResult()){
                                workers.add(doc.toObject(Worker.class));
                            }

                            updateRecycler(workers);
                        }
                    }
                });
    }

    WorkerAdaoter workerAdaoter;
    void updateRecycler(List<Worker> list){

        if(workerAdaoter == null){
            workerAdaoter = new WorkerAdaoter(this,this);
            binding.catRecyler.setAdapter(workerAdaoter);
            binding.catRecyler.setLayoutManager(new GridLayoutManager(this,2));
        }

        workerAdaoter.updateList(list);

        if(list.size() == 0){
            binding.emptyUi.setVisibility(View.VISIBLE);
        } else binding.emptyUi.setVisibility(View.GONE);
    }

    @Override
    public void onWorkerClick(Worker worker) {
        AppSingleton.getINSTANCE().setSelectedWorker(worker);
        startActivity(new Intent(this,WorkerDetailActivity.class));
    }
}
