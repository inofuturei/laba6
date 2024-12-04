package com.example.lab6;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {
    private List<Reminder> reminders;
    private final Consumer<Reminder> onDelete;

    public ReminderAdapter(List<Reminder> reminders, Consumer<Reminder> onDelete) {
        this.reminders = reminders;
        this.onDelete = onDelete;
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reminder, parent, false);
        return new ReminderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {
        Reminder reminder = reminders.get(position);
        holder.bind(reminder);
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }

    public void updateData(List<Reminder> newReminders) {
        this.reminders = newReminders;
        notifyDataSetChanged();
    }

    class ReminderViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleView;
        private final TextView timeView;
        private final Button deleteButton;

        public ReminderViewHolder(@NonNull View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.tv_title);
            timeView = itemView.findViewById(R.id.tv_time);
            deleteButton = itemView.findViewById(R.id.btn_delete);
        }

        public void bind(Reminder reminder) {
            titleView.setText(reminder.getTitle());
            timeView.setText(DateFormat.getDateTimeInstance().format(new Date(reminder.getTime())));
            deleteButton.setOnClickListener(v -> onDelete.accept(reminder));
        }
    }
}

