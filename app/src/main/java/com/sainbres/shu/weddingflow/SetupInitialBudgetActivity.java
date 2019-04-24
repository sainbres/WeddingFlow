package com.sainbres.shu.weddingflow;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.sainbres.shu.weddingflow.Models.InitialBudget;
import com.sainbres.shu.weddingflow.Models.WeddingEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

        final TextInputLayout initSavingsTil = findViewById(R.id.InitSavingsInputLayout);
        final TextInputLayout periodicSavingsTil = findViewById(R.id.PeriodicSavingsInputLayout);

        final EditText initSavingsInput = findViewById(R.id.input_init);
        final EditText periodicSavingsInput = findViewById(R.id.input_periodic_savings);

        final Spinner spinnerPeriodicity = findViewById(R.id.spinner_periodicity);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.periodicity_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPeriodicity.setAdapter(adapter);

        final Button completeSetupBtn = findViewById(R.id.completeSetupBtn);

        completeSetupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String initSavingsText = initSavingsInput.getText().toString();
                String periodicSavingsText = periodicSavingsInput.getText().toString();
                String periodicityText = spinnerPeriodicity.getSelectedItem().toString();

                Boolean allFieldsRequired = false;

                //Check if initSavings has been entered
                if (initSavingsText.equals("")){
                    initSavingsTil.setError("Initial savings required, set 0 for starting with nothing");
                    initSavingsInput.getBackground().setColorFilter(getResources().getColor(R.color.colorError), PorterDuff.Mode.SRC_ATOP);
                    allFieldsRequired = true;
                } else {
                    initSavingsTil.setError(null);
                    initSavingsInput.getBackground().clearColorFilter();
                }
                //Check if periodicSavings has been entered
                if (periodicSavingsText.equals("")){
                    periodicSavingsTil.setError("Periodic savings required, set 0 if not currently saving");
                    periodicSavingsInput.getBackground().setColorFilter(getResources().getColor(R.color.colorError), PorterDuff.Mode.SRC_ATOP);
                    allFieldsRequired = true;
                } else {
                    periodicSavingsTil.setError(null);
                    periodicSavingsInput.getBackground().clearColorFilter();
                }
                if (allFieldsRequired){
                    //do nothing
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    Date dateNow = new Date();
                    String dateNowStr = sdf.format(dateNow);

                    int eventId = SharedPrefs.getInt(getString(R.string.SP_EventId), -1);

                    InitialBudget budget = new InitialBudget();
                    budget.setEventId(eventId);
                    budget.setSavingsStart(Double.parseDouble(initSavingsText));
                    budget.setSavingsPeriodic(Double.parseDouble(periodicSavingsText));
                    budget.setSavingsPeriodicity(periodicityText);
                    budget.setSavingsStartDate(dateNowStr);
                    budget.save();

                    Bundle bundle = new Bundle();
                    bundle.putString("fragment", "budget");
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
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
