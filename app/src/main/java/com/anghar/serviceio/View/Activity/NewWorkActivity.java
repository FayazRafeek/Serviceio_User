package com.anghar.serviceio.View.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.anghar.serviceio.Model.AppSingleton;
import com.anghar.serviceio.Model.Data.Invite;
import com.anghar.serviceio.Model.Data.User;
import com.anghar.serviceio.Model.Data.Work;
import com.anghar.serviceio.Model.Data.Worker;
import com.anghar.serviceio.R;
import com.anghar.serviceio.View.Fragment.SelectDialog;
import com.anghar.serviceio.databinding.ActivityNewWorkBinding;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class NewWorkActivity extends AppCompatActivity implements SelectDialog.OnDialogSelect {


    ActivityNewWorkBinding binding;

    Boolean IS_INVITE = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNewWorkBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        IS_INVITE = getIntent().getBooleanExtra("WORKER_INVITE",false);
        if(IS_INVITE){
            binding.catLay.setVisibility(View.GONE);
            updateWorkerProfile();
            binding.categoryTitle.setText("Invite worker");
        }

        binding.catInp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCatSelect();
            }
        });

        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gatherData();
            }
        });

        binding.favBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    Worker worker;
    void updateWorkerProfile(){

        worker = AppSingleton.getINSTANCE().getSelectedWorker();
        binding.workerName.setText(worker.getDisplayName());
        binding.workerCat.setText(worker.getCategory());
        Glide.with(this)
                .load(worker.getProfUrl())
                .centerCrop()
                .into(binding.workerImage);

        binding.workerParent.setVisibility(View.VISIBLE);

    }
    void showCatSelect(){

        List<String> categories = new ArrayList<>();
        categories.add("Electrical");
        categories.add("Plumbing");
        categories.add("Construction");
        categories.add("Gardening");
        categories.add("Cleaning");
        categories.add("Day Care");
        categories.add("Other");

        new SelectDialog(categories,"Select category",this,"CATEGORY").show(getSupportFragmentManager(),"TAG");
    }


    void gatherData(){

        String category = binding.catInp.getText().toString();
        String comp = binding.compInp.getText().toString();
        String address = binding.addressInp.getText().toString();
        Boolean isUrgent = binding.urgerntCheck.isChecked();

        String userSt = getSharedPreferences(getString(R.string.AUTH_PREF_FILE),MODE_PRIVATE).getString("USER_DATA","");
        User user = new Gson().fromJson(userSt,User.class);

        if (user == null) {
            Toast.makeText(this, "User Data unavailable", Toast.LENGTH_SHORT).show();
            return;
        }

        Work work = new Work(String.valueOf(System.currentTimeMillis())
        ,category,comp,user.getuId(),user.getName(),user.getPhone(),user.getEmail(),address,isUrgent,System.currentTimeMillis());


        if(category.equals("Other")){
            String otherCat = binding.otherInp.getText().toString();
            work.setOtherCat(otherCat);
        }
        if(IS_INVITE){
            String inviteId = System.currentTimeMillis() + worker.getWorkerId().substring(0,3);
            Invite invite = new Invite(inviteId
                    ,user.getuId(),worker.getWorkerId(),work.getWorkId()
                    ,user.getName(),user.getPhone(),worker.getDisplayName(),work.getCategory()
                    ,work,worker,"SENT",System.currentTimeMillis()
                    );
            FirebaseFirestore
                    .getInstance()
                    .collection("Invites")
                    .document(inviteId)
                    .set(invite)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(NewWorkActivity.this, "Invite sent successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            } else
                                Toast.makeText(NewWorkActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
        } else
        FirebaseFirestore.getInstance()
                .collection("Works")
                .document(work.getWorkId())
                .set(work)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(NewWorkActivity.this, "Work added successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        } else
                            Toast.makeText(NewWorkActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }


    @Override
    public void onSelectItem(String item, String tag) {
        if(tag.equals("CATEGORY")){
            binding.catInp.setText(item);
            if(item.equals("Other")){
                binding.otherCat.setVisibility(View.VISIBLE);
                binding.otherInp.setText("");
            } else {
                binding.otherCat.setVisibility(View.GONE);
            }
        }
    }
}
