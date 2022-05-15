package com.anghar.serviceio.View.Activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.anghar.serviceio.Model.AppSingleton;
import com.anghar.serviceio.Model.Data.User;
import com.anghar.serviceio.Model.Data.Work;
import com.anghar.serviceio.Model.Data.WorkInterest;
import com.anghar.serviceio.Model.Data.Worker;
import com.anghar.serviceio.R;
import com.anghar.serviceio.databinding.ActivityNewInterestBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.Calendar;

public class NewInterestActivity extends AppCompatActivity {

    ActivityNewInterestBinding binding;
    Work work;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNewInterestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        work = AppSingleton.INSTANCE.getSelectedWork();
        if(work == null) finish();

        setUpUi();

        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendInterest();
            }
        });
    }

    void setUpUi(){

        if(work.getCategory().equals("Other"))
            binding.workLayout.workCategory.setText(work.getOtherCat());
        else
            binding.workLayout.workCategory.setText(work.getCategory());

        binding.workLayout.workTitle.setText(work.getComplaint());
        binding.workLayout.workAddress.setText(work.getAddress());

        Calendar cal = Calendar.getInstance();
        binding.fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog pickerDialog = new DatePickerDialog(NewInterestActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                fromCal = Calendar.getInstance();
                                fromCal.set(Calendar.YEAR,i);
                                fromCal.set(Calendar.MONTH,i1);
                                fromCal.set(Calendar.DAY_OF_MONTH,i2);
                                i1++;
                                String date = i2 + "/" + i1 + "/" + i;
                                binding.fromDate.setText(date);
                                binding.toDate.setText("");
                            }
                        },
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH));

                pickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                pickerDialog.show();

            }
        });

        binding.toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(fromCal == null){
                    Toast.makeText(NewInterestActivity.this, "Pick a starting date first", Toast.LENGTH_SHORT).show();
                    return;
                }
                DatePickerDialog pickerDialog = new DatePickerDialog(NewInterestActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                toCal = Calendar.getInstance();
                                toCal.set(Calendar.YEAR,i);
                                toCal.set(Calendar.MONTH,i1);
                                toCal.set(Calendar.DAY_OF_MONTH,i2);
                                i1++;
                                String date = i2 + "/" + i1 + "/" + i;
                                binding.toDate.setText(date);
                            }
                        },
                        fromCal.get(Calendar.YEAR),
                        fromCal.get(Calendar.MONTH),
                        fromCal.get(Calendar.DAY_OF_MONTH));

                pickerDialog.getDatePicker().setMinDate(fromCal.getTimeInMillis());
                pickerDialog.show();

            }
        });
    }

    Calendar fromCal,toCal;


    void sendInterest(){


        String proposal = binding.proposal.getText().toString();
        String amount = binding.amount.getText().toString();
        String fromDate = binding.fromDate.getText().toString();
        String toDate = binding.toDate.getText().toString();

        String workerSt = getSharedPreferences(getString(R.string.AUTH_PREF_FILE),MODE_PRIVATE).getString("WORKER_DATA","");
        Worker worker = new Gson().fromJson(workerSt,Worker.class);

        if(worker == null){
            Toast.makeText(this, "Worker data not found", Toast.LENGTH_SHORT).show();
            finish();
        }

        String intId = System.currentTimeMillis() + worker.getWorkerId().substring(0,3) + work.getWorkId().substring(0,3);
        WorkInterest workInterest = new WorkInterest(intId
        ,work.getWorkId(),work.getUserId(),worker.getWorkerId(),work,worker,proposal,amount,fromDate,toDate,System.currentTimeMillis());

        FirebaseFirestore.getInstance()
                .collection("Interests")
                .document(workInterest.getInterestId())
                .set(workInterest)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(NewInterestActivity.this, "Proposal sent successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(NewInterestActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
