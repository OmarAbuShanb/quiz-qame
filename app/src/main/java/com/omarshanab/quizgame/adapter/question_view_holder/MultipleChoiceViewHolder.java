package com.omarshanab.quizgame.adapter.question_view_holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.omarshanab.quizgame.R;
import com.omarshanab.quizgame.adapter.QuestionsPagerAdapter;
import com.omarshanab.quizgame.database.ModelQuestion;
import com.omarshanab.quizgame.databinding.LayoutMultipleChoiceBinding;

import java.util.ArrayList;
import java.util.List;

public class MultipleChoiceViewHolder extends RecyclerView.ViewHolder {
    LayoutMultipleChoiceBinding binding;
    Context context;
    ModelQuestion modelQuestion;

    public MultipleChoiceViewHolder(LayoutMultipleChoiceBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
        context = itemView.getContext();
    }

    public void bind(ModelQuestion modelQuestion) {
        this.modelQuestion = modelQuestion;
        binding.answer1.setText(modelQuestion.getAnswer1());
        binding.answer2.setText(modelQuestion.getAnswer2());
        binding.answer3.setText(modelQuestion.getAnswer3());
        binding.answer4.setText(modelQuestion.getAnswer4());

        binding.answer1.setOnClickListener(v -> validate(binding.answer1));
        binding.answer2.setOnClickListener(v -> validate(binding.answer2));
        binding.answer3.setOnClickListener(v -> validate(binding.answer3));
        binding.answer4.setOnClickListener(v -> validate(binding.answer4));

    }

    void validate(TextView textView) {
        String answer = textView.getText().toString();
        String resNameTVPPressed = context.getResources().getResourceEntryName(textView.getId());
        int numTVPPressed = Integer.parseInt(resNameTVPPressed.substring(resNameTVPPressed.length() - 1));
        ImageView[] imageViews = new ImageView[]{binding.ivAns1, binding.ivAns2, binding.ivAns3, binding.ivAns4};
        textView.setTextColor(context.getResources().getColor(R.color.white));

        if (answer.equals(modelQuestion.getTrueAnswer())) {
            QuestionsPagerAdapter.listenerAnswer.onAnswerQuestion(modelQuestion.getQuestionId(),
                    true,
                    modelQuestion.getPoints());
            textView.setBackgroundResource(R.drawable.true_case);
            imageViews[numTVPPressed - 1].setImageResource(R.drawable.ic_check_circle);
        } else {
            QuestionsPagerAdapter.listenerAnswer.onAnswerQuestion(modelQuestion.getQuestionId(),
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
                if (tv.getText().toString().equals(modelQuestion.getTrueAnswer())) {
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
