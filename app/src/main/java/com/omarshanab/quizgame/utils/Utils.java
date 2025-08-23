package com.omarshanab.quizgame.utils;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import com.omarshanab.quizgame.R;
import com.omarshanab.quizgame.database.ModelLevel;
import com.omarshanab.quizgame.database.ModelQuestion;
import com.omarshanab.quizgame.database.ModelUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;

public class Utils {
    public static int userId;
    public static String fullName;
    public static String email;

    public static void initUserData(ModelUser user) {
        userId = user.getUserId();
        fullName = user.getFullName();
        email = user.getEmail();
    }

    public static boolean allowSound;
    public static MediaPlayer loopMP;

    public static void playOrResumeOrPauseLoopSound(Context ctx) {
        if (allowSound && loopMP == null) {
            loopMP = MediaPlayer.create(ctx, R.raw.pubg);
            loopMP.setLooping(true);
            loopMP.start();
        }
    }
//    private static int loopMPLastPositions;
//    public static void playOrResumeOrPauseLoopSound(Context ctx,boolean isPause) {
//        if (allowSound) {
//            if (loopMP != null) {
//                if (loopMP.isPlaying() && isPause) {
//                    loopMP.pause();
//                    loopMPLastPositions = loopMP.getCurrentPosition();
//                } else {
//                    loopMP = MediaPlayer.create(ctx, R.raw.pubg);
//                    loopMP.seekTo(loopMPLastPositions);
//                    loopMP.start();
//                }
//            } else {
//                loopMP = MediaPlayer.create(ctx, R.raw.pubg);
//                loopMP.setLooping(true);
//                loopMP.start();
//            }
//        }
//    }

    public static MediaPlayer mp;

    public static void playSound(Context ctx, int rowMedia) {
//        if (allowSound) {
//            if (mp != null && mp.isPlaying()) {
//                mp.stop();
//            }
//
//            mp = MediaPlayer.create(ctx, rowMedia);
//            mp.setOnCompletionListener(mp -> {
//                mp.reset();
//                mp.release();
//            });
//            mp.start();
//        }
    }

    public static boolean allowNotification;

    public static void setAlarmNotification(Context context) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) - 1);
        Intent intent = new Intent(context, AlarmReceiver.class);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                (60 * 2) * 1000, pendingIntent);
    }

    public static void chancelAlarmNotification(Context context) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    static Toast toast;

    public static void showToast(View view, Context context) {
        if (toast != null)
            toast.cancel();
        toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }

    public static GradientDrawable gradientDrawableView(
            Context context, int backgroundColor, float cornerRadius, int strokeWidth, int strokeColor) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(backgroundColor);
        gradientDrawable.setCornerRadius(convertDpToPixel(cornerRadius, context));
        gradientDrawable.setStroke(convertDpToPixel(strokeWidth, context), strokeColor);
        return gradientDrawable;
    }

    public static int convertDpToPixel(float dp, Context context) {
        return Math.round(dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static String loadJSONFromAsset(Context context) {
        String json;
        try {
            InputStream inputStream = context.getAssets().open("puzzleGameData.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            // UTF_8 ترميز الحروف
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static ArrayList<ModelLevel> modelLevelArrayList;
    public static ArrayList<ModelQuestion> modelQuestionArrayList;

    public static void getDataFromJSON(Context context) {
        modelLevelArrayList = new ArrayList<>();
        modelQuestionArrayList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(loadJSONFromAsset(context));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                // add to level model
                int levelNo = jsonObject.getInt("level_no");
                int unlockPoints = jsonObject.getInt("unlock_points");
                modelLevelArrayList.add(new ModelLevel(levelNo, unlockPoints));

                // add to questions model
                JSONArray array = jsonObject.getJSONArray("questions");
                for (int j = 0; j < array.length(); j++) {
                    JSONObject jsonObject1 = array.getJSONObject(j);
                    int id = jsonObject1.getInt("id");
                    String title = jsonObject1.getString("title");
                    String answer1 = jsonObject1.getString("answer_1");
                    String answer2 = jsonObject1.getString("answer_2");
                    String answer3 = jsonObject1.getString("answer_3");
                    String answer4 = jsonObject1.getString("answer_4");
                    String trueAnswer = jsonObject1.getString("true_answer");
                    int points = jsonObject1.getInt("points");
                    int duration = jsonObject1.getInt("duration");
                    int patternId = jsonObject1.getJSONObject("pattern").getInt("pattern_id");
                    String hint = jsonObject1.getString("hint");
                    modelQuestionArrayList.add(new ModelQuestion(id, title, answer1, answer2, answer3, answer4,
                            trueAnswer, points, duration, patternId, hint, levelNo));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
