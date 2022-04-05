package com.anghar.serviceio.View.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.anghar.serviceio.Model.AppSingleton;
import com.anghar.serviceio.Model.Data.Work;
import com.anghar.serviceio.Model.Data.Worker;
import com.anghar.serviceio.databinding.ActivityWorkerDetailBinding;
import com.bumptech.glide.Glide;

public class WorkerDetailActivity extends AppCompatActivity {

    ActivityWorkerDetailBinding binding;
    Worker worker;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityWorkerDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        worker = AppSingleton.getINSTANCE().getSelectedWorker();
        if(worker == null) finish();

        updateUi();
    }

    void updateUi(){

        Glide.with(this)
                .load(worker.getProfUrl())
                .centerCrop()
                .into(binding.workerImage);

        binding.workerName.setText(worker.getDisplayName());
        binding.workerCat.setText(worker.getCategory());
        binding.workerBio.setText(worker.getBio());
        binding.web.setText(worker.getWebsite());

        binding.phoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(worker.getPhone() == null) return;
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + worker.getPhone()));
                startActivity(intent);
            }
        });

        binding.invWrkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WorkerDetailActivity.this,NewWorkActivity.class);
                intent.putExtra("WORKER_INVITE",true);
                startActivity(intent);
            }
        });
    }
}
