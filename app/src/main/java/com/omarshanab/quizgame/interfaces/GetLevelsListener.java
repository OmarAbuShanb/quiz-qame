package com.omarshanab.quizgame.interfaces;


import com.omarshanab.quizgame.database.ModelLevel;

import java.util.List;

public interface GetLevelsListener {
    void onGetLevels(List<ModelLevel> levels);
}
