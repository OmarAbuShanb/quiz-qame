package com.omarshanab.quizgame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.omarshanab.quizgame.activites.ActivityLogin;
import com.omarshanab.quizgame.activites.ActivitySettings;
import com.omarshanab.quizgame.activites.ActivityWelcome;
import com.omarshanab.quizgame.database.DatabaseRepository;
import com.omarshanab.quizgame.databinding.ActivityMainBinding;
import com.omarshanab.quizgame.utils.UtilSystemUI;
import com.omarshanab.quizgame.utils.Utils;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    DatabaseRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Utils.allowSound = preferences.getBoolean(ActivitySettings.AllowSOUND, true);
        Utils.allowNotification = preferences.getBoolean(ActivitySettings.AllowNOTIFICATION, true);

        repository =  DatabaseRepository.getInstance(getApplication());
        repository.getRememberMyUser(modelUser -> {
            runOnUiThread(() ->
                    new Handler().postDelayed(() -> {
                        Class<?> aClass;
                        if (modelUser != null) {
                            Utils.initUserData(modelUser);
                            aClass = ActivityWelcome.class;
                        } else {
                            aClass = ActivityLogin.class;
                        }
                        startActivity(new Intent(MainActivity.this, aClass));
                        finish();
                        new Handler().postDelayed(() ->
                                UtilSystemUI.showSystemUI(MainActivity.this), 1000);
                    }, 2000));
        });

        UtilSystemUI.hideSystemUI(MainActivity.this);
        Animation animationLogoSplash = AnimationUtils.loadAnimation(MainActivity.this, R.anim.translate_y);
        binding.logoSplash.startAnimation(animationLogoSplash);
        Utils.playSound(MainActivity.this, R.raw.sweep_transition);
    }
}