package com.omarshanab.quizgame.activites;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.omarshanab.quizgame.R;
import com.omarshanab.quizgame.database.DatabaseRepository;
import com.omarshanab.quizgame.databinding.ActivityLoginBinding;
import com.omarshanab.quizgame.utils.UtilValidation;
import com.omarshanab.quizgame.utils.Utils;

import java.util.Objects;

public class ActivityLogin extends AppCompatActivity {
    ActivityLoginBinding binding;
    DatabaseRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.login);

        repository = DatabaseRepository.getInstance(getApplication());

        binding.buRegister.setOnClickListener(view -> {
            Intent intent = new Intent(getBaseContext(), ActivitySignUp.class);
            arl.launch(intent);
        });

        binding.buLogin.setOnClickListener(view -> {
            String email = Objects.requireNonNull(binding.teEmail2.getText()).toString().trim();
            String password = Objects.requireNonNull(binding.tePassword2.getText()).toString().trim();

            if (UtilValidation.validateEmailAddress(email, ActivityLogin.this)
                    && UtilValidation.validatePassword(password, true, ActivityLogin.this)) {
                repository.hasUser(email, password, user -> {
                    if (user == null) {
                        runOnUiThread(() ->
                                Toast.makeText(ActivityLogin.this, getString(R.string.login_information_not_match), Toast.LENGTH_SHORT).show());
                        return;
                    }
                    Utils.initUserData(user);
                    startWelcomeActivity();
                    if (binding.cbRememberMe.isChecked())
                        repository.updateRememberMy(email, true);
                });
            }
        });

    }

    ActivityResultLauncher<Intent> arl = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == 100) {
                    startWelcomeActivity();
                }
            });

    private void startWelcomeActivity() {
        Intent intent = new Intent(getBaseContext(), ActivityWelcome.class);
        startActivity(intent);
        finish();
    }
}