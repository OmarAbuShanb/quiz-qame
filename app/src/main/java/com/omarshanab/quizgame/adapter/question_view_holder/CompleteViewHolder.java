package com.omarshanab.quizgame.adapter.question_view_holder;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.omarshanab.quizgame.adapter.QuestionsPagerAdapter;
import com.omarshanab.quizgame.database.ModelQuestion;
import com.omarshanab.quizgame.databinding.LayoutCompleteBinding;

import java.util.Objects;

public class CompleteViewHolder extends RecyclerView.ViewHolder {
    LayoutCompleteBinding binding;
    Context context;

    public CompleteViewHolder(LayoutCompleteBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
        context = itemView.getContext();
    }

    public void bind(ModelQuestion modelQuestion) {
        binding.buCheck.setOnClickListener(v -> {
            String answer = Objects.requireNonNull(binding.edAnswer.getText()).toString().trim();
            if (!answer.trim().isEmpty()) {
                boolean isTrueAnswer = modelQuestion.getTrueAnswer().equals(answer);
                QuestionsPagerAdapter.listenerAnswer.onAnswerQuestion(
                        modelQuestion.getQuestionId(),
                        isTrueAnswer,
                        isTrueAnswer ? modelQuestion.getPoints() : -modelQuestion.getPoints());

                if (!isTrueAnswer) {
                    binding.tvCompleteSolving.setText(modelQuestion.getTrueAnswer());
                }
            }
        });
    }
}
