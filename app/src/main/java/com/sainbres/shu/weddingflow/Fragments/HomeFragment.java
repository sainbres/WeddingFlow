package com.sainbres.shu.weddingflow.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.raizlabs.android.dbflow.sql.language.CursorResult;
import com.raizlabs.android.dbflow.sql.language.IConditional;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.sainbres.shu.weddingflow.Models.User;
import com.sainbres.shu.weddingflow.Models.User_Table;
import com.sainbres.shu.weddingflow.Models.WeddingEvent;
import com.sainbres.shu.weddingflow.Models.WeddingEvent_Table;
import com.sainbres.shu.weddingflow.R;
import com.sainbres.shu.weddingflow.SetupWeddingEventActivity;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


public class HomeFragment extends Fragment {
    private View view;

    private SharedPreferences SharedPrefs;
    private SharedPreferences.Editor Editor;
    private TextView countdownLarge;
    private TextView countdownMedium;
    private TextView countdownLittle;
    private final int LARGEST_COUNTDOWN = 999;
    private static int RESULT_LOAD_IMG = 1;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        SharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Editor = SharedPrefs.edit();

        TextView participants = view.findViewById(R.id.participantsLine);
        TextView location = view.findViewById(R.id.locationLine);
        ImageView image = view.findViewById(R.id.weddingPhotoImageView);
        FloatingActionButton fab = view.findViewById(R.id.addPhotoFAB);

        int userId = SharedPrefs.getInt(getString(R.string.SP_UserId), -1);
        if (userId != -1){
            //not default
            /*
            User user = SQLite.select()
                    .from(User.class)
                    .where(User_Table.UserId.eq(userId))
                    .querySingle();
            */
            WeddingEvent event = SQLite.select()
                    .from(WeddingEvent.class)
                    .where(WeddingEvent_Table.UserId.eq(userId))
                    .querySingle();

            if (event != null){
                setUpCountdown(view, event.getWeddingDate());
                participants.setText(event.getParticipants());
                location.setText(event.getLocation());

                if (event.getImage() != null){
                    Bundle bundle = this.getArguments();
                    if (bundle != null){
                        if (bundle.getString("imagePath").equals(event.getImage()))
                        {
                            image.setImageURI(Uri.parse(bundle.getString("uri")));
                        } else {
                            image.setImageURI(Uri.fromFile(new File(event.getImage(), "fileName.jpg")));
                        }
                    }
                }
            }
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyStoragePermissions(getActivity());
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMG);
            }
        });




        return view;
    }

    public void setUpCountdown(View view, String dateString){
        countdownLarge = view.findViewById(R.id.countdownLarge);
        countdownMedium = view.findViewById(R.id.countdownMedium);
        countdownLittle = view.findViewById(R.id.countdownLittle);
        TextView daysTextView = view.findViewById(R.id.days);

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date date = null;
            date = sdf.parse(dateString);
            LocalDate weddingDate = new LocalDate(date);
            LocalDate currentDate = new LocalDate();
            int numberOfDaysUntil = Days.daysBetween(currentDate, weddingDate).getDays();

            if(numberOfDaysUntil > LARGEST_COUNTDOWN) {
                countdownLarge.setText("9");
                countdownMedium.setText("9");
                countdownLittle.setText("9");
            } else if (numberOfDaysUntil > 99) {
                String firstDigit = Integer.toString(numberOfDaysUntil).substring(0, 1);
                String secondDigit = Integer.toString(numberOfDaysUntil).substring(1, 2);
                String thirdDigit = Integer.toString(numberOfDaysUntil).substring(2, 3);

                countdownLarge.setText(firstDigit);
                countdownMedium.setText(secondDigit);
                countdownLittle.setText(thirdDigit);
            } else if (numberOfDaysUntil > 9) {
                String secondDigit = Integer.toString(numberOfDaysUntil).substring(0, 1);
                String thirdDigit = Integer.toString(numberOfDaysUntil).substring(1, 2);

                countdownMedium.setText(secondDigit);
                countdownLittle.setText(thirdDigit);
            } else if (numberOfDaysUntil > 1) {
                String thirdDigit = Integer.toString(numberOfDaysUntil).substring(0, 1);
                countdownLittle.setText(thirdDigit);
            } else if (numberOfDaysUntil == 1) {
                countdownLittle.setText("1");
                daysTextView.setText("day");
            }
            LocalDate waste = new LocalDate();
        } catch (ParseException e) {
            e.printStackTrace();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMG){
            int userId = SharedPrefs.getInt(getString(R.string.SP_UserId), -1);
            if (userId != -1) {

                ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());
                ImageView image = view.findViewById(R.id.weddingPhotoImageView);
                String uniquePathName = UUID.randomUUID().toString();
                File directory = cw.getDir(uniquePathName, Context.MODE_PRIVATE);
                File mypath = new File(directory, "fileName.jpg");
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);

                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                Bitmap bitmap = BitmapFactory.decodeFile(picturePath);

                image.setImageBitmap(bitmap);

                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(mypath);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //not default
                WeddingEvent event = SQLite.select()
                        .from(WeddingEvent.class)
                        .where(WeddingEvent_Table.UserId.eq(userId))
                        .querySingle();

                if (event != null){
                    event.setImage(directory.getAbsolutePath());
                    event.save();
                }
            }
        }
    }


}