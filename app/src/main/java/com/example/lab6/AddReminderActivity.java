package com.example.lab6;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.Calendar;

public class AddReminderActivity extends AppCompatActivity {
    private EditText titleInput;
    private EditText messageInput;
    private Button pickDateButton;
    private Button pickTimeButton;
    private Button saveButton;
    private Calendar calendar = Calendar.getInstance();
    private ReminderViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        // Инициализация базы данных
        AppDatabase database = AppDatabase.getInstance(this);

        // Использование ReminderViewModelFactory для создания ViewModel
        ReminderViewModelFactory factory = new ReminderViewModelFactory(database);
        viewModel = new ViewModelProvider(this, factory).get(ReminderViewModel.class);

        // Настройка UI
        titleInput = findViewById(R.id.et_title);
        messageInput = findViewById(R.id.et_message);
        pickDateButton = findViewById(R.id.btn_pick_date);
        pickTimeButton = findViewById(R.id.btn_pick_time);
        saveButton = findViewById(R.id.btn_save_reminder);

        pickDateButton.setOnClickListener(v -> {
            DatePickerDialog datePicker = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePicker.show();
        });

        pickTimeButton.setOnClickListener(v -> {
            TimePickerDialog timePicker = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
            timePicker.show();
        });

        saveButton.setOnClickListener(v -> saveReminder());
    }

    private void saveReminder() {
        String title = titleInput.getText().toString();
        String message = messageInput.getText().toString();
        long time = calendar.getTimeInMillis();

        if (title.isEmpty() || message.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        Reminder reminder = new Reminder(0, title, message, time);
        viewModel.addReminder(reminder); // Сохранение в базу данных
        Utils.scheduleReminder(this, reminder); // Установка напоминания

        Toast.makeText(this, "Напоминание добавлено", Toast.LENGTH_SHORT).show();
        finish();
    }
}


