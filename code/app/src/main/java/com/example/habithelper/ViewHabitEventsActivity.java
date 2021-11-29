package com.example.habithelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ViewHabitEventsActivity extends AppCompatActivity {


    private TextView viewDateCompleted, viewHabitName;
    private EditText viewOptionalComments;
    private TextView viewOptionalLocation;
    FirebaseFirestore db;
    FirebaseUser user;
    Bundle extras;
    private HabitEvent habitEventEditing;
    private Button viewLocationButton;

    private String address, date, comment;
    private Double Lat, Long;

    ImageView eventImage;
    private FirebaseStorage storage;
    private String currentPhotoPath;
    private String currentPhotoFileName;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_habit_events);
//        setContentView(R.layout.activity_view_habit_events);
        //Setting up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Habit Events");

        db = FirebaseFirestore.getInstance();

        viewHabitName = findViewById(R.id.viewHabitName);
        viewDateCompleted = findViewById(R.id.viewDateCompleted);
        viewOptionalComments = findViewById(R.id.viewOptionalComments);
        viewOptionalLocation = findViewById(R.id.viewOptionalLocation);
        viewLocationButton = findViewById(R.id.viewLocation_button);

        eventImage = findViewById(R.id.viewImage);
        currentPhotoFileName = "";
        currentPhotoPath = "";
        storage = FirebaseStorage.getInstance();


        extras = getIntent().getExtras();
        //Getting the habit object being viewed, and the current user
        if (extras != null){
            if (extras.getString("classFrom").equals(showLocationActivity.class.toString())){
                address = extras.getString("address");
                Lat = extras.getDouble("lat");
                Long = extras.getDouble("long");
                date = extras.getString("date");
                comment = extras.getString("comment");
                currentPhotoFileName = extras.getString("photo_path");

                user = (FirebaseUser) extras.get("currentUser");
                habitEventEditing = (HabitEvent) extras.getSerializable("habitEvent");

                viewHabitName.setText(habitEventEditing.getEventTitle());
                viewDateCompleted.setText(date);
                viewOptionalComments.setText(comment);
                viewOptionalLocation.setText(address);

                habitEventEditing.setEventDateCompleted(date);
                habitEventEditing.setEventComment(comment);
                habitEventEditing.setEventLocation(address);
                habitEventEditing.setLat(Lat);
                habitEventEditing.setLong(Long);

                //LOAD IMAGE HERE
                if (currentPhotoFileName!=null && !currentPhotoFileName.equals("")) {
                    Log.d("PHOTO", "onCreate: display photo");
//                    showImage(eventImage, currentPhotoPath);
                    locatePicture(currentPhotoFileName,eventImage);
                }

            }
            else { // from main activity (Events fragment)
                habitEventEditing = (HabitEvent) extras.getSerializable("habit");
                user = (FirebaseUser) extras.get("currentUser");

                viewHabitName.setText(habitEventEditing.getEventTitle());
                viewDateCompleted.setText(habitEventEditing.getEventDateCompleted());
                viewOptionalComments.setText(habitEventEditing.getEventComment());
                viewOptionalLocation.setText(habitEventEditing.getEventLocation());
            }

        }


        if (habitEventEditing.getEventPhoto().length() > 0){
            Log.d("HABITEVENTPHOTO", habitEventEditing.getEventPhoto());
            currentPhotoFileName = habitEventEditing.getEventPhoto();
            locatePicture(habitEventEditing.getEventPhoto(), eventImage);
        }

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
                viewDateCompleted.setText(date);
            }
        };
        DatePickerDialog.OnDateSetListener finalSetListener = setListener;
        viewDateCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ViewHabitEventsActivity.this,
                        finalSetListener, year, month, day);
                datePickerDialog.show();
            }
        });

        viewLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go to showLocationActivity
                Lat = habitEventEditing.getLat();
                Long = habitEventEditing.getLong();

                Intent intent = new Intent(ViewHabitEventsActivity.this, showLocationActivity.class);
                intent.putExtra("lat", Lat);
                intent.putExtra("long", Long);
                intent.putExtra("address", viewOptionalLocation.getText().toString());
                intent.putExtra("date", viewDateCompleted.getText().toString());
                intent.putExtra("comment", viewOptionalComments.getText().toString());

                intent.putExtra("currentUser", user);
                intent.putExtra("habitEvent", habitEventEditing);
                intent.putExtra("photo_path", currentPhotoFileName);


                startActivity(intent);

            }
        });
        FloatingActionButton takePictureButton = findViewById(R.id.fab_viewHabitEventPicture);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture(view);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_habit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save:


                String habitEventsTitle = viewHabitName.getText().toString();
                String habitEventDate = viewDateCompleted.getText().toString();;
                String habitEventComment = viewOptionalComments.getText().toString();
                String habitEventLocation = viewOptionalLocation.getText().toString();

                try {


                    HabitEvent newEditedHabitEvent;
                    if (habitEventLocation.equals(habitEventEditing.getEventLocation())) {
                        newEditedHabitEvent = new HabitEvent(habitEventsTitle, habitEventComment, habitEventDate,
                                habitEventEditing.getAssociatedHabitTitle(), habitEventLocation,
                                habitEventEditing.getLat(), habitEventEditing.getLong());


                    } else {
                        newEditedHabitEvent = new HabitEvent(habitEventsTitle, habitEventComment, habitEventDate,
                                habitEventEditing.getAssociatedHabitTitle(), habitEventLocation,
                                Lat, Long);
                    }

                    newEditedHabitEvent.setEventPhoto(currentPhotoFileName);
                    String emailToEdit = user.getEmail();

                    //deleting the old habit from the database
                    habitEventEditing.deleteHabitEventFromDB(emailToEdit, db);

                    //adding the new edited habit to the database
                    newEditedHabitEvent.addHabitEventToDB(emailToEdit, db);

                    Intent intent = new Intent(ViewHabitEventsActivity.this, MainActivity.class);
                    intent.putExtra("classFrom", ViewHabitsActivity.class.toString());
                    intent.putExtra("currentUser", user);
                    startActivity(intent);
                    eventImage.setImageDrawable(null);
                }catch(Exception e){
                    Toast.makeText(getApplicationContext(),
                            "Something went wrong!",
                            Toast.LENGTH_SHORT).show();
                }
                return true;

            case R.id.delete:
                String email = user.getEmail();
                //deleting the habit from the database
                try{
                    habitEventEditing.deleteHabitEventFromDB(email, db);
                    Intent intent = new Intent(ViewHabitEventsActivity.this, MainActivity.class);
                    intent.putExtra("classFrom", ViewHabitEventsActivity.class.toString());
                    intent.putExtra("currentUser", user);
                    startActivity(intent);
                    eventImage.setImageDrawable(null);
                    //Retrieving current number of habit events from db
                    DocumentReference docRef = db.collection("Habits")
                            .document(user.getEmail())
                            .collection(user.getEmail()+"_habits").document(habitEventEditing.getAssociatedHabitTitle());

                    docRef.get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    //Incrementing number of habit events and saving it back to db
                                    String numHabitEvents = (String) documentSnapshot.get("numHabitEvents");
                                    int numberOfHabitEvents = Integer.parseInt(numHabitEvents);
                                    numberOfHabitEvents--;
                                    numHabitEvents = String.valueOf(numberOfHabitEvents);

                                    Map<String, String> data = new HashMap<>();
                                    data.put("numHabitEvents", numHabitEvents);
                                    docRef.set(data, SetOptions.merge());
                                }
                            });

            }catch(Exception e){
                Toast.makeText(getApplicationContext(),
                    "Something went wrong!",
                    Toast.LENGTH_SHORT).show();
        }
                return true;

            case R.id.goBack:
                eventImage.setImageDrawable(null);
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
        try{
            dispatchTakePictureIntent();
        }catch (Exception e){
            Toast.makeText(ViewHabitEventsActivity.this,
                    "Camera unavailable at this time. Please start again.",
                    Toast.LENGTH_SHORT).show();
        }
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
                Toast.makeText(ViewHabitEventsActivity.this, "File creation failed.",
                        Toast.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Log.d("MyCamera", "photo file created");
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                try{
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }catch(Exception e){
                    Log.d("MyCamera", "FAILURE");
                }
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
        if (bitmap != null){
            savePicture(bitmap);
        }

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
                Toast.makeText(ViewHabitEventsActivity.this, "File save failed.",
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

    /**
     * Find the relevant image in firestore storage, if it exists
     * @param fileName
     * @param destination
     */
    private void locatePicture(String fileName, ImageView destination){
        StorageReference storageRef = storage.getReference();
        StorageReference pictureImagesRef = storageRef.child(fileName);
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
                currentPhotoPath = finalLocalFile.getAbsolutePath();
                showImage(destination, finalLocalFile.getAbsolutePath());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    }
    //END OF CODE FOR RETRIEVING HABIT EVEN IMAGE
}