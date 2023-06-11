package com.example.todoapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.todoapp.R;
import com.example.todoapp.SQDataBase;
import com.example.todoapp.utils.TaskAdapter;

public class TasksFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tasks, container, false);

        SQDataBase db = new SQDataBase(getActivity(), "tasks", null, 1);

        ListView taskContainer = root.findViewById(R.id.task_container);

        TaskAdapter taskAdapter = new TaskAdapter(getActivity(), db);

        taskContainer.setAdapter(taskAdapter);

        return root;
    }
}
