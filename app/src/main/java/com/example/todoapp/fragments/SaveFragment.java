package com.example.todoapp.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.todoapp.R;
import com.example.todoapp.SQDataBase;
import com.example.todoapp.utils.ImageManagement;
import com.example.todoapp.utils.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.io.File;

public class SaveFragment extends Fragment {
    private EditText et_title, et_description;
    private ImageView iv_image;
    private Uri imageUri, uri;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    FloatingActionButton fabAdd;
    SQDataBase db;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_save, container, false);
        db = new SQDataBase(getActivity(), "tasks", null, 1);
        fabAdd = requireActivity().findViewById(R.id.fab_add);

        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            Bitmap image = ImageManagement.SetImageView(result, getContext());
            iv_image.setImageBitmap(image);
            imageUri = result.getData().getData();
        });

        Button btn_save = root.findViewById(R.id.btn_save);
        TextView tv_title = root.findViewById(R.id.tv_title);
        LinearLayout btn_image = root.findViewById(R.id.select_image);

        fabAdd.hide();

        et_title = root.findViewById(R.id.et_title);
        et_description = root.findViewById(R.id.et_description);
        iv_image = root.findViewById(R.id.iv_image);

        Bundle bundle = getArguments();
        tv_title.setText(bundle != null ? "Update Task" : "Add Task");
        btn_save.setText(bundle != null ? "Update" : "Save");

        if (bundle != null) {
            int taskId = bundle.getInt("id");
            Task task = db.GetTask(taskId);

            Bitmap bitmap = BitmapFactory.decodeByteArray(task.getImage(), 0, task.getImage().length);
            uri = ImageManagement.GetImageUriFromTask(getActivity(),task);
            et_title.setText(task.getTitle());
            et_description.setText(task.getDescription());
            iv_image.setImageBitmap(bitmap);
            imageUri = uri;
        }

        btn_save.setOnClickListener(view -> {
            boolean task = SaveTask(bundle != null ? bundle.getInt("id") : null);
            if (task){
                Toast.makeText(getContext(), "Task saved", Toast.LENGTH_SHORT).show();
                requireActivity().getSupportFragmentManager()
                        .beginTransaction().
                        replace(R.id.content_main, new TasksFragment())
                        .addToBackStack(null)
                        .commit();            }
        });

        btn_image.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fabAdd.show();
        if (uri != null) {
            File file = new File(uri.getPath());
            file.delete();
        }
    }
    private boolean SaveTask(@Nullable Integer id){
        Task task = new Task();
        task.setTitle(et_title.getText().toString());
        task.setDescription(et_description.getText().toString());
        task.setImage(ImageManagement.GetImageBytes(getActivity(), imageUri));

        if(et_title.getText().toString().isEmpty() || et_description.getText().toString().isEmpty() || ImageManagement.GetImageBytes(getActivity(), imageUri) == null){
            Toast.makeText(getContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return false;
        } else if (id != null) {
            task.setId(id);
            db.UpdateTask(task);
        } else {
            db.CreateTask(task);
        }
        return true;
    }
}