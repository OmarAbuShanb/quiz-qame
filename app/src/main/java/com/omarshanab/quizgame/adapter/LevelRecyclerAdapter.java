package com.omarshanab.quizgame.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.omarshanab.quizgame.R;
import com.omarshanab.quizgame.database.ModelLevel;
import com.omarshanab.quizgame.databinding.ItemLevelBinding;
import com.omarshanab.quizgame.interfaces.OnClickLevelListener;

import java.util.List;

public class LevelRecyclerAdapter extends RecyclerView.Adapter<LevelRecyclerAdapter.LevelViewHolder> {
    List<ModelLevel> levels;
    int sumPoints;
    Drawable drawableLockLevel;
    Drawable drawableUnLockLevel;
    OnClickLevelListener onClickLevelListener;

    public LevelRecyclerAdapter(List<ModelLevel> levels, int sumPoints, Drawable drawableLockLevel, Drawable drawableUnLockLevel, OnClickLevelListener onClickLevelListener) {
        this.levels = levels;
        this.sumPoints = sumPoints;
        this.drawableLockLevel = drawableLockLevel;
        this.drawableUnLockLevel = drawableUnLockLevel;
        this.onClickLevelListener = onClickLevelListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void refreshRecycler(int sumPoints) {
        this.sumPoints = sumPoints;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LevelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_level, null, false);
        return new LevelViewHolder(view);
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

        public LevelViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemLevelBinding.bind(itemView);
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
                    if (isLastLevel || sumPoints < pointsNextLevel){
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