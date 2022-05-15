package com.anghar.serviceio.View.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.anghar.serviceio.R;
import com.anghar.serviceio.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {

    ActivitySplashBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Boolean is_logged_in = getSharedPreferences(getString(R.string.AUTH_PREF_FILE),MODE_PRIVATE).getBoolean(getString(R.string.IS_LOGIN_KEY),false);

        Animation animZoomIn = AnimationUtils.loadAnimation(this,
                R.anim.zoom_in);

        binding.parent.startAnimation(animZoomIn);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(is_logged_in)
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                else startActivity(new Intent(SplashActivity.this,AuthActivity.class));
                finish();
            }
        },1500);
    }
}
