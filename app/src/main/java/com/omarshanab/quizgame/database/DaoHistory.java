package com.omarshanab.quizgame.database;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DaoHistory {

    @Insert
    void insertHistoryGame(ModelHistory... modelHistory);

    @Query("delete FROM History WHERE user_id = :userId and level_no = :levelNo")
    void deleteHistoryLevel(int userId, int levelNo);

    @Query("delete FROM History WHERE user_id = :userId")
    void deleteAllHistoryUser(int userId);

    @Query("SELECT COUNT(*) FROM History WHERE user_id = :userId and level_no = :levelNo and status = -1")
    int getSkipCount(int userId, int levelNo);

    @Query("SELECT SUM(question_points) FROM History WHERE user_id = :userId")
    int getSumPoints(int userId);

    @Query("SELECT * FROM History WHERE user_id = :userId ORDER BY level_no DESC LIMIT 1")
    List<ModelHistory> getLastUserLevel(int userId);

    @Query("SELECT * FROM History WHERE user_id = :userId and level_no = :levelNo ORDER BY question_id DESC LIMIT 1")
    ModelHistory getLastQuestionSolveInLevelId(int userId, int levelNo);
}
