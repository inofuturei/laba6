package com.example.lab6;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ReminderDao {
    @Insert
    long insertReminder(Reminder reminder);

    @Query("SELECT * FROM reminders")
    List<Reminder> getAllReminders();

    @Delete
    void deleteReminder(Reminder reminder);
}


