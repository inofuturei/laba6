package com.example.lab6;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ReminderViewModelFactory implements ViewModelProvider.Factory {
    private final AppDatabase database;

    public ReminderViewModelFactory(AppDatabase database) {
        this.database = database;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ReminderViewModel.class)) {
            return (T) new ReminderViewModel(database);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
