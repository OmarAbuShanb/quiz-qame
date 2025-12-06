package com.omarshanab.quizgame.adapter.question_view_holder;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.omarshanab.quizgame.R;
import com.omarshanab.quizgame.database.ModelQuestion;
import com.omarshanab.quizgame.databinding.LayoutMultipleChoiceBinding;
import com.omarshanab.quizgame.interfaces.OnListenerAnswer;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MultipleChoiceViewHolder extends RecyclerView.ViewHolder {
    LayoutMultipleChoiceBinding binding;
    Context context;
    ModelQuestion modelQuestion;
    private String trueAnswer;
    private List<String> answers;

    public MultipleChoiceViewHolder(LayoutMultipleChoiceBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
        context = itemView.getContext();
    }

    public void bind(ModelQuestion modelQuestion, OnListenerAnswer onListenerAnswer) {
        this.modelQuestion = modelQuestion;
        answers = new ArrayList<>();
        try {
            JSONArray jsonArrayAnswers = new JSONArray(modelQuestion.getAnswers());
            for (int i = 0; i < jsonArrayAnswers.length(); i++) {
                answers.add(jsonArrayAnswers.getString(i));
            }

            JSONArray jsonArrayTrueAnswers = new JSONArray(modelQuestion.getTrueAnswers());
            if (jsonArrayTrueAnswers.length() > 0) {
                trueAnswer = jsonArrayTrueAnswers.getString(0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        List<TextView> textViews = new ArrayList<>();
        textViews.add(binding.answer1);
        textViews.add(binding.answer2);
        textViews.add(binding.answer3);
        textViews.add(binding.answer4);

        for (int i = 0; i < textViews.size(); i++) {
            if (i < answers.size()) {
                textViews.get(i).setText(answers.get(i));
                textViews.get(i).setOnClickListener(v -> validate((TextView) v, onListenerAnswer));
            }
        }
    }

    void validate(TextView textView, OnListenerAnswer onListenerAnswer) {
        String answer = textView.getText().toString();
        String resNameTVPPressed = context.getResources().getResourceEntryName(textView.getId());
        int numTVPPressed = Integer.parseInt(resNameTVPPressed.substring(resNameTVPPressed.length() - 1));
        ImageView[] imageViews = new ImageView[]{binding.ivAns1, binding.ivAns2, binding.ivAns3, binding.ivAns4};
        textView.setTextColor(context.getResources().getColor(R.color.white));

        if (answer.equals(trueAnswer)) {
            onListenerAnswer.onAnswerQuestion(modelQuestion.getQuestionId(),
                    true,
                    modelQuestion.getPoints());
            textView.setBackgroundResource(R.drawable.true_case);
            imageViews[numTVPPressed - 1].setImageResource(R.drawable.ic_check_circle);
        } else {
            onListenerAnswer.onAnswerQuestion(modelQuestion.getQuestionId(),
                    false,
                    -modelQuestion.getPoints());
            textView.setBackgroundResource(R.drawable.false_case);
            imageViews[numTVPPressed - 1].setImageResource(R.drawable.ic_close);
            List<TextView> textViewList = new ArrayList<>();
            textViewList.add(binding.answer1);
            textViewList.add(binding.answer2);
            textViewList.add(binding.answer3);
            textViewList.add(binding.answer4);
            textViewList.remove(textView);
            for (TextView tv : textViewList) {
                if (tv.getText().toString().equals(trueAnswer)) {
                    String resNameImageTrue = context.getResources().getResourceEntryName(tv.getId());
                    int numTVImageTrue = Integer.parseInt(resNameImageTrue.substring(resNameTVPPressed.length() - 1));
                    imageViews[numTVImageTrue - 1].setImageResource(R.drawable.ic_check_circle);
                    tv.setBackgroundResource(R.drawable.true_case);
                    tv.setTextColor(context.getResources().getColor(R.color.white));
                }
            }
        }
    }
}
