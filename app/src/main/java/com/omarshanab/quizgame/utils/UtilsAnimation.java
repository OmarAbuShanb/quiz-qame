package com.omarshanab.quizgame.utils;

import android.app.Activity;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ProgressBar;

import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

import java.util.Timer;
import java.util.TimerTask;

public class UtilsAnimation {
    public static final long animationDuration = 180;


    public static int progressValue = 20;
    static boolean isProgressRunning = false;

    public static void animationProgress(boolean isNext, ProgressBar progressBar, Activity activity) {
        if (!isProgressRunning) {
            isProgressRunning = true;
            final int[] numTimes = {20};
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (numTimes[0] != 0) {
                        if (isNext) {
                            progressValue++;
                        } else {
                            progressValue--;
                        }
                        activity.runOnUiThread(() -> progressBar.setProgress(progressValue));
                        numTimes[0] = numTimes[0] - 1;
                    } else {
                        isProgressRunning = false;
                        timer.cancel();
                    }
                }
                // بقسم وقت كل لفة على عدد اللفات
            }, 0, animationDuration / numTimes[0]);
        }
    }

    public static void scaleViewAnimation(View view) {
        float form = 1.0f;
        float to = 0.9f;
        ScaleAnimation translate = new ScaleAnimation(form, to, form, to,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        translate.setDuration(200);
        translate.setFillAfter(true);
        translate.setInterpolator(new OvershootInterpolator());
        view.startAnimation(translate);
    }
}
