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
import com.sainbres.shu.weddingflow.Models.Payment_Table;
import com.sainbres.shu.weddingflow.Models.WeddingEvent;
import com.sainbres.shu.weddingflow.Models.WeddingEvent_Table;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditInitialBudgetActivity extends AppCompatActivity {

    private SharedPreferences SharedPrefs;
    private SharedPreferences.Editor Editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_initial_budget);

        SharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Editor = SharedPrefs.edit();

        Toolbar toolbar = findViewById(R.id.editBudget_Toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Edit Savings");
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

        final Button editBudgetBtn = findViewById(R.id.editBudgetBtn);

        int budgetId = SharedPrefs.getInt(getString(R.string.SP_BudgetId), -1);

        final InitialBudget originalBudget = SQLite.select()
                .from(InitialBudget.class)
                .where(InitialBudget_Table.BudgetId.eq(budgetId))
                .querySingle();


        initSavingsInput.setText(Double.toString(originalBudget.getSavingsStart()));
        periodicSavingsInput.setText(Double.toString(originalBudget.getSavingsPeriodic()));

        int spinnerPos = 0;
        String origianlPeriodicity = originalBudget.getSavingsPeriodicity();
        String[] periodicityOptions = getResources().getStringArray(R.array.periodicity_array);
        for(int i = 0; i < periodicityOptions.length; i++)
        {
            if(origianlPeriodicity.equals(periodicityOptions[i]))
            {
                spinnerPos = i;
                break;
            }
        }

        spinnerPeriodicity.setSelection(spinnerPos);

        editBudgetBtn.setOnClickListener(new View.OnClickListener() {
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

                    originalBudget.setSavingsStart(Double.parseDouble(initSavingsText));

                    Boolean redoSavingPaymentsCheck = false;

                    if (originalBudget.getSavingsPeriodic() != Double.parseDouble(periodicSavingsText)) {
                        redoSavingPaymentsCheck = true;
                        originalBudget.setSavingsPeriodic(Double.parseDouble(periodicSavingsText));
                    }

                    if (!originalBudget.getSavingsPeriodicity().equals(periodicityText)) {
                        redoSavingPaymentsCheck = true;
                        originalBudget.setSavingsPeriodicity(periodicityText);
                    }
                    originalBudget.save();

                    if (redoSavingPaymentsCheck) {
                        removeSavingPayments(originalBudget);
                        addSavingPayments(originalBudget);
                    }

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
    public void removeSavingPayments(InitialBudget budget){
        SQLite.delete()
                .from(Payment.class)
                .where(Payment_Table.BudgetId.eq(budget.getBudgetId()))
                .and(Payment_Table.Name.eq("Periodic Savings"))
                .execute();
    }

    public void addSavingPayments(InitialBudget budget){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat sdfStorage = new SimpleDateFormat("yyyy-MM-dd");

        int eventId = SharedPrefs.getInt(getString(R.string.SP_EventId), -1);

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
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Bundle bundle = new Bundle();
                bundle.putString("fragment", "budget");
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Bundle bundle = new Bundle();
        bundle.putString("fragment", "budget");
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}