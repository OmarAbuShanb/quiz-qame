package com.omarshanab.quizgame.activites;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.omarshanab.quizgame.R;
import com.omarshanab.quizgame.adapter.QuestionsPagerAdapter;
import com.omarshanab.quizgame.database.DatabaseRepository;
import com.omarshanab.quizgame.database.ModelHistory;
import com.omarshanab.quizgame.database.ModelQuestion;
import com.omarshanab.quizgame.databinding.ActivityQuestionBinding;
import com.omarshanab.quizgame.databinding.LayoutToastBinding;
import com.omarshanab.quizgame.utils.DialogReloadLevel;
import com.omarshanab.quizgame.utils.DialogWarning;
import com.omarshanab.quizgame.utils.Utils;
import com.omarshanab.quizgame.utils.UtilsAnimation;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ActivityQuestion extends AppCompatActivity {
    ActivityQuestionBinding binding;
    int currentPagerPosition = 0;
    int scoreNum = 0;
    boolean stopTouchRunning = false;
    CountDownTimer countDownTimer;
    ArrayList<String> questionsType;
    int skipCount;
    List<ModelQuestion> questions;
    DatabaseRepository repository;
    int levelNo;
    QuestionsPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        questions = (List<ModelQuestion>) intent.getSerializableExtra("questions");
        currentPagerPosition = intent.getIntExtra("lastQuestionSolveInLevelIndex", 0);
        skipCount = intent.getIntExtra("skipCount", 0);
        scoreNum = intent.getIntExtra("sumPoints", 0);
        levelNo = intent.getIntExtra("levelNo", 0);
        boolean isLastLevel = intent.getBooleanExtra("isLastLevel", false);
        int pointsLastLevel = intent.getIntExtra("pointsLastLevel", 0);

        repository = DatabaseRepository.getInstance(getApplication());

        binding.tvScore.setText(String.valueOf(scoreNum));

        questionsType = new ArrayList<>();
        for (int i = 0; i < questions.size(); i++) {
            switch (questions.get(i).getPatternId()) {
                case 1: {
                    questionsType.add(getString(R.string.answer_true_or_false));
                    break;
                }
                case 2: {
                    questionsType.add(getString(R.string.choose_the_correct_answer));
                    break;
                }
                default:
                    questionsType.add(getString(R.string.complete_with_the_correct_answer));
            }
        }

        binding.tvQuestionType.setText(questionsType.get(currentPagerPosition));
        binding.tvQuestion.setText(questions.get(currentPagerPosition).getTitle(), TextView.BufferType.SPANNABLE);

        binding.progressQuestions.setMax(questions.size() * 20);
        binding.progressQuestions.setProgress((currentPagerPosition + 1) * 20);

        setCountdown(questions.get(currentPagerPosition).getDuration());

        adapter = new QuestionsPagerAdapter(questions);
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setUserInputEnabled(false);
        binding.viewPager.setCurrentItem(currentPagerPosition, false);
        QuestionsPagerAdapter.setListenerAnswer((id, isTrue, score) -> {
            showToastAnswer(isTrue, ActivityQuestion.this);
            setTouchViewPager(true);
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }

            int pp = 0;
            if (scoreNum + score >= 0) {
                scoreNum += score;
                pp = score;
            } else {
                pp = -scoreNum;
            }
            binding.tvScore.setText(String.valueOf(scoreNum));
            repository.insertHistoryGame(new ModelHistory(
                    Utils.userId, levelNo, id, isTrue ? 1 : 0, pp));
            System.out.println("setListenerAnswer isTrue" + isTrue);
        });

        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                currentPagerPosition = position;
                binding.tvQuestionType.setText(questionsType.get(currentPagerPosition));
                binding.tvQuestion.setText(questions.get(position).getTitle(), TextView.BufferType.SPANNABLE);
                setCountdown(questions.get(currentPagerPosition).getDuration());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 2) {
                    setTouchViewPager(false);
                    System.out.println("registerOnPageChangeCallback");
                }
            }
        });

        binding.buSkip.setOnClickListener(v -> {
            // skip
            if (!stopTouchRunning && skipCount > 0) {
                DialogWarning dialogWarning = new DialogWarning(ActivityQuestion.this,
                        getString(R.string.skip),
                        getString(R.string.really_want_to_skip), () -> {
                    Toast.makeText(this, getString(R.string.skip), Toast.LENGTH_SHORT).show();
                    nextQuestion();
                    skipCount--;
                    int pp = 0;
                    if (scoreNum - 1 >= 0) {
                        pp = -1;
                        scoreNum--;
                    }
                    repository.insertHistoryGame(new ModelHistory(Utils.userId, levelNo,
                            questions.get(currentPagerPosition).getQuestionId(), -1,
                            pp));
                });
                dialogWarning.show();
            } else if (stopTouchRunning && (questions.size() - 1 == currentPagerPosition)) {
                if (scoreNum < pointsLastLevel && !isLastLevel) {
                    DialogReloadLevel dialogReloadLevel = new DialogReloadLevel(ActivityQuestion.this, () -> {
                        repository.deleteHistoryLevel(Utils.userId, levelNo, num -> runOnUiThread(() -> {
                            binding.viewPager.setCurrentItem(0, false);
                            skipCount = 2;
                            scoreNum = num;
                            binding.tvScore.setText(String.valueOf(scoreNum));
                            binding.progressQuestions.setProgress(20);
                            adapter = new QuestionsPagerAdapter(questions);
                            binding.viewPager.setAdapter(adapter);
                            setTouchViewPager(false);
                        }));
                    });
                    dialogReloadLevel.show();
                } else if (isLastLevel) {
                    Toast.makeText(this, "تم بحمد الله", Toast.LENGTH_SHORT).show();
                } else {
                    // جاب النقاط
                    setResult(100);
                    finish();
                }
                // خلص المرحلة
            } else if (skipCount == 0) {
                Toast.makeText(this, R.string.dont_have_enough_points_for_skip, Toast.LENGTH_SHORT).show();
                // do not skip
            } else {
                // next
                nextQuestion();
            }
        });
    }

    void setCountdown(long millieSecond) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(millieSecond, 1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                String millieSecondString = String.valueOf(millisUntilFinished);
                String second = getString(R.string.ziro_ziro);
                if (millieSecondString.length() > 3) {
                    // "15000" => "15"
                    String secondString = millieSecondString.substring(0, millieSecondString.length() - 3);
                    // "5" => "05"
                    second = new DecimalFormat(getString(R.string.ziro_ziro)).format(Integer.parseInt(secondString));
                }
                binding.tvCountdown.setText(getString(R.string.ziro_ziro) + ":" + second);
            }

            @Override
            public void onFinish() {
                Toast.makeText(ActivityQuestion.this, R.string.finish_time, Toast.LENGTH_SHORT).show();
                setTouchViewPager(true);
                System.out.println("onFinish");
                int pointQuestion = -questions.get(currentPagerPosition).getPoints();
                int pp = 0;
                if (scoreNum + pointQuestion <= 0) {
                    pp = -scoreNum;
                    scoreNum = 0;
                }
                binding.tvScore.setText(String.valueOf(scoreNum));
                repository.insertHistoryGame(new ModelHistory(Utils.userId, levelNo,
                        questions.get(currentPagerPosition).getQuestionId(), 0,
                        scoreNum == 0 ? pp : -questions.get(currentPagerPosition).getPoints()));
            }
        };
        countDownTimer.start();
    }

    void showToastAnswer(boolean trueAnswer, Context context) {
        LayoutToastBinding binding = LayoutToastBinding.inflate(LayoutInflater.from(context));
        binding.image.setImageResource(trueAnswer ? R.drawable.smile : R.drawable.unhappy);
        binding.text.setText(trueAnswer ? getString(R.string.correct_answer) : getString(R.string.wrong_answer));
        Utils.showToast(binding.getRoot(), context);
        if (trueAnswer) {
            Utils.playSound(this, R.raw.win);
        } else {
            Utils.playSound(this, R.raw.fail);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    void setTouchViewPager(boolean runTouch) {
        System.out.println("setTouchViewPager runTouch = " + runTouch);
        stopTouchRunning = runTouch;
        binding.touchLayout.setOnTouchListener((v, event) -> runTouch);
    }

    void nextQuestion() {
//        UtilsAnimation.animatePagerTransition(true, binding.viewPager);
        binding.viewPager.setCurrentItem(currentPagerPosition + 1);
        UtilsAnimation.animationProgress(true, binding.progressQuestions, ActivityQuestion.this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Utils.setAlarmNotification(getApplicationContext());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Utils.chancelAlarmNotification(getApplicationContext());
    }
}