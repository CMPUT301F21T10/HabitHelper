/*
Copyright 2021 CMPUT301F21T10

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
/*
CreateHabitEventActivity hold all the pertinent data for the habit event being created.
 */
package com.example.habithelper;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * This class is responsible to create new habit events
 */
public class CreateHabitEventActivity extends AppCompatActivity implements Serializable {

    TextView textViewHabitName;
    Button button_location_picker;
    TextView locationText;
    TextView editTextDateCompleted;
    EditText editTextComments;
    TextView selectedLocationText;

    ImageView eventImage;
    private FirebaseStorage storage;
    private String currentPhotoPath;
    private String currentPhotoFileName;
    static final int REQUEST_IMAGE_CAPTURE = 1;


    FirebaseFirestore db;
    FirebaseUser user;
    Habit habit_to_create_event;


    String address = "";
    Double Lat = 0.0, Long = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_habit_event);
        //Setting up toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Create Habit Event");

        db = FirebaseFirestore.getInstance();

        button_location_picker = findViewById(R.id.buttonAddLocation);
        locationText = findViewById(R.id.textView_addedLocation);
        selectedLocationText = findViewById(R.id.textView_selectedLocation);
        textViewHabitName = findViewById(R.id.textViewHabitName);
        editTextDateCompleted = findViewById(R.id.editTextDateCompleted);
        editTextComments = findViewById(R.id.editTextOptionalComments);

        eventImage = findViewById(R.id.imageView);
        currentPhotoFileName = "";
        currentPhotoPath = "";


        Bundle extras = getIntent().getExtras();
        if (extras != null){
            Log.d("CLASS_FROM", "onCreate: " + extras.getString("classFrom"));

            if (extras.getString("classFrom").equals(LocationPickerActivity.class.toString())){

                address = extras.getString("address");
                Log.d("ADDRESS", "onCreate: " + address);
                String date = extras.getString("date");
                String comment = extras.getString("comment");
                Lat = extras.getDouble("lat");
                Long = extras.getDouble("long");

                //Get the habit for which a habit event is to be created
                habit_to_create_event = (Habit) extras.getSerializable("habit");
                textViewHabitName.setText("Habit Name: "+habit_to_create_event.getTitle());
                user = (FirebaseUser) extras.get("currentUser");

                editTextDateCompleted.setText(date);
                editTextComments.setText(comment);
                locationText.setText(address);
                locationText.setVisibility(View.VISIBLE);
                selectedLocationText.setVisibility(View.VISIBLE);

                //LOAD IMAGE HERE
            }
            else{ // from main activity (habit fragment)

                //Get the habit for which a habit event is to be created
                habit_to_create_event = (Habit) extras.getSerializable("habit");
                textViewHabitName.setText("Habit Name: "+habit_to_create_event.getTitle());
                user = (FirebaseUser) extras.get("currentUser");
            }



        }

        button_location_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapIntent = new Intent(CreateHabitEventActivity.this, LocationPickerActivity.class);
                mapIntent.putExtra("habit", habit_to_create_event);
                mapIntent.putExtra("currentUser", user);

                // store data already input to display again when back to this activity
                String date = editTextDateCompleted.getText().toString();
                String comment = editTextComments.getText().toString();

                mapIntent.putExtra("date", date);
                mapIntent.putExtra("comment", comment);
                mapIntent.putExtra("lat", Lat);
                mapIntent.putExtra("long", Long);
                mapIntent.putExtra("address", address);

                startActivity(mapIntent);
            }
        });


        //Setting up date picker dialogue
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog.OnDateSetListener setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = year + "-" + month + "-" + dayOfMonth;
                editTextDateCompleted.setText(date);
            }
        };
        DatePickerDialog.OnDateSetListener finalSetListener = setListener;
        editTextDateCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateHabitEventActivity.this,
                        finalSetListener, year, month, day);
                datePickerDialog.show();
            }
        });

        storage = FirebaseStorage.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_habit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Getting the habit event details edit texts of the habit event being created
        editTextDateCompleted = findViewById(R.id.editTextDateCompleted);
        editTextComments = findViewById(R.id.editTextOptionalComments);

        switch (item.getItemId()){
            //When create is selected in the menu
            case R.id.createHabit:

                //Get the current user from MainActivity to pass it back to MainActivity
                Bundle extras = getIntent().getExtras();
                user = (FirebaseUser) extras.get("currentUser");

                //Get the habit details of the habit being created
                String associatedHabitTitle = habit_to_create_event.getTitle();
                String EventTitle = associatedHabitTitle + " Event";
                String EventComment = editTextComments.getText().toString();
                String EventDateCompleted = editTextDateCompleted.getText().toString();
                String EventLocation = locationText.getText().toString();

                //Create the new habit event object
                HabitEvent newHabitEvent = new HabitEvent(EventTitle, EventComment, EventDateCompleted, associatedHabitTitle,
                        EventLocation, Lat, Long);

                //adding the new habit event to the database
                newHabitEvent.setEventPhoto(currentPhotoFileName);
                newHabitEvent.addHabitEventToDB(user.getEmail(), db);

                Intent intent = new Intent(CreateHabitEventActivity.this, MainActivity.class);
                intent.putExtra("classFrom", CreateHabitEventActivity.class.toString());
                intent.putExtra("habitEventCreated", newHabitEvent);
                intent.putExtra("currentUser", user);
                startActivity(intent);
                return true;

            //When cancel is selected in the menu
            case R.id.goBack:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //START OF CODE FOR GETTING HABIT EVENT IMAGE

    /**
     * Initiate the camera
     * @param view
     */
    public void takePicture(View view){
        dispatchTakePictureIntent();
    }

    /**
     * Start up the camera
     * https://developer.android.com/training/camera/photobasics
     */
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
                Toast.makeText(CreateHabitEventActivity.this, "File creation failed.",
                        Toast.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Log.d("MyCamera", "photo file created");
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    /**
     * Create a place to put the image the camera will take
     * https://developer.android.com/training/camera/photobasics
     * @return the location we can put the new image into
     * @throws IOException
     */
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

    /**
     * Create a collision unlikely file name for temporary storage
     * @return the file name
     */
    private String getImageFileDestination(){
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return "JPEG_" + timeStamp + "_";
    }

    /**
     * After finishing with taking a picture set the pictre
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setPicture();
        }

    /**
     * Put the picture into a place the user can see then move to save it to the
     * firestore storage
     */
    private void setPicture() {

        Bitmap bitmap = showImage(eventImage, currentPhotoPath);
        savePicture(bitmap);
        //Log.d(encodeFileToBase64Binary(currentPhotoPath));

    }

    /**
     * Put the image into firestore storage
     * https://developer.android.com/training/camera/photobasics
     * @param image the image in bitmap form
     *
     */
    //From firebase storage tutorial
    private void savePicture(Bitmap image){
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = user.getEmail().split("@")[0] + "-" + timeStamp + ".jpg";
        currentPhotoFileName = fileName;
        Log.d("SAVEPICTURE", currentPhotoFileName);
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
                Toast.makeText(CreateHabitEventActivity.this, "File save failed.",
                        Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //load the image into the image view
                showImage(eventImage, currentPhotoPath);
                }
        });
    }

    /**
     * Scale the image and show it to the user
     * Taken from the android tutorial
     * https://developer.android.com/training/camera/photobasics
     * @param destination
     * @param filePhotoPath
     * @return
     */
    private Bitmap showImage(ImageView destination, String filePhotoPath){

        // Get the dimensions of the View
        int targetW = destination.getWidth();
        int targetH = destination.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(filePhotoPath, bmOptions);

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.max(1, Math.min(photoW/(targetW), photoH/(targetH)));

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(filePhotoPath, bmOptions);
        destination.setImageBitmap(bitmap);
        return bitmap;
    }
    //END OF CODE FOR RETRIEVING HABIT EVEN IMAGE
}