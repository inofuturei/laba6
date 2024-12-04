package com.example.lab6;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

public class Utils {
    @SuppressLint("ScheduleExactAlarm")
    public static void scheduleReminder(Context context, Reminder reminder) {
        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.putExtra("title", reminder.getTitle());
        intent.putExtra("message", reminder.getMessage());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                reminder.getId(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        reminder.getTime(),
                        pendingIntent
                );
            } else {
                alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        reminder.getTime(),
                        pendingIntent
                );
            }
        }
    }

    public static void cancelReminder(Context context, Reminder reminder) {
        Intent intent = new Intent(context, ReminderReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                reminder.getId(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    // Проверяет, включены ли уведомления для приложения
    public static boolean areNotificationsEnabled(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return notificationManager != null && notificationManager.areNotificationsEnabled();
        }
        return true; // На версиях ниже Android 8 уведомления всегда включены
    }

    // Открывает экран настроек уведомлений для приложения
    public static void openNotificationSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
        context.startActivity(intent);
    }

    // Проверяет, включен ли канал уведомлений
    public static boolean isChannelEnabled(Context context, String channelId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = notificationManager.getNotificationChannel(channelId);
            return channel != null && channel.getImportance() != NotificationManager.IMPORTANCE_NONE;
        }
        return true; // Для версий ниже Android 8 проверка не требуется
    }
}



