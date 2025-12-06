package com.omarshanab.quizgame.activites;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.omarshanab.quizgame.R;
import com.omarshanab.quizgame.adapter.LevelRecyclerAdapter;
import com.omarshanab.quizgame.database.DatabaseRepository;
import com.omarshanab.quizgame.databinding.ActivityLevelBinding;
import com.omarshanab.quizgame.utils.Utils;

import java.io.Serializable;
import java.util.Objects;

public class ActivityLevel extends AppCompatActivity {
    ActivityLevelBinding binding;
    DatabaseRepository repository;
    LevelRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLevelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(
                getSupportActionBar()).setTitle(getString(R.string.levels)
        );

        repository = DatabaseRepository.getInstance(getApplication());
        repository.getAllLevels(levels ->
                repository.getSumPoints(Utils.userId, sumPoints ->
                        runOnUiThread(() -> {
                            adapter = new LevelRecyclerAdapter(
                                    getBaseContext(),
                                    levels,
                                    sumPoints,
                                    (levelNo, isLastLevel, pointsLastLevel) ->
                                            repository.getQuestionsLevel(
                                                    Utils.userId,
                                                    levelNo,
                                                    (questions, lastQuestionSolveInLevelIndex, skipCount) -> {
                                                        Intent intent = new Intent(getBaseContext(), ActivityQuestion.class);
                                                        intent.putExtra("questions", (Serializable) questions);
                                                        intent.putExtra("lastQuestionSolveInLevelIndex", lastQuestionSolveInLevelIndex);
                                                        intent.putExtra("skipCount", skipCount);
                                                        intent.putExtra("sumPoints", sumPoints);
                                                        intent.putExtra("levelNo", levelNo);
                                                        intent.putExtra("isLastLevel", isLastLevel);
                                                        intent.putExtra("pointsLastLevel", pointsLastLevel);
                                                        arl.launch(intent);
                                                        Utils.playSound(this, R.raw.click);
                                                    }));
                            binding.levelRecyclerView.setHasFixedSize(true);
                            binding.levelRecyclerView.setLayoutManager(new GridLayoutManager(getBaseContext(), 2));
                            binding.levelRecyclerView.setAdapter(adapter);
                        })));
    }

    ActivityResultLauncher<Intent> arl = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == 100) {
                    repository.getSumPoints(Utils.userId, sumPoints ->
                            runOnUiThread(() ->
                                    adapter.refreshRecycler(sumPoints)
                            )
                    );
                }
            });
}