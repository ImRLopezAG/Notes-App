package com.example.todoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.todoapp.utils.Task;

import java.util.ArrayList;
import java.util.List;

public class SQDataBase extends SQLiteOpenHelper {
    public SQDataBase(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS tasks (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, description TEXT, image BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE tasks");
        onCreate(db);
    }

    public List<Task> GetAllTasks() {
        List<Task> tasks = new ArrayList<>();
        Cursor cursor = getDatabase(0).rawQuery("SELECT * FROM tasks", null);

        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(cursor.getInt(0));
                task.setTitle(cursor.getString(1));
                task.setDescription(cursor.getString(2));
                task.setImage(cursor.getBlob(3));
                tasks.add(task);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return tasks;
    }

    public Task GetTask(int id) {
        Task task = null;
        Cursor cursor = getDatabase(0).rawQuery("SELECT * FROM tasks WHERE id = " + id, null);

        if (cursor.moveToFirst()) {
            do {
                task = new Task();
                task.setId(cursor.getInt(0));
                task.setTitle(cursor.getString(1));
                task.setDescription(cursor.getString(2));
                task.setImage(cursor.getBlob(3));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return task;
    }

    public void CreateTask(Task task) {
        try {
            ContentValues values = new ContentValues();
            values.put("title", task.getTitle());
            values.put("description", task.getDescription());
            values.put("image", task.getImage());
            getDatabase(1).insert("tasks", null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void UpdateTask(Task task) {
        Task oldTask = GetTask(task.getId());
        try  {
            ContentValues values = new ContentValues();
            values.put("title", task.getTitle());
            values.put("description", task.getDescription());

            if (task.getImage() != oldTask.getImage())
                values.put("image", task.getImage());

            getDatabase(1).update("tasks", values, "id = " + task.getId(), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void DeleteTask(int id) {
        try  {
            getDatabase(1).delete("tasks", "id = " + id, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SQLiteDatabase getDatabase(int option) {
        return option == 1 ? getWritableDatabase() : getReadableDatabase();
    }

}
