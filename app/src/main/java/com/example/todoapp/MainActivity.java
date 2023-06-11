package com.example.todoapp;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;


import com.example.todoapp.databinding.ActivityMainBinding;
import com.example.todoapp.fragments.SaveFragment;
import com.example.todoapp.fragments.TasksFragment;

import android.view.Menu;
import android.view.MenuItem;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    SQDataBase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_main, new TasksFragment())
                .commit();

        binding.fabAdd.setOnClickListener(view -> getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_main, new SaveFragment())
                .addToBackStack(null)
                .commit());

        db = new SQDataBase(this, "tasks", null, 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_close) {
            ExitDialog(this);
            return true;
        }else  if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new TasksFragment())
                    .commit();
        } else {
            super.onBackPressed();
        }
    }


    public static void ExitDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("😢 Exit App!");
        builder.setMessage("Are you sure you want to exit?");
        builder.setPositiveButton("Yes", (dialogInterface, i) -> System.exit(0));
        builder.setNegativeButton("No", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}