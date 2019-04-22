package com.sainbres.shu.weddingflow;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.sainbres.shu.weddingflow.Models.User;
import com.sainbres.shu.weddingflow.Models.User_Table;
import com.sainbres.shu.weddingflow.Models.WeddingEvent;
import com.sainbres.shu.weddingflow.Models.WeddingEvent_Table;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences SharedPrefs;
    private SharedPreferences.Editor Editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Editor = SharedPrefs.edit();

        Toolbar toolbar = findViewById(R.id.settings_Toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Settings");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final TextInputLayout currentPasswordTil = findViewById(R.id.CurrentPasswordInputLayout);
        final TextInputLayout newPasswordTil = findViewById(R.id.NewPasswordInputLayout);
        final TextInputLayout confirmPasswordTil = findViewById(R.id.ConfirmPasswordInputLayout);

        final EditText currentPasswordInput = findViewById(R.id.input_old_password);
        final EditText newPasswordInput = findViewById(R.id.input_new_password);
        final EditText confirmPasswordInput = findViewById(R.id.input_confirm_password);

        Button changePasswordBtn = findViewById(R.id.changePasswordBtn);

        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int userId = SharedPrefs.getInt(getString(R.string.SP_UserId), -1);

                User user = SQLite.select().from(User.class)
                        .where(User_Table.UserId.eq(userId))
                        .querySingle();

                if (user != null){
                    String currentPassText = currentPasswordInput.getText().toString();
                    String newPassText = newPasswordInput.getText().toString();
                    String confirmPassText = confirmPasswordInput.getText().toString();
                    Boolean allFieldsRequired = false;

                    if (currentPassText.equals(""))
                    {
                        currentPasswordTil.setError("Old password is required");
                        currentPasswordInput.getBackground().setColorFilter(getResources().getColor(R.color.colorError), PorterDuff.Mode.SRC_ATOP);
                        allFieldsRequired = true;
                    } else {
                        currentPasswordTil.setError(null);
                        currentPasswordInput.getBackground().clearColorFilter();
                    }

                    if (newPassText.equals(""))
                    {
                        newPasswordTil.setError("Old password is required");
                        newPasswordInput.getBackground().setColorFilter(getResources().getColor(R.color.colorError), PorterDuff.Mode.SRC_ATOP);
                        allFieldsRequired = true;
                    } else {
                        newPasswordTil.setError(null);
                        newPasswordInput.getBackground().clearColorFilter();
                    }


                    if (confirmPassText.equals(""))
                    {
                        confirmPasswordTil.setError("Confirm password is required");
                        confirmPasswordInput.getBackground().setColorFilter(getResources().getColor(R.color.colorError), PorterDuff.Mode.SRC_ATOP);
                        allFieldsRequired = true;
                    } else {
                        confirmPasswordTil.setError(null);
                        confirmPasswordInput.getBackground().clearColorFilter();
                    }

                    if (allFieldsRequired){
                        //Do nothing
                    } else {
                        if (!currentPassText.equals(user.getPassword()))
                        {
                            currentPasswordTil.setError("Old password is invalid");
                            currentPasswordInput.getBackground().setColorFilter(getResources().getColor(R.color.colorError), PorterDuff.Mode.SRC_ATOP);
                            currentPasswordInput.setText("");
                        } else {
                            currentPasswordTil.setError(null);
                            currentPasswordInput.getBackground().clearColorFilter();
                            if (!newPassText.equals(confirmPassText)) {
                                newPasswordTil.setError("Both passwords must match");
                                newPasswordInput.getBackground().setColorFilter(getResources().getColor(R.color.colorError), PorterDuff.Mode.SRC_ATOP);
                                newPasswordInput.setText("");
                                confirmPasswordTil.setError("Both passwords must match");
                                confirmPasswordInput.getBackground().setColorFilter(getResources().getColor(R.color.colorError), PorterDuff.Mode.SRC_ATOP);
                                confirmPasswordInput.setText("");
                            }
                            else {
                                newPasswordTil.setError(null);
                                newPasswordInput.getBackground().clearColorFilter();
                                confirmPasswordTil.setError(null);
                                confirmPasswordInput.getBackground().clearColorFilter();

                                user.setPassword(newPasswordInput.getText().toString());
                                user.save();

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }
                        }
                    }
                }
            }
        });

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
