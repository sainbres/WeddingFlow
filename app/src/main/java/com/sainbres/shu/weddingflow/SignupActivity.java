package com.sainbres.shu.weddingflow;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.sainbres.shu.weddingflow.Models.User;
import com.sainbres.shu.weddingflow.Models.User_Table;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SignupActivity extends AppCompatActivity {

    private SharedPreferences SharedPrefs;
    private SharedPreferences.Editor Editor;

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private EditText dobTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        final TextInputLayout firstNameTil = findViewById(R.id.FirstNameInputLayout);
        final TextInputLayout surnameTil = findViewById(R.id.SurnameInputLayout);
        final TextInputLayout emailTil = findViewById(R.id.EmailInputLayout);
        final TextInputLayout passwordTil = findViewById(R.id.PasswordInputLayout);
        final TextInputLayout dobTil = findViewById(R.id.DOBInputLayout);

        final EditText firstName = findViewById(R.id.input_first_name);
        final EditText surname = findViewById(R.id.input_surname);
        final EditText username = findViewById(R.id.input_email);
        final EditText password = findViewById(R.id.input_password);
        //DateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.UK);
        dobTextView = findViewById(R.id.input_dob);

        final TextView errorOutput = findViewById(R.id.errorTextView);

        final Button signUpBtn = findViewById(R.id.signUpBtn);

        SharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Editor = SharedPrefs.edit();

        Toolbar toolbar = findViewById(R.id.signup_Toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Sign up");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dobTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(SignupActivity.this,
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
                      dobTextView.setText(dayOfMonth+"-"+month+"-"+year);
            }
        };

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstNameText = firstName.getText().toString();
                String surnameText = surname.getText().toString();
                String emailText = username.getText().toString();
                String passwordText = password.getText().toString();
                String dobText = dobTextView.getText().toString();

                Boolean allFieldsRequired = false;

                //Check If FirstName has been entered
                if (firstNameText.equals("")){
                    firstNameTil.setError("First name is required");
                    firstName.getBackground().setColorFilter(getResources().getColor(R.color.colorError), PorterDuff.Mode.SRC_ATOP);
                    allFieldsRequired = true;
                } else {
                    firstNameTil.setError(null);
                    firstName.getBackground().clearColorFilter();
                }

                //Check If Surname has been entered
                if (surnameText.equals("")){
                    surnameTil.setError("Surname is required");
                    surname.getBackground().setColorFilter(getResources().getColor(R.color.colorError), PorterDuff.Mode.SRC_ATOP);
                    allFieldsRequired = true;
                } else {
                    surnameTil.setError(null);
                    surname.getBackground().clearColorFilter();
                }

                //Check If Email has been entered
                if (emailText.equals("")){
                    emailTil.setError("Surname is required");
                    username.getBackground().setColorFilter(getResources().getColor(R.color.colorError), PorterDuff.Mode.SRC_ATOP);
                    allFieldsRequired = true;
                } else {
                    emailTil.setError(null);
                    username.getBackground().clearColorFilter();
                }

                //Check If password has been entered
                if (passwordText.equals("")){
                    passwordTil.setError("Surname is required");
                    password.getBackground().setColorFilter(getResources().getColor(R.color.colorError), PorterDuff.Mode.SRC_ATOP);
                    allFieldsRequired = true;
                } else {
                    passwordTil.setError(null);
                    password.getBackground().clearColorFilter();
                }

                //Check If dob has been entered
                if (dobText.equals("")){
                    dobTil.setError("Surname is required");
                    dobTextView.getBackground().setColorFilter(getResources().getColor(R.color.colorError), PorterDuff.Mode.SRC_ATOP);
                    allFieldsRequired = true;
                } else {
                    dobTil.setError(null);
                    dobTextView.getBackground().clearColorFilter();
                }


                if (allFieldsRequired){
                // Do nothing if a field is required
                } else {
                    User user = SQLite.select().from(User.class)
                            .where(User_Table.UserName.eq(username.getText().toString()))
                            .querySingle();
                    if (user != null) {
                        emailTil.setError("Name already in use");
                        username.getBackground().setColorFilter(getResources().getColor(R.color.colorError), PorterDuff.Mode.SRC_ATOP);
                    }
                    else {
                        emailTil.setError(null);
                        username.getBackground().clearColorFilter();
                        /*
                        DateFormat format = new SimpleDateFormat("d/M/YYYY", Locale.UK);
                        try {
                            Date dobDate = format.parse(dobText);
                            Date dateNow = new Date();
                            if (dobDate.after(dateNow.) < (dateNow.))

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        */
                        User newUser = new User();
                        newUser.setFirstName(firstNameText);
                        newUser.setSurname(surnameText);
                        newUser.setUserName(emailText);
                        newUser.setPassword(passwordText);
                        newUser.save();

                        Editor.putInt(getString(R.string.SP_UserId), newUser.getUserId());
                        Editor.commit();

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


}
