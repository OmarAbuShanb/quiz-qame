package com.omarshanab.quizgame.database;

import android.app.Application;

import com.omarshanab.quizgame.interfaces.EventDatabaseListener;
import com.omarshanab.quizgame.interfaces.GetIntegerListener;
import com.omarshanab.quizgame.interfaces.GetLevelsListener;
import com.omarshanab.quizgame.interfaces.GetQuestionListener;
import com.omarshanab.quizgame.interfaces.GetQuestionsListener;
import com.omarshanab.quizgame.interfaces.GetUserListener;

import java.util.Date;
import java.util.List;

public class DatabaseRepository {
    DaoUser daoUser;
    DaoLevel daoLevel;
    DaoQuestion daoQuestion;
    DaoHistory daoHistory;
    private static DatabaseRepository mInstance;

    private DatabaseRepository(Application application) {
        DatabaseApp db = DatabaseClient.getInstance(application).getAppDatabase();
        daoUser = db.daoUser();
        daoLevel = db.daoLevel();
        daoQuestion = db.daoQuestion();
        daoHistory = db.daoHistory();
    }

    public static synchronized DatabaseRepository getInstance(Application application) {
        if (mInstance == null) {
            mInstance = new DatabaseRepository(application);
        }
        return mInstance;
    }

    /////////////////////////////////////   User Repository   //////////////////////////////////////
    public void insertUser(ModelUser user, GetUserListener listener) {
        DatabaseClient.databaseWriteExecutor.execute(() -> {
            daoUser.insertUser(user);
            ModelUser modelUser = daoUser.getRememberMyUser();
            listener.onGetUser(modelUser);
        });
    }

    public void updateUser(int userId, String email, String fullName, String country, Date barthDate, int gender, EventDatabaseListener listener) {
        DatabaseClient.databaseWriteExecutor.execute(() -> {
            daoUser.updateUser(userId, email, fullName, country, ConverterDate.dataToLong(barthDate), gender);
            listener.onEvent();
        });
    }

    public void updateRememberMy(String email, boolean rememberMy) {
        DatabaseClient.databaseWriteExecutor.execute(() -> daoUser.updateRememberMy(email, rememberMy));
    }

    public void hasUser(String email, String password, GetUserListener listener) {
        DatabaseClient.databaseWriteExecutor.execute(() -> {
            ModelUser user = daoUser.hasUser(email, password);
            listener.onGetUser(user);
        });
    }

    public void getRememberMyUser(GetUserListener listener) {
        DatabaseClient.databaseWriteExecutor.execute(() -> {
            ModelUser user = daoUser.getRememberMyUser();
            listener.onGetUser(user);
        });
    }

    public void updatePassword(int userId, String oldPassword, String newPassword, GetIntegerListener listener) {
        DatabaseClient.databaseWriteExecutor.execute(() -> {
            int result = daoUser.updatePassword(userId, oldPassword, newPassword);
            listener.onGetInt(result);
        });
    }

    /////////////////////////////////////   Level Repository   /////////////////////////////////////

    public void getAllLevels(GetLevelsListener listener) {
        DatabaseClient.databaseWriteExecutor.execute(() -> {
            List<ModelLevel> levels = daoLevel.getAllLevels();
            listener.onGetLevels(levels);
        });
    }

    /////////////////////////////////////   Questions Repository   /////////////////////////////////

    public void getQuestionsLevel(int userId, int levelNo, GetQuestionsListener listener) {
        DatabaseClient.databaseWriteExecutor.execute(() -> {
            // قائمة الاسئلة
            List<ModelQuestion> questions = daoQuestion.getQuestionsLevel(levelNo);
//            Collections.shuffle(questions);

            // جلب موقع اخر سؤال تم حله و0 ان لم يوجد
            int lastQuestionSolveInLevelIndex = 0;
            ModelHistory lastQuestionSolveInLeve = daoHistory.getLastQuestionSolveInLevelId(userId, levelNo);
            if (lastQuestionSolveInLeve != null) {
                // البحث في قائمة الاسئلة عن موقع اخر سؤال محلول
                for (int i = 0; i < questions.size(); i++) {
                    if (questions.get(i).getQuestionId() == lastQuestionSolveInLeve.getQuestionId()) {
                        lastQuestionSolveInLevelIndex = i;
                    }
                }
            }

            // 2 عدد المرات المسموح به للتخطي
            int skipCount = 2 - daoHistory.getSkipCount(userId, levelNo);

            listener.onGetQuestions(questions, lastQuestionSolveInLevelIndex, skipCount);
        });
    }

    /////////////////////////////////////   History Repository  ////////////////////////////////////
    public void insertHistoryGame(ModelHistory modelHistory) {
        DatabaseClient.databaseWriteExecutor.execute(() -> daoHistory.insertHistoryGame(modelHistory));
    }

    public void deleteHistoryLevel(int userId, int levelNo, GetIntegerListener listener) {
        DatabaseClient.databaseWriteExecutor.execute(() -> {
            daoHistory.deleteHistoryLevel(userId, levelNo);
            int result = daoHistory.getSumPoints(userId);
            listener.onGetInt(result);
        });
    }

    public void deleteAllHistoryUser(int userId, EventDatabaseListener listener) {
        DatabaseClient.databaseWriteExecutor.execute(() -> {
            daoHistory.deleteAllHistoryUser(userId);
            listener.onEvent();
        });
    }

    public void getSumPoints(int userId, GetIntegerListener listener) {
        DatabaseClient.databaseWriteExecutor.execute(() -> {
            int result = daoHistory.getSumPoints(userId);
            listener.onGetInt(result);
        });
    }

    public void getLastUserLevel(int userId, GetIntegerListener listener) {
        DatabaseClient.databaseWriteExecutor.execute(() -> {
            List<ModelHistory> historyList = daoHistory.getLastUserLevel(userId);
            if (historyList.isEmpty()) {
                listener.onGetInt(1);
            } else {
                // اخر لعبة لعبها المستخدم
                ModelHistory lastGame = historyList.get(0);
                // اخر سؤال في المرحلة
                ModelQuestion lastQuestionInLevel = daoQuestion.getLastQuestionInLevel(lastGame.getLevelNo());
                // اذا كان id ااخر لعبة == id اخر سؤال في المرحلة
                if (lastGame.getQuestionId() == lastQuestionInLevel.getQuestionId()) {
                    // اخر مرحلة
                    ModelLevel lastLevel = daoLevel.getLastLevel();
                    // اذا كانت اخر مرحلة في اللعبة يرجع رقمها
                    if (lastGame.getLevelNo() == lastLevel.getLevelNo()) {
                        listener.onGetInt(lastGame.getLevelNo());
                    } else {
                        // والا يرجع رقم المرحلة الي بعدها
                        listener.onGetInt(lastGame.getLevelNo() + 1);
                    }
                } else {
                    // اذا لم يكن اخر سؤال في المرحلة يرجع رقم المرحلة مباشرة
                    listener.onGetInt(lastGame.getLevelNo());
                }
            }
        });
    }
}
