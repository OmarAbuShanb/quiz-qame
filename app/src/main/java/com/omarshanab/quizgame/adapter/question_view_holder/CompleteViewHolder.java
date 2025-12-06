package com.omarshanab.quizgame.adapter.question_view_holder;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.omarshanab.quizgame.database.ModelQuestion;
import com.omarshanab.quizgame.databinding.LayoutCompleteBinding;
import com.omarshanab.quizgame.interfaces.OnListenerAnswer;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CompleteViewHolder extends RecyclerView.ViewHolder {
    LayoutCompleteBinding binding;
    Context context;

    public CompleteViewHolder(LayoutCompleteBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
        context = itemView.getContext();
    }

    public void bind(ModelQuestion modelQuestion, OnListenerAnswer onListenerAnswer) {
        binding.buCheck.setOnClickListener(v -> {
            String answer = Objects.requireNonNull(binding.edAnswer.getText()).toString().trim();
            if (!answer.trim().isEmpty()) {

                List<String> trueAnswers = new ArrayList<>();
                String firstTrueAnswer = "";
                try {
                    JSONArray jsonArray = new JSONArray(modelQuestion.getTrueAnswers());
                    if (jsonArray.length() > 0) {
                        firstTrueAnswer = jsonArray.getString(0);
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        trueAnswers.add(jsonArray.getString(i));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                boolean isTrueAnswer = false;
                for(String trueAns : trueAnswers) {
                    if (trueAns.equalsIgnoreCase(answer)) {
                        isTrueAnswer = true;
                        break;
                    }
                }

                onListenerAnswer.onAnswerQuestion(
                        modelQuestion.getQuestionId(),
                        isTrueAnswer,
                        isTrueAnswer ? modelQuestion.getPoints() : -modelQuestion.getPoints());

                if (!isTrueAnswer) {
                    binding.tvCompleteSolving.setText(firstTrueAnswer);
                }
            }
        });
    }
}
