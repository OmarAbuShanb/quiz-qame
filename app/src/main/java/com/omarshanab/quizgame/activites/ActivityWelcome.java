package com.omarshanab.quizgame.activites;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.omarshanab.quizgame.R;
import com.omarshanab.quizgame.databinding.ActivityWelcomeBinding;
import com.omarshanab.quizgame.utils.Utils;

public class ActivityWelcome extends AppCompatActivity {
    ActivityWelcomeBinding binding;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Utils.playOrResumeOrPauseLoopSound(getApplicationContext());

        binding.tvWelcomeMassage.setText(Utils.fullName);

        binding.buStart.setOnClickListener(v -> {
            Utils.playSound(this, R.raw.click);
            Intent intent = new Intent(getBaseContext(), ActivityLevel.class);
            startActivity(intent);
        });

        binding.buSettings.setOnClickListener(v -> {
            Utils.playSound(this, R.raw.click);
            Intent intent = new Intent(getBaseContext(), ActivitySettings.class);
            arl.launch(intent);
        });

        binding.buExist.setOnClickListener(v -> finish());
    }

    ActivityResultLauncher<Intent> arl = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == 100) {
                    Intent intent = new Intent(getBaseContext(), ActivityLogin.class);
                    startActivity(intent);
                    finish();
                }
            });
}