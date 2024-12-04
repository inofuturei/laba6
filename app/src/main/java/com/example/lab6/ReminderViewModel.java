package com.example.lab6;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.Executors;

public class ReminderViewModel extends ViewModel {
    private final MutableLiveData<List<Reminder>> reminders = new MutableLiveData<>();
    private final AppDatabase database;

    public ReminderViewModel(AppDatabase database) {
        this.database = database;
        loadReminders();
    }

    public LiveData<List<Reminder>> getReminders() {
        return reminders;
    }

    public void loadReminders() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Reminder> data = database.reminderDao().getAllReminders();
            reminders.postValue(data);
        });
    }

    public void addReminder(Reminder reminder) {
        Executors.newSingleThreadExecutor().execute(() -> {
            database.reminderDao().insertReminder(reminder);
            loadReminders();
        });
    }

    public void deleteReminder(Reminder reminder) {
        Executors.newSingleThreadExecutor().execute(() -> {
            database.reminderDao().deleteReminder(reminder);
            loadReminders();
        });
    }
}


