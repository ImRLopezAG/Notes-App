package com.example.todoapp.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.todoapp.R;
import com.example.todoapp.SQDataBase;
import com.example.todoapp.fragments.SaveFragment;


public class TaskAdapter extends ArrayAdapter<Task> {
    SQDataBase db;
    public TaskAdapter(Context context, SQDataBase db) {
        super(context, 0, db.GetAllTasks());
        this.db = db;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Task task = getItem(position);

        if (convertView == null) {
            TaskComponent taskComponent = new TaskComponent(getContext(), null);
            convertView = taskComponent;
            convertView.setTag(taskComponent);
        }

        TaskComponent taskComponent = (TaskComponent) convertView.getTag();
        taskComponent.Build(task);

        Update(task, taskComponent);
        Delete(task, taskComponent);
        OnClick(taskComponent);

        return convertView;
    }

    @Override
    public int getCount() {
        return db.GetAllTasks().size();
    }

    private void Update(Task task, TaskComponent component) {
        component.btnEdit.setOnClickListener(view -> {
            SaveFragment saveFragment = new SaveFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("id", task.getId());
            saveFragment.setArguments(bundle);

            ((AppCompatActivity) getContext()).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, saveFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    private void Delete(Task task, TaskComponent component) {
        component.btnDelete.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Delete task");
            builder.setMessage("Are you sure you want to delete this task?");
            builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                db.DeleteTask(task.getId());
                remove(task);
                notifyDataSetChanged();
                Toast.makeText(getContext(), "Task deleted", Toast.LENGTH_SHORT).show();
            });
            builder.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    private void OnClick(TaskComponent component) {
        component.setOnClickListener(view -> {
            component.setTaskDescriptionVisibility(component.getTaskDescriptionVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            component.setArrowRotation(component.getTaskDescriptionVisibility() == View.GONE ? 0 : 180);
        });
    }
}

