package com.omarshanab.quizgame.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.omarshanab.quizgame.adapter.question_view_holder.CompleteViewHolder;
import com.omarshanab.quizgame.adapter.question_view_holder.MultipleChoiceViewHolder;
import com.omarshanab.quizgame.adapter.question_view_holder.TrueOrFalseViewHolder;
import com.omarshanab.quizgame.database.ModelQuestion;
import com.omarshanab.quizgame.databinding.LayoutCompleteBinding;
import com.omarshanab.quizgame.databinding.LayoutMultipleChoiceBinding;
import com.omarshanab.quizgame.databinding.LayoutTrueOrFalseBinding;
import com.omarshanab.quizgame.interfaces.ListenerAnswer;

import java.util.List;

public class QuestionsPagerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<ModelQuestion> modelQuestions;
    public static ListenerAnswer listenerAnswer;

    public static void setListenerAnswer(ListenerAnswer listenerAnswer) {
        QuestionsPagerAdapter.listenerAnswer = listenerAnswer;
    }

    public QuestionsPagerAdapter(List<ModelQuestion> modelQuestions) {
        this.modelQuestions = modelQuestions;
    }

    @Override
    public int getItemViewType(int position) {
//        1 True of False
//        2 Choose the correct answer
//        3 Fill the answer
        return modelQuestions.get(position).getPatternId();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case 1:
                LayoutTrueOrFalseBinding trueOrFalseLayout = LayoutTrueOrFalseBinding.inflate(inflater, parent, false);
                return new TrueOrFalseViewHolder(trueOrFalseLayout);
            case 2:
                LayoutMultipleChoiceBinding chooseLayout = LayoutMultipleChoiceBinding.inflate(inflater, parent, false);
                return new MultipleChoiceViewHolder(chooseLayout);
            default:
                LayoutCompleteBinding completeLayout = LayoutCompleteBinding.inflate(inflater, parent, false);
                return new CompleteViewHolder(completeLayout);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 1:
                if (holder instanceof TrueOrFalseViewHolder) {
                    ((TrueOrFalseViewHolder) holder).bind(modelQuestions.get(position));
                }
                break;
            case 2:
                if (holder instanceof MultipleChoiceViewHolder) {
                    ((MultipleChoiceViewHolder) holder).bind(modelQuestions.get(position));
                }
                break;
            default:
                if (holder instanceof CompleteViewHolder) {
                    ((CompleteViewHolder) holder).bind(modelQuestions.get(position));
                }

        }
    }

    @Override
    public int getItemCount() {
        return modelQuestions.size();
    }
}