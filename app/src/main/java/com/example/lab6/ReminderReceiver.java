package com.example.lab6;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;

public class ReminderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("title");
        String message = intent.getStringExtra("message");

        if (!Utils.areNotificationsEnabled(context)) {
            Toast.makeText(context, "Уведомления отключены. Пожалуйста, включите их в настройках.", Toast.LENGTH_LONG).show();
            Utils.openNotificationSettings(context);
            return;
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "reminders",
                    "Напоминания",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Канал для напоминаний");
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        Notification notification = new NotificationCompat.Builder(context, "reminders")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();

        if (notificationManager != null) {
            notificationManager.notify((int) System.currentTimeMillis(), notification);
        }
    }
}




