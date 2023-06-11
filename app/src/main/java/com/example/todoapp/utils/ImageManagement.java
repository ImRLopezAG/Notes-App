package com.example.todoapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.activity.result.ActivityResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageManagement {
    public static  byte[] GetImageBytes(Context context, Uri image) {
        if (image != null) {
            try {
                InputStream inputStream = context.getContentResolver().openInputStream(image);
                if (inputStream != null) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        byteArrayOutputStream.write(buffer, 0, bytesRead);
                    }
                    return byteArrayOutputStream.toByteArray();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public static Uri GetImageUriFromTask(Context context, Task task) {
        byte[] imageBytes = task.getImage();
        if (imageBytes != null && imageBytes.length > 0) {
            File tempImageFile;
            try {
                tempImageFile = File.createTempFile("temp_image", ".jpg", context.getCacheDir());
                FileOutputStream fileOutputStream = new FileOutputStream(tempImageFile);
                fileOutputStream.write(imageBytes);
                fileOutputStream.close();
                return Uri.fromFile(tempImageFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public static  Bitmap SetImageView(ActivityResult result, Context context){
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            if (data != null) {
                Uri image = data.getData();
                if (image != null) {
                    try {
                        return MediaStore.Images.Media.getBitmap(context.getContentResolver(), image);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }
}
