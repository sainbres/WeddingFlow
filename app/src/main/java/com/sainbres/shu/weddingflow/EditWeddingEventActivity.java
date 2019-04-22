package com.sainbres.shu.weddingflow;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.sainbres.shu.weddingflow.Models.WeddingEvent;
import com.sainbres.shu.weddingflow.Models.WeddingEvent_Table;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class EditWeddingEventActivity extends AppCompatActivity {

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private static int RESULT_LOAD_IMG = 1;
    private EditText imageText;
    private byte[] imageByteData;
    private String imagePath;
    public final int REQUEST_CODE_FOR_PERMISSIONS = 654;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private SharedPreferences SharedPrefs;
    private SharedPreferences.Editor Editor;
    private WeddingEvent event;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_wedding_event);

        SharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Editor = SharedPrefs.edit();

        int eventId = SharedPrefs.getInt(getString(R.string.SP_EventId), -1);

        event = SQLite.select().from(WeddingEvent.class).where(WeddingEvent_Table.EventId.eq(eventId)).querySingle();

        final TextInputLayout parTil = findViewById(R.id.ParticipantsInputLayout);
        final TextInputLayout locTil = findViewById(R.id.LocationInputLayout);
        final TextInputLayout dateTil = findViewById(R.id.DateInputLayout);

        final EditText parEditText = findViewById(R.id.input_participants);
        final EditText locEditText = findViewById(R.id.input_location);
        final EditText dateEditText = findViewById(R.id.input_date);

        imageText = findViewById(R.id.input_image);

        parEditText.setText(event.getParticipants());
        locEditText.setText(event.getLocation());
        dateEditText.setText(event.getWeddingDate());

        if(event.getImage() != null){
            imagePath = event.getImage();
            imageText.setText(imagePath.substring(imagePath.lastIndexOf("/") + 1).trim());
        }


        final ImageButton imageBtn = findViewById(R.id.imageBtn);

        final Button completeEditBtn = findViewById(R.id.editWeddingBtn);

        Toolbar toolbar = findViewById(R.id.editWedding_Toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Edit Wedding Details");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(EditWeddingEventActivity.this,
                        R.style.DatePickerLight,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                dialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorWhite));
                dialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorWhite));

            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                dateEditText.setText(dayOfMonth+"-"+month+"-"+year);
            }
        };

        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyStoragePermissions(EditWeddingEventActivity.this);
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMG);
            }

        });


        completeEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String participantsText = parEditText.getText().toString();
                String locText = locEditText.getText().toString();
                String dateText = dateEditText.getText().toString();

                Boolean allFieldsRequired = false;

                //Check If Participants has been entered
                if (participantsText.equals("")){
                    parTil.setError("Participants are required");
                    parEditText.getBackground().setColorFilter(getResources().getColor(R.color.colorError), PorterDuff.Mode.SRC_ATOP);
                    allFieldsRequired = true;
                } else {
                    parTil.setError(null);
                    parEditText.getBackground().clearColorFilter();
                }

                //Check If Participants has been entered
                if (locText.equals("")){
                    locTil.setError("A location is required");
                    locEditText.getBackground().setColorFilter(getResources().getColor(R.color.colorError), PorterDuff.Mode.SRC_ATOP);
                    allFieldsRequired = true;
                } else {
                    locTil.setError(null);
                    locEditText.getBackground().clearColorFilter();
                }

                //Check If Participants has been entered
                if (dateText.equals("")){
                    dateTil.setError("A date is required");
                    dateEditText.getBackground().setColorFilter(getResources().getColor(R.color.colorError), PorterDuff.Mode.SRC_ATOP);
                    allFieldsRequired = true;
                } else {
                    dateTil.setError(null);
                    dateEditText.getBackground().clearColorFilter();
                }

                if (allFieldsRequired){
                    // Do nothing if a field is required
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    try {
                        Date date = sdf.parse(dateText);
                        Date currentDate = new Date();
                        if(date.before(currentDate) || date.equals(currentDate)) {
                            dateTil.setError("Date needs to be in the future");
                            dateEditText.getBackground().setColorFilter(getResources().getColor(R.color.colorError), PorterDuff.Mode.SRC_ATOP);
                        } else {
                            // Reset date field formatting
                            dateTil.setError(null);
                            dateEditText.getBackground().clearColorFilter();
                            int userId = SharedPrefs.getInt(getString(R.string.SP_UserId), -1);
                            if (userId != -1){

                                event.setUserId(userId);
                                event.setLocation(locText);
                                event.setParticipants(participantsText);
                                event.setWeddingDate(dateText);

                                if (!imageText.getText().toString().equals("")){
                                    event.setImage(imagePath);
                                } else {
                                    event.setImage(null);
                                }
                                event.save();

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }

                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMG){
            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            String uniquePathName = UUID.randomUUID().toString();
            File directory = cw.getDir(uniquePathName, Context.MODE_PRIVATE);
            File mypath = new File(directory, "fileName.jpg");
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);

            assert cursor != null;
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            imageText.setText(picturePath.substring(picturePath.lastIndexOf("/") + 1).trim());

            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(mypath);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            imagePath = directory.getAbsolutePath();

        }
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}

