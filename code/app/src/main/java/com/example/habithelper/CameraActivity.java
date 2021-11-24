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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        storage = FirebaseStorage.getInstance();

        takePhotoButton = findViewById(R.id.camera_button);
        cameraImageView = findViewById(R.id.cameraImageView);
        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTakePhotoButtonClick(view);
            }
        });

        cameraImageView.setImageBitmap(locatePicture("fake-time.jpg"));
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
                photoFile = createImageFile();
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

    private File createImageFile() throws IOException {

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                getImageFileDestination(),  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private String getImageFileDestination(){
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return "JPEG_" + timeStamp + "_";
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
                setPic();

            }


        }

    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = cameraImageView.getWidth();
        int targetH = cameraImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.max(1, Math.min(photoW/targetW, photoH/targetH));

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        cameraImageView.setImageBitmap(bitmap);
        savePicture("fake", bitmap);
        //Log.d(encodeFileToBase64Binary(currentPhotoPath));

    }

    //From firebase storage tutorial
    private void savePicture(String userID, Bitmap image){
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();

        String timeStamp = "time";
        String fileName = userID + "-" + timeStamp + ".jpg";
        // Create a reference to the image based upon the userID
        StorageReference pictureRef = storageRef.child(fileName);

        // Create a reference to 'images/mountains.jpg'
        StorageReference pictureImagesRef = storageRef.child("images/" + fileName);

        // While the file names are the same, the references point to different files
        pictureRef.getName().equals(pictureImagesRef.getName());    // true
        pictureRef.getPath().equals(pictureImagesRef.getPath());    // false

        //Put the bitmap into a form usable by firestore
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        //Put the file into firestore storage
        UploadTask uploadTask = pictureRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(CameraActivity.this, "File save failed.",
                        Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
            }
        });
    }

    private Bitmap locatePicture(String fileName){
        StorageReference storageRef = storage.getReference();
        StorageReference pictureRef = storageRef.child(fileName);
        final Bitmap[] result = new Bitmap[1];
        final long ONE_MEGABYTE = 1024 * 1024;
        pictureRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                result[0] = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                Log.d("MyCamera", "ImageRetrival Success");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.d("MyCamera", "ImageRetrival Failed");
                result[0] = null;
            }
        });
        return result[0];
    }

    private void showImage(ImageView destination, File file){

    }
}
