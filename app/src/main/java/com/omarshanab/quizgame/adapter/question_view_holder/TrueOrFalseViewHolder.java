package com.omarshanab.quizgame.adapter.question_view_holder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.omarshanab.quizgame.adapter.QuestionsPagerAdapter;
import com.omarshanab.quizgame.database.ModelQuestion;
import com.omarshanab.quizgame.databinding.LayoutMultipleChoiceBinding;
import com.omarshanab.quizgame.databinding.LayoutTrueOrFalseBinding;
import com.omarshanab.quizgame.utils.UtilsAnimation;

public class TrueOrFalseViewHolder extends RecyclerView.ViewHolder {
    LayoutTrueOrFalseBinding binding;
    ModelQuestion modelQuestion;

    public TrueOrFalseViewHolder(LayoutTrueOrFalseBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(ModelQuestion modelQuestion) {
        this.modelQuestion = modelQuestion;
        binding.cardTrue.setOnClickListener(v -> onClickListener(v, true));
        binding.cardFalse.setOnClickListener(v -> onClickListener(v, false));
    }

    void onClickListener(View v, boolean isTrueButton) {
        UtilsAnimation.scaleViewAnimation(v);
        boolean isTrueAnswer = Boolean.parseBoolean(modelQuestion.getTrueAnswer()) == isTrueButton;
        QuestionsPagerAdapter.listenerAnswer.onAnswerQuestion(
                modelQuestion.getQuestionId(),
                isTrueAnswer,
                isTrueAnswer ? modelQuestion.getPoints() : -modelQuestion.getPoints());
        if(!isTrueAnswer) {
            binding.tvTrueFalseSolving.setText(modelQuestion.getTrueAnswer());
        }
    }
}