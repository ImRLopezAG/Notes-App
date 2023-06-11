package com.example.todoapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.todoapp.R;

public class TaskComponent extends LinearLayout {

    private TextView tvTask, tvTaskDesc;
    private ImageView image, arrow;
    public ImageButton btnEdit, btnDelete;
    public TaskComponent(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @Override
    public void setId(int id) {
        btnEdit.setTag(id);
        btnDelete.setTag(id);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.task_item, this, true);

        tvTask = findViewById(R.id.tv_title);
        tvTaskDesc = findViewById(R.id.tv_description);
        image = findViewById(R.id.image);
        btnEdit = findViewById(R.id.btn_edit);
        btnDelete = findViewById(R.id.btn_delete);

        arrow = findViewById(R.id.arrow);

    }

    public int getTaskDescriptionVisibility() {
        return tvTaskDesc.getVisibility();
    }
    public void setTaskTitle(String title) {
        tvTask.setText(title);
    }

    public void setTaskDescription(String description) {
        tvTaskDesc.setText(description);
    }

    public void setTaskImage (byte[] imageByte){
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
        image.setImageBitmap(bitmap);
    }

    public void setTaskDescriptionVisibility(int visibility) {
        tvTaskDesc.setVisibility(visibility);
    }

    public void setArrowRotation(int rotation){
        arrow.setRotation(rotation);
    }

    public void Build(Task task){
        setTaskTitle(task.getTitle());
        setTaskDescription(task.getDescription());
        setTaskImage(task.getImage());
        setId(task.getId());
        setTaskDescriptionVisibility(View.GONE);
    }

}
