package com.omarshanab.quizgame.activites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.omarshanab.quizgame.R;
import com.omarshanab.quizgame.database.DatabaseRepository;
import com.omarshanab.quizgame.databinding.ActivitySettingsBinding;
import com.omarshanab.quizgame.utils.DialogWarning;
import com.omarshanab.quizgame.utils.Utils;

public class ActivitySettings extends AppCompatActivity {
    public static String AllowSOUND = "allowSound";
    public static String AllowNOTIFICATION = "allowNotification";
    ActivitySettingsBinding binding;
    DatabaseRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        setTitle(getString(R.string.settings));

        SharedPreferences.Editor preferences = PreferenceManager.getDefaultSharedPreferences(this).edit();

        binding.switchSound.setChecked(Utils.allowSound);
        binding.switchSound.setOnClickListener(v -> {
            Utils.allowSound = !Utils.allowSound;
            preferences.putBoolean(AllowSOUND, Utils.allowSound).apply();
            if (Utils.loopMP != null) {
                Utils.loopMP.pause();
            } else {
                Utils.playOrResumeOrPauseLoopSound(getApplicationContext());
            }
        });

        binding.switchSound.setChecked(Utils.allowNotification);
        binding.switchSound.setOnCheckedChangeListener((v, isChecked) -> {
            Utils.allowNotification = isChecked;
            preferences.putBoolean(AllowNOTIFICATION, isChecked).apply();

            if (isChecked) {
                Utils.setAlarmNotification(ActivitySettings.this);
            } else {
                Utils.chancelAlarmNotification(ActivitySettings.this);
            }
        });

        repository = DatabaseRepository.getInstance(getApplication());

        repository.getSumPoints(Utils.userId, num -> runOnUiThread(() -> binding.tvPoints.setText(" " + num)));

        repository.getLastUserLevel(Utils.userId, num -> runOnUiThread(() -> binding.tvLevel.setText(" " + num)));

        binding.buDeleteAllHistory.setOnClickListener(v -> {
            DialogWarning dialogWarning = new DialogWarning(ActivitySettings.this,
                    getString(R.string.delete),
                    getString(R.string.entire_game_history_will_be_deleted), () -> {
                repository.deleteAllHistoryUser(Utils.userId, () -> {
                    runOnUiThread(() -> {
                        Toast.makeText(this, getString(R.string.delete_successfuly), Toast.LENGTH_SHORT).show();
                        binding.tvPoints.setText(" 0");
                        binding.tvLevel.setText(" 1");
                    });
                });
            });
            dialogWarning.show();
        });

        binding.buEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), ActivitySignUp.class);
            intent.putExtra("edit_profile", true);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.bu_logout) {
            repository.updateRememberMy(Utils.email, false);
            setResult(100);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}