package com.omarshanab.quizgame.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DaoQuestion {

    @Insert
    void insertQuestion(ModelQuestion... modelQuestions);

    @Query("select * from Question where level_no = :levelNo")
    List<ModelQuestion> getQuestionsLevel(int levelNo);

    @Query("SELECT * FROM Question WHERE level_no = :levelNo ORDER BY question_id DESC LIMIT 1")
    ModelQuestion getLastQuestionInLevel(int levelNo);
}
