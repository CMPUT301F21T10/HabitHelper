package com.example.habithelper;


import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomCamera{

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Activity source;

    public void CustomCamera(Activity source){
        this.source = source;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            source.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
            Log.d("MyCamera", "ACTIVITY NOT FOUND");
        }
        Log.d("MyCamera", "Dispatched");
    }



    public void startPicture(){
        dispatchTakePictureIntent();
    }

    public void processImage(Intent takePictureIntent, int FinalImage){

    }



}
