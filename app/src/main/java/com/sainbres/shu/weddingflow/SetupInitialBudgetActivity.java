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

import com.jjoe64.graphview.series.DataPoint;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.sainbres.shu.weddingflow.Models.InitialBudget;
import com.sainbres.shu.weddingflow.Models.InitialBudget_Table;
import com.sainbres.shu.weddingflow.Models.Payment;
import com.sainbres.shu.weddingflow.Models.WeddingEvent;
import com.sainbres.shu.weddingflow.Models.WeddingEvent_Table;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
                    SimpleDateFormat sdfStorage = new SimpleDateFormat("yyyy-MM-dd");
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

                    budget = SQLite.select()
                            .from(InitialBudget.class)
                            .where(InitialBudget_Table.EventId.eq(eventId))
                            .querySingle();

                    WeddingEvent event = SQLite.select()
                            .from(WeddingEvent.class)
                            .where(WeddingEvent_Table.EventId.eq(eventId))
                            .querySingle();

                    if (budget != null || event != null)
                    {
                        Date savingsEndDate = null;
                        Date periodicPaymentDate = null;
                        Calendar cal = Calendar.getInstance();
                        try {
                            savingsEndDate = sdf.parse(event.getWeddingDate());
                            periodicPaymentDate = sdf.parse(budget.getSavingsStartDate());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        switch (budget.getSavingsPeriodicity())
                            {
                                case "Weekly": {
                                    do {
                                        cal.setTime(periodicPaymentDate);
                                        cal.add(Calendar.WEEK_OF_YEAR, 1);
                                        periodicPaymentDate = cal.getTime();
                                        Payment payment = new Payment();
                                        payment.setName("Periodic Savings");
                                        payment.setMemo("Automatic weekly savings");
                                        payment.setAmount(budget.getSavingsPeriodic());
                                        payment.setDate(sdfStorage.format(periodicPaymentDate));
                                        payment.setBudgetId(budget.getBudgetId());
                                        payment.setDirection("In");
                                        payment.save();
                                    } while (periodicPaymentDate.before(savingsEndDate));
                                }
                                case "Monthly":{
                                    do {
                                        cal.setTime(periodicPaymentDate);
                                        cal.add(Calendar.MONTH, 1);
                                        periodicPaymentDate = cal.getTime();
                                        Payment payment = new Payment();
                                        payment.setName("Periodic Savings");
                                        payment.setMemo("Automatic monthly savings");
                                        payment.setAmount(budget.getSavingsPeriodic());
                                        payment.setDate(sdfStorage.format(periodicPaymentDate));
                                        payment.setBudgetId(budget.getBudgetId());
                                        payment.setDirection("In");
                                        payment.save();
                                    } while (periodicPaymentDate.before(savingsEndDate));
                                }
                                case "Quarterly": {
                                    do {
                                        cal.setTime(periodicPaymentDate);
                                        cal.add(Calendar.MONTH, 3);
                                        periodicPaymentDate = cal.getTime();
                                        Payment payment = new Payment();
                                        payment.setName("Periodic Savings");
                                        payment.setMemo("Automatic quarterly savings");
                                        payment.setAmount(budget.getSavingsPeriodic());
                                        payment.setDate(sdfStorage.format(periodicPaymentDate));
                                        payment.setBudgetId(budget.getBudgetId());
                                        payment.setDirection("In");
                                        payment.save();
                                    } while (periodicPaymentDate.before(savingsEndDate));
                                }
                            }
                            /*
                            if (budget.getSavingsPeriodicity().equals("Monthly")){
                                cal.setTime(periodicPaymentDate);
                                cal.add(Calendar.MONTH, 1);
                                periodicPaymentDate = cal.getTime();
                            }
                            ongoingSavings = ongoingSavings + budget.getSavingsPeriodic();
                            dataPoints.add(new DataPoint(periodicPaymentDate, ongoingSavings));
                            */
                        }
                    }
                    /*



                    */
                    Bundle bundle = new Bundle();
                    bundle.putString("fragment", "budget");
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
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
