package com.omarshanab.quizgame.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.omarshanab.quizgame.utils.Utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseClient {
    private static DatabaseClient mInstance;
    private static volatile DatabaseApp databaseApp;
    public static ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(4);

    private DatabaseClient(Context mCtx) {
        databaseApp = Room.databaseBuilder(mCtx.getApplicationContext(),
                        DatabaseApp.class, "GameQuizDB")
                .addCallback(callback(mCtx))
                .build();
    }

    public static synchronized DatabaseClient getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new DatabaseClient(mCtx);
        }
        return mInstance;
    }

    public DatabaseApp getAppDatabase() {
        return databaseApp;
    }

    RoomDatabase.Callback callback(Context mCtx) {
        return new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                databaseWriteExecutor.execute(() -> {
                    DaoQuestion daoQuestion = databaseApp.daoQuestion();
                    DaoLevel daoLevel = databaseApp.daoLevel();

                    Utils.getDataFromJSON(mCtx);
                    if (Utils.modelQuestionArrayList != null && Utils.modelLevelArrayList != null) {
                        for (ModelLevel modelLevel : Utils.modelLevelArrayList) {
                            daoLevel.insertLevel(modelLevel);
                        }
                        for (ModelQuestion modelQuestion : Utils.modelQuestionArrayList) {
                            daoQuestion.insertQuestion(modelQuestion);
                        }
                    }
                });
            }
        };
    }
}
