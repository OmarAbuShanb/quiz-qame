package com.omarshanab.quizgame.interfaces;


import com.omarshanab.quizgame.database.ModelQuestion;

import java.util.List;

public interface GetQuestionsListener {
    void onGetQuestions(List<ModelQuestion> questions,int lastQuestionSolveInLevelIndex,int skipCount);
}
