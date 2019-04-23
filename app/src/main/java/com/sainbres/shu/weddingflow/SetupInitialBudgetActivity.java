package com.sainbres.shu.weddingflow;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SetupInitialBudgetActivity extends AppCompatActivity {

    private SharedPreferences SharedPrefs;
    private SharedPreferences.Editor Editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_initial_budget);

        SharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Editor = SharedPrefs.edit();

        Toolbar toolbar = findViewById(R.id.setupBudget_Toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Setup Initial Budget");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Spinner spinner = findViewById(R.id.spinner_periodicity);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.periodicity_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
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
