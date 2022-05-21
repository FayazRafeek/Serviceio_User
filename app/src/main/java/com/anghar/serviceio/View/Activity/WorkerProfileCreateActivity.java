package com.anghar.serviceio.View.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.anghar.serviceio.Model.Data.Worker;
import com.anghar.serviceio.R;
import com.anghar.serviceio.View.Fragment.SelectDialog;
import com.anghar.serviceio.databinding.ActivityWorkerProfCreateBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class WorkerProfileCreateActivity extends AppCompatActivity implements SelectDialog.OnDialogSelect {

    ActivityWorkerProfCreateBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityWorkerProfCreateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dobInputListen();


        binding.profileImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageChoose();
            }
        });

        binding.catInp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCatSelectDialog();
            }
        });

        binding.subBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gatherData();
            }
        });

        binding.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    Calendar calendar;
    void dobInputListen(){
        calendar = Calendar.getInstance();
        binding.dobInp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(WorkerProfileCreateActivity.this,listner, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
    String dob = "";
    DatePickerDialog.OnDateSetListener listner =new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {

            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH,month);
            calendar.set(Calendar.DAY_OF_MONTH,day);

            String date = "";
            month++;
            if(day < 10) date = "0" + day; else date = "" + day;
            if(month < 10) date += "-0" + month; else date += "-" + month;
            date += "-" + year;
            dob = date;
            binding.dobInp.setText(date);

        }
    };


    void showImageChoose(){
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        profSelcResult.launch(chooserIntent);
    }

    Boolean IS_PROF_CHOOSE = false;
    ActivityResultLauncher<Intent> profSelcResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if(data != null){
                            Uri selectedImage = data.getData();
                            binding.profileImg.setImageURI(selectedImage);
                            IS_PROF_CHOOSE = true;
                        }
                    }
                }
            });



    List<String> categories = new ArrayList<>();
    void showCatSelectDialog(){
        categories.add("Electrical");
        categories.add("Plumbing");
        categories.add("Cleaning");
        categories.add("Construction");
        categories.add("Day Care");
        categories.add("Gardening");
        categories.add("Other");
        new SelectDialog(categories,"Select category : ", this,"CAT").show(getSupportFragmentManager(),"TAG");
    }


    @Override
    public void onSelectItem(String item, String tag) {
        binding.catInp.setText(item);
        if(item.equals("Other")){
            binding.otherLay.setVisibility(View.VISIBLE);
            binding.otherInp.setText("");
        } else binding.otherLay.setVisibility(View.GONE);
    }

    void gatherData(){

        String disName = binding.dispNameInp.getText().toString();
        String dob = binding.dobInp.getText().toString();
        String category = binding.catInp.getText().toString();
        String bio = binding.bioInp.getText().toString();
        String web = binding.webInp.getText().toString();
        String pin = binding.pinInp.getText().toString();
        String phone = binding.phoneInp.getText().toString();
        String city = binding.cityInp.getText().toString();

        Worker worker = new Worker();
        worker.setDisplayName(disName);
        worker.setBio(bio);
        worker.setDob(dob);
        worker.setWorkerId(FirebaseAuth.getInstance().getUid());
        worker.setPincode(pin);
        worker.setPhone(phone);
        worker.setCity(phone);
        if(category.equals("Other")){
            category = binding.otherInp.getText().toString();
        }
        worker.setCategory(category);
        worker.setWebsite(web);
        worker.setPincode(web);

        binding.profileImg.setDrawingCacheEnabled(true);
        binding.profileImg.buildDrawingCache();
        Bitmap profileBitmap = ((BitmapDrawable) binding.profileImg.getDrawable()).getBitmap();

        showLoadDing();

        uploadProf(worker,profileBitmap)
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            worker.setProfUrl(task.getResult().toString());
                            uploadData(worker);
                        } else {
                            stopLoadDing();
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    void uploadData(Worker worker){

        showLoadDing();

        FirebaseFirestore.getInstance()
                .collection("Workers")
                .document(worker.getWorkerId())
                .set(worker)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            stopLoadDing();
                            Toast.makeText(getApplicationContext(), "Upload Successfull", Toast.LENGTH_SHORT).show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    saveWorkerLocally(worker);
                                    finish();
                                }
                            },800);
                        } else {
                            stopLoadDing();
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public Task<Uri> uploadProf(Worker worker, Bitmap bitmap){

        StorageReference profRefs = FirebaseStorage.getInstance().getReference().child("worker/profile/" + worker.getWorkerId());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = profRefs.putBytes(data);

        return uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return profRefs.getDownloadUrl();
            }
        });
    }

    void saveWorkerLocally(Worker worker){
        SharedPreferences pref = getSharedPreferences(getString(R.string.AUTH_PREF_FILE), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(worker);
        editor.putString("WORKER_DATA",json);
        editor.commit();
    }


    void showLoadDing(){
        binding.progressParent.setVisibility(View.VISIBLE);
    }


    void stopLoadDing(){
        binding.progressParent.setVisibility(View.GONE);
    }
}
