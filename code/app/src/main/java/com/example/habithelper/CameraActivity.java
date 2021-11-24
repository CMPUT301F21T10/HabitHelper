package com.example.habithelper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Tutorial followed to build camera:
 * https://sodocumentation.net/android/topic/619/camera-2-api
 */
public class CameraActivity extends AppCompatActivity {
    private Button takePhotoButton;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private String currentPhotoPath;
    private ImageView cameraImageView;

    private FirebaseStorage storage;
    private ImageProcessor processor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        takePhotoButton = findViewById(R.id.camera_button);
        cameraImageView = findViewById(R.id.cameraImageView);
        processor = new ImageProcessor(CameraActivity.this,
                cameraImageView, new User("test", "test@test.ca", "password"));

        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTakePhotoButtonClick(view);
            }
        });
        findViewById(R.id.photo_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processor.retrieveImage("test-20211124_151857.jpg");
            }
        });


    }


    public void onTakePhotoButtonClick(View view){
        dispatchTakePictureIntent();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = processor.createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(CameraActivity.this, "File creation failed.",
                        Toast.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Log.d("MyCamera", "photo file created");
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                Log.d("MyCamera", MediaStore.EXTRA_OUTPUT);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras != null){
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                if (imageBitmap != null){
                    cameraImageView.setImageBitmap(imageBitmap);
                    Log.d("MyCamera", "bitmap not null");
                }else{
                    Log.d("MyCamera", "bitmap null");
                }
            }
            else{
                Log.d("MyCamera", "extras null");
                processor.saveImage();

            }


        }

    }

}
