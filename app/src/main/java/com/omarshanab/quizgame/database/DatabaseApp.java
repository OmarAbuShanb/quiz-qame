package com.omarshanab.quizgame.database;


import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ModelUser.class,
                ModelLevel.class,
                ModelQuestion.class,
                ModelHistory.class},
        version = 1, exportSchema = false)
public abstract class DatabaseApp extends RoomDatabase {
    public abstract DaoLevel daoLevel();

    public abstract DaoQuestion daoQuestion();

    public abstract DaoHistory daoHistory();

    public abstract DaoUser daoUser();
}
