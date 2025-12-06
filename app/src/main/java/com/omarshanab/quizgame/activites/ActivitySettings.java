package com.omarshanab.quizgame.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.omarshanab.quizgame.R;
import com.omarshanab.quizgame.database.DatabaseRepository;
import com.omarshanab.quizgame.databinding.ActivitySettingsBinding;
import com.omarshanab.quizgame.utils.DialogWarning;
import com.omarshanab.quizgame.utils.Utils;
import com.omarshanab.quizgame.viewmodels.SettingsViewModel;

public class ActivitySettings extends AppCompatActivity {
    public static String AllowSOUND = "allowSound";
    public static String AllowNOTIFICATION = "allowNotification";
    ActivitySettingsBinding binding;
    DatabaseRepository repository;
    private SettingsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        setSupportActionBar(binding.toolbar);
        setTitle(getString(R.string.settings));


        viewModel.isSoundAllowed().observe(this, allowed -> {
            binding.switchSound.setChecked(allowed);
            Utils.allowSound = allowed;
        });
        viewModel.isNotificationAllowed().observe(this, allowed -> {
            binding.switchNotification.setChecked(allowed);
            Utils.allowNotification = allowed;
        });

        binding.switchSound.setOnCheckedChangeListener((buttonView, isChecked) -> {
            viewModel.setAllowSound(isChecked);
        });

        binding.switchNotification.setOnCheckedChangeListener((buttonView, isChecked) -> {
            viewModel.setAllowNotification(isChecked);
        });

        repository = DatabaseRepository.getInstance(getApplication());

        viewModel.getUserPoints().observe(this, num -> binding.tvPoints.setText(" " + num));

        viewModel.getUserLevel().observe(this, num -> binding.tvLevel.setText(" " + num));

        viewModel.isHistoryDeleted().observe(this, deleted -> {
            if (deleted) {
                Toast.makeText(this, getString(R.string.delete_successfuly), Toast.LENGTH_SHORT).show();
            }
        });
        binding.buDeleteAllHistory.setOnClickListener(v -> {
            DialogWarning dialogWarning = new DialogWarning(ActivitySettings.this,
                    getString(R.string.delete),
                    getString(R.string.entire_game_history_will_be_deleted),
                    () -> viewModel.deleteAllHistory());
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
