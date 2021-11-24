package com.example.habithelper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

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
 * Object for handling images within firebase storage
 * Extract images from storage, put images into storage, and show image to user
 * Each image processor is attached to one file
 * A user is required for creating the file name
 */
public class ImageProcessor {
    private ImageView destination;
    private String fileName;
    private FirebaseStorage storage;
    private User user;
    private String filePath;
    private Context originalContext;
    private volatile Boolean success;

    public ImageProcessor(Context originalContext, ImageView destination, User user){
        generateBasics();
        this.originalContext = originalContext;
        this.user = user;
        this.destination = destination;

        generateFileName();
    }

    public ImageProcessor(Context originalContext, User user){
        generateBasics();
        this.originalContext = originalContext;
        this.user = user;
        generateFileName();
    }

    private void generateBasics(){
        fileName = null;
        storage = FirebaseStorage.getInstance();
        filePath = null;
        destination = null;
        user = null;
        success = null;
    }

    public void setDestination(ImageView destination){
        if (destination != null){
            this.destination = destination;
        }
    }

    public File retrieveImage(String newFileName){
        locatePicture(newFileName);
        return null;
    }

    public Boolean saveImage(){

        this.savePicture();
        return success;

    }

    public String getFilePath() {
        return filePath;
    }


    public Boolean showImage(){
        try{
            destination.setImageBitmap(getImageAsBitmap());
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public Bitmap getImageAsBitmap(){
        // Get the dimensions of the View
        int targetW = destination.getWidth();
        int targetH = destination.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(filePath, bmOptions);

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.max(1, Math.min(photoW/(targetW), photoH/(targetH)));

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(filePath, bmOptions);
        return bitmap;
    }

    /**
     * Create a file to put the image into
     * @return the file path for the file created
     * @throws IOException
     */
    public File createImageFile() throws IOException {

        File storageDir = originalContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                fileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        filePath = image.getAbsolutePath();
        return image;
    }

    /**
     * Create a file name with attempts to avoid collision by naming the file according
     * to the current time
     */
    private void generateFileName(){
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        fileName = "JPEG_" + timeStamp + "_";
        return;
    }

    private String generateStorageFileName(String userID){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = userID + "-" + timeStamp + ".jpg";
        return fileName;
    }

    /**
     * Put the image into storage
     */
    //From firebase storage tutorial
    private void savePicture(){
        Bitmap image = getImageAsBitmap();
        String userID = this.user.getEmail().split("@")[0];
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();

        String fileName = generateStorageFileName(userID);
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
                ImageProcessor.this.success = false;
                notifyAll();
                Log.d("Waiting", "FailedSave");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                ImageProcessor.this.success = true;
                if (destination != null){
                    showImage();
                }

                Log.d("Waiting", "SuccessfulSave");
            }
        });
    }

    /**
     * Find the image in the firestore storage if it exists
     * @param newFileName
     */
    private void locatePicture(String newFileName){

        StorageReference storageRef = storage.getReference();
        StorageReference pictureImagesRef = storageRef.child(newFileName);
        File localFile = null;
        try {
            localFile = File.createTempFile("images", "jpg");
        }
        catch (Exception e){
            return;
        }
        File finalLocalFile = localFile;
        pictureImagesRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Local temp file has been created
                filePath = finalLocalFile.getAbsolutePath();
                fileName = newFileName;
                showImage();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                success = false;
                toastText("Image Retrieval Failed");

            }
        });
    }

    private void toastText(String message){
        Toast.makeText(this.originalContext, message,
                Toast.LENGTH_SHORT).show();
    }

}
