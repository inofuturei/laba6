package com.example.lab6;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ReminderViewModel viewModel;
    private ReminderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация базы данных
        AppDatabase database = AppDatabase.getInstance(this);

        // Инициализация ViewModel через Factory
        ReminderViewModelFactory factory = new ReminderViewModelFactory(database);
        viewModel = new ViewModelProvider(this, factory).get(ReminderViewModel.class);

        // Настройка RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view_reminders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReminderAdapter(new ArrayList<>(), this::deleteReminder);
        recyclerView.setAdapter(adapter);

        // Наблюдение за данными
        viewModel.getReminders().observe(this, reminders -> {
            if (reminders != null) {
                adapter.updateData(reminders);
            }
        });

        // Кнопка добавления
        Button addReminderButton = findViewById(R.id.btn_add_reminder);
        addReminderButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AddReminderActivity.class));
        });
    }

    private void deleteReminder(Reminder reminder) {
        viewModel.deleteReminder(reminder);
        Utils.cancelReminder(this, reminder);
        Toast.makeText(this, "Напоминание удалено", Toast.LENGTH_SHORT).show();
    }
}
