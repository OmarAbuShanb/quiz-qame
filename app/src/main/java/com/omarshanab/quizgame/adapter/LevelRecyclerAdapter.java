package com.omarshanab.quizgame.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.omarshanab.quizgame.R;
import com.omarshanab.quizgame.database.ModelLevel;
import com.omarshanab.quizgame.databinding.ItemLevelBinding;
import com.omarshanab.quizgame.interfaces.OnClickLevelListener;
import com.omarshanab.quizgame.utils.Utils;

import java.util.List;

public class LevelRecyclerAdapter extends RecyclerView.Adapter<LevelRecyclerAdapter.LevelViewHolder> {
    List<ModelLevel> levels;
    int sumPoints;
    OnClickLevelListener onClickLevelListener;

    Drawable drawableLockLevel;
    Drawable drawableUnLockLevel;

    public LevelRecyclerAdapter(
            Context context,
            List<ModelLevel> levels,
            int sumPoints,
            OnClickLevelListener onClickLevelListener
    ) {
        this.levels = levels;
        this.sumPoints = sumPoints;
        this.onClickLevelListener = onClickLevelListener;

        drawableLockLevel = Utils.levelViewDrawable(context, R.color.gray);
        drawableUnLockLevel = Utils.levelViewDrawable(context, R.color.primary_color);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void refreshRecycler(int sumPoints) {
        this.sumPoints = sumPoints;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LevelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLevelBinding binding = ItemLevelBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new LevelViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LevelViewHolder holder, int position) {
        boolean isLastLevel = position == levels.size() - 1;
        int pointsNextLevel = !isLastLevel ? levels.get(position + 1).getUnlockPoints() : 0;
        ModelLevel level = levels.get(position);
        holder.bind(level, sumPoints, level.getUnlockPoints() <= sumPoints ? drawableUnLockLevel : drawableLockLevel,
                onClickLevelListener, isLastLevel, pointsNextLevel);
    }

    @Override
    public int getItemCount() {
        return levels.size();
    }

    static class LevelViewHolder extends RecyclerView.ViewHolder {
        ItemLevelBinding binding;
        Context context;

        public LevelViewHolder(@NonNull ItemLevelBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            context = itemView.getContext();
        }

        @SuppressLint("ClickableViewAccessibility")
        void bind(ModelLevel level, int sumPoints, Drawable drawable,
                  OnClickLevelListener onClickLevelListener, boolean isLastLevel, int pointsNextLevel) {
            if (sumPoints >= level.getUnlockPoints()) {
                binding.ivLock.setImageResource(R.drawable.unlock);
                binding.getRoot().setOnClickListener(v -> {
                    // ازا بقدر افوت الي بعدها سكرها
                    // ازا كان اخر مرحلة افتحها
                    if (isLastLevel || sumPoints < pointsNextLevel) {
                        onClickLevelListener.onClick(level.getLevelNo(), isLastLevel, pointsNextLevel);
                    } else {
                        Toast.makeText(context, R.string.you_have_finished_this_level_before, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                binding.ivLock.setImageResource(R.drawable.lock);
                binding.getRoot().setOnTouchListener((v, e) -> false);
            }

            binding.tvLevelName.setText(String.valueOf(level.getLevelNo()));
            binding.tvScoreRequired.setText(context.getString(R.string.required_point) + " " + level.getUnlockPoints());
            binding.parentConstraintLayout.setBackground(drawable);
        }
    }
}