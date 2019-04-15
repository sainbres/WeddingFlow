package com.sainbres.shu.weddingflow;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.sainbres.shu.weddingflow.Models.User;
import com.sainbres.shu.weddingflow.Models.User_Table;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences SharedPrefs;
    private SharedPreferences.Editor Editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText username = findViewById(R.id.input_email);
        final EditText password = findViewById(R.id.input_password);
        final Button loginBtn = findViewById(R.id.loginBtn);
        final Button signUpBtn = findViewById(R.id.signUpBtn);
        final TextView errorTextView = findViewById(R.id.errorTextView);
        final CheckBox staySignedIn = findViewById(R.id.staySignedIn);

        SharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Editor = SharedPrefs.edit();

        boolean loggedIn = SharedPrefs.getBoolean(getString(R.string.SP_StayLoggedIn), false);
        int UserId = SharedPrefs.getInt(getString(R.string.SP_UserId), -1);

        if (loggedIn && UserId != -1) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
        loginBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                User user = SQLite.select().from(User.class)
                        .where(User_Table.UserName.eq(username.getText().toString()))
                        .and(User_Table.Password.eq(password.getText().toString()))
                        .querySingle();

                if (user != null) {
                    Editor.putInt(getString(R.string.SP_UserId), user.getUserId());
                    Editor.putBoolean(getString(R.string.SP_StayLoggedIn), staySignedIn.isChecked());
                    Editor.commit();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                    //correct password
                } else {
                    //Toast.makeText(getApplicationContext(), "Wrong Credentials", Toast.LENGTH_SHORT).show();
                    errorTextView.setVisibility(View.VISIBLE);
                }


                //wrong password


            }
        });
    }
}
