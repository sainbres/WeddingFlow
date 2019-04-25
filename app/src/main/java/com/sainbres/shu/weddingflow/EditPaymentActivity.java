package com.sainbres.shu.weddingflow;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

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

public class EditPaymentActivity extends AppCompatActivity {

    private SharedPreferences SharedPrefs;
    private SharedPreferences.Editor Editor;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    private SimpleDateFormat sdfStorage = new SimpleDateFormat("yyyy-MM-dd");
    private Payment payment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle extras = getIntent().getExtras();
        int paymentId = extras.getInt("PaymentId", -1);


        payment = new Payment();
        if (paymentId != -1){
            payment = SQLite.select()
                    .from(Payment.class)
                    .where(Payment_Table.PaymentId.eq(paymentId))
                    .querySingle();
        } else { //No way of editing so return home
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

        SharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Editor = SharedPrefs.edit();

        setContentView(R.layout.activity_edit_payment);

        Toolbar toolbar = findViewById(R.id.editPayment_Toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Edit Payment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));

        final TextInputLayout nameTil = findViewById(R.id.PaymentNameInputLayout);
        final TextInputLayout dateTil = findViewById(R.id.PaymentDateInputLayout);
        final TextInputLayout memoTil = findViewById(R.id.PaymentMemoInputLayout);
        final TextInputLayout amountTil = findViewById(R.id.PaymentAmountInputLayout);

        final EditText nameInput = findViewById(R.id.input_payment_name);
        final EditText dateInput = findViewById(R.id.input_payment_date);
        final EditText memoInput = findViewById(R.id.input_payment_memo);
        final EditText amountInput = findViewById(R.id.input_payment_amount);

        nameInput.setText(payment.getName());
        try {
            dateInput.setText(sdf.format(sdfStorage.parse(payment.getDate())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        memoInput.setText(payment.getMemo());

        double amount;
        if (payment.getDirection().equals("Out"))
        {
            amount = payment.getAmount() * -1;
        } else {
            amount = payment.getAmount();
        }
        amountInput.setText(Double.toString(amount));

        if (payment.getName().equals("Periodic Savings")){
            nameInput.setEnabled(false);
        }


        final Button addPaymentBtn = findViewById(R.id.editPaymentBtn);

        final Spinner spinnerDirection = findViewById(R.id.spinner_direction);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.direction_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDirection.setAdapter(adapter);

        int spinnerPos = 0;
        String originalDirection = payment.getDirection();
        String[] directionOptions = getResources().getStringArray(R.array.direction_array);
        for(int i = 0; i < directionOptions.length; i++)
        {
            if(originalDirection.equals(directionOptions[i]))
            {
                spinnerPos = i;
                break;
            }
        }

        spinnerDirection.setSelection(spinnerPos);

        dateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(EditPaymentActivity.this,
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
                dateInput.setText(dayOfMonth+"-"+month+"-"+year);
            }
        };

        addPaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameText = nameInput.getText().toString();
                String dateText = dateInput.getText().toString();
                String memoText = memoInput.getText().toString();
                String amountText = amountInput.getText().toString();
                String directionText = spinnerDirection.getSelectedItem().toString();

                Boolean allFieldsRequired = false;

                //Check if name has been entered
                if (nameText.equals("")){
                    nameTil.setError("Payment Name required");
                    nameInput.getBackground().setColorFilter(getResources().getColor(R.color.colorError), PorterDuff.Mode.SRC_ATOP);
                    allFieldsRequired = true;
                } else {
                    nameTil.setError(null);
                    nameInput.getBackground().clearColorFilter();
                }

                //Check if date has been entered
                if (dateText.equals("")){
                    dateTil.setError("Payment Date is required");
                    dateInput.getBackground().setColorFilter(getResources().getColor(R.color.colorError), PorterDuff.Mode.SRC_ATOP);
                    allFieldsRequired = true;
                } else {
                    dateTil.setError(null);
                    dateInput.getBackground().clearColorFilter();
                }

                //Check if date has been entered
                if (memoText.equals("")){
                    memoTil.setError("Payment Memo is required");
                    memoInput.getBackground().setColorFilter(getResources().getColor(R.color.colorError), PorterDuff.Mode.SRC_ATOP);
                    allFieldsRequired = true;
                } else {
                    memoTil.setError(null);
                    memoInput.getBackground().clearColorFilter();
                }

                //Check if date has been entered
                if (amountText.equals("")){
                    amountTil.setError("Payment Amount is required");
                    amountInput.getBackground().setColorFilter(getResources().getColor(R.color.colorError), PorterDuff.Mode.SRC_ATOP);
                    allFieldsRequired = true;
                } else {
                    amountTil.setError(null);
                    amountInput.getBackground().clearColorFilter();
                }

                if (allFieldsRequired){
                    //do nothing
                } else {
                    try {
                        int eventId = SharedPrefs.getInt(getString(R.string.SP_EventId), -1);

                        InitialBudget budget = SQLite.select()
                                .from(InitialBudget.class)
                                .where(InitialBudget_Table.EventId.eq(eventId))
                                .querySingle();

                        WeddingEvent event = SQLite.select()
                                .from(WeddingEvent.class)
                                .where(WeddingEvent_Table.EventId.eq(eventId))
                                .querySingle();


                        Date savingsStartDate = sdf.parse(budget.getSavingsStartDate());
                        Date savingsEndDate = sdf.parse(event.getWeddingDate());
                        Date paymentDate = sdf.parse(dateText);

                        if (paymentDate.after(savingsEndDate)){
                            dateTil.setError("Payment date cannot be after wedding");
                            dateInput.getBackground().setColorFilter(getResources().getColor(R.color.colorError), PorterDuff.Mode.SRC_ATOP);
                        } else {
                            dateTil.setError(null);
                            dateInput.getBackground().clearColorFilter();

                            if (paymentDate.before(savingsStartDate)){
                                dateTil.setError("Payment date cannot be before your savings started");
                                dateInput.getBackground().setColorFilter(getResources().getColor(R.color.colorError), PorterDuff.Mode.SRC_ATOP);
                            } else {
                                dateTil.setError(null);
                                dateInput.getBackground().clearColorFilter();


                                String dateStore = sdfStorage.format(sdf.parse(dateText));

                                payment.setName(nameText);
                                payment.setDate(dateStore);
                                payment.setMemo(memoText);
                                double amount;
                                if (directionText.equals("Out"))
                                {
                                    amount = Double.parseDouble(amountText) * -1;
                                } else {
                                    amount = Double.parseDouble(amountText);
                                }
                                payment.setAmount(amount);
                                payment.setDirection(directionText);
                                payment.save();

                                Bundle bundle = new Bundle();
                                bundle.putString("fragment", "budget");
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.putExtras(bundle);
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

