package com.omarshanab.quizgame.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DaoLevel {

    @Insert
    void insertLevel(ModelLevel modelLevel);

    @Query("select * from Level")
    List<ModelLevel> getAllLevels();

    @Query("SELECT * FROM Level ORDER BY level_no DESC LIMIT 1")
    ModelLevel getLastLevel();
}
