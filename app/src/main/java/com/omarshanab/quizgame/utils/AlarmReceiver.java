package com.omarshanab.quizgame.utils;

import static android.os.Build.VERSION_CODES.O;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.omarshanab.quizgame.MainActivity;
import com.omarshanab.quizgame.R;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        setUpNotification(context, 132);
    }

    public void setUpNotification(Context context, int notificationId) {
        String NOT_CHANNEL_NAME = "channel name";
        String NOT_BIG_CONTENT = "طولت علينا خلنا نشوفك دايما يغالي❤";

        Intent intent = new Intent(context, MainActivity.class);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notBuilder = new NotificationCompat.Builder(context, String.valueOf(notificationId));
        notBuilder
                .setContentTitle("تذكير الغوالي")
                .setContentText(NOT_BIG_CONTENT)
                .setSmallIcon(R.drawable.ic_check)
                .setPriority(Notification.PRIORITY_MAX)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        playDefaultRingtone(context);

        NotificationManagerCompat notManager = NotificationManagerCompat.from(context);
        if (Build.VERSION.SDK_INT >= O) {
            if (notManager.getNotificationChannel(NOT_CHANNEL_NAME) == null) {
                NotificationChannel notChannel =
                        new NotificationChannel(String.valueOf(notificationId), NOT_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                notChannel.setDescription("NOT_CHANNEL_Description");
                notManager.createNotificationChannel(notChannel);
            }
        }
        notManager.notify(notificationId, notBuilder.build());
    }

    public void playDefaultRingtone(Context context) {
        try {
            Uri defaultNot = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, defaultNot);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}