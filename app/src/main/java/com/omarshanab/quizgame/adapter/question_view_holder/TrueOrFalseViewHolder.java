package com.omarshanab.quizgame.adapter.question_view_holder;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.omarshanab.quizgame.database.ModelQuestion;
import com.omarshanab.quizgame.databinding.LayoutTrueOrFalseBinding;
import com.omarshanab.quizgame.interfaces.OnListenerAnswer;
import com.omarshanab.quizgame.utils.UtilsAnimation;

import org.json.JSONArray;
import org.json.JSONException;

public class TrueOrFalseViewHolder extends RecyclerView.ViewHolder {
    LayoutTrueOrFalseBinding binding;
    ModelQuestion modelQuestion;
    private String trueAnswer;

    public TrueOrFalseViewHolder(LayoutTrueOrFalseBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(ModelQuestion modelQuestion, OnListenerAnswer onListenerAnswer) {
        this.modelQuestion = modelQuestion;
        try {
            JSONArray jsonArray = new JSONArray(modelQuestion.getTrueAnswers());
            if (jsonArray.length() > 0) {
                trueAnswer = jsonArray.getString(0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        binding.cardTrue.setOnClickListener(v -> onClickListener(v, true, onListenerAnswer));
        binding.cardFalse.setOnClickListener(v -> onClickListener(v, false, onListenerAnswer));
    }

    void onClickListener(View v, boolean isTrueButton, OnListenerAnswer onListenerAnswer) {
        UtilsAnimation.scaleViewAnimation(v);
        boolean isTrueAnswerCorrect = Boolean.parseBoolean(trueAnswer) == isTrueButton;
        onListenerAnswer.onAnswerQuestion(
                modelQuestion.getQuestionId(),
                isTrueAnswerCorrect,
                isTrueAnswerCorrect ? modelQuestion.getPoints() : -modelQuestion.getPoints());
        if (!isTrueAnswerCorrect) {
            binding.tvTrueFalseSolving.setText(trueAnswer);
        }
    }
}
