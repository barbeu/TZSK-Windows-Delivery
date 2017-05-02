package com.example.tzadmin.tzsk_windows;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.tzadmin.tzsk_windows.AuthModule.Auth;
import com.example.tzadmin.tzsk_windows.DatabaseModule.Database;
import com.example.tzadmin.tzsk_windows.DatabaseModule.DatabaseHelper;
import com.example.tzadmin.tzsk_windows.DatabaseModule.DatabaseModels.Delivery;
import com.example.tzadmin.tzsk_windows.DatabaseModule.DatabaseModels.Photo;
import java.io.File;

public class CameraActivity extends AppCompatActivity {

    final int CODE_PHOTO = 1;
    File directory;
    Delivery delivery;
    String pathPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createDirectory();
        Intent intentGetId = getIntent();
        int id = intentGetId.getIntExtra("id", -1);
        delivery = Database.selectDelivery(id, Auth.id);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Database.SetUp(dbHelper.getReadableDatabase());

        intent.putExtra(MediaStore.EXTRA_OUTPUT, generateFileUri());
        startActivityForResult(intent, CODE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == CODE_PHOTO) {
            if (resultCode == RESULT_OK) {
                if (intent == null) {
                    //Add photo to database
                    finish();
                }
            } else finish();
        }
    }

    private Uri generateFileUri() {
        File file = null;
        pathPhoto = directory.getPath() + "/" + delivery.DocID +
                "_" + System.currentTimeMillis() + ".jpg";
        file = new File(pathPhoto);
        return Uri.fromFile(file);
    }

    private void createDirectory() {
        directory = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "Delivery");
        if (!directory.exists())
            directory.mkdirs();
    }
}