package com.omarshanab.quizgame.interfaces;

import com.omarshanab.quizgame.database.ModelQuestion;

import java.util.List;

public interface OnClickLevelListener {
    void onClick(int levelNo, boolean isLastLevel, int pointsNextLevel);
}
