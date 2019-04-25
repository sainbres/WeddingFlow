package com.sainbres.shu.weddingflow.BudgetTabFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.sainbres.shu.weddingflow.Models.InitialBudget;
import com.sainbres.shu.weddingflow.Models.InitialBudget_Table;
import com.sainbres.shu.weddingflow.Models.Payment;
import com.sainbres.shu.weddingflow.Models.Payment_Table;
import com.sainbres.shu.weddingflow.R;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class BudgetOverviewFragment extends Fragment {

    private SharedPreferences SharedPrefs;
    private SharedPreferences.Editor Editor;

    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    private SimpleDateFormat sdfStorage = new SimpleDateFormat("yyyy-MM-dd");

    public BudgetOverviewFragment() {
        // Required empty public constructor
    }

    public static BudgetOverviewFragment newInstance(String param1, String param2) {
        BudgetOverviewFragment fragment = new BudgetOverviewFragment();
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
        View view = inflater.inflate(R.layout.fragment_budget_overview, container, false);

        SharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Editor = SharedPrefs.edit();
        NumberFormat currency = NumberFormat.getCurrencyInstance();

        int budgetId = SharedPrefs.getInt(getString(R.string.SP_BudgetId), -1);

        InitialBudget budget = SQLite.select()
                .from(InitialBudget.class)
                .where(InitialBudget_Table.BudgetId.eq(budgetId))
                .querySingle();

        TextView currentEstimatedFunds = view.findViewById(R.id.estimatedFunds);

        TextView periodicitySavingsLabel = view.findViewById(R.id.monthlySavingsLabel);
        TextView periodicitySavings = view.findViewById(R.id.monthlySavings);
        TextView upcomingBill = view.findViewById(R.id.upcomingBill);
        TextView upcomingIncrease = view.findViewById(R.id.upcomingIncrease);
        TextView nextNegative = view.findViewById(R.id.nextNegative);
        TextView predictedSurplus = view.findViewById(R.id.predictedSurplus);

        Date dateNow = new Date();

        periodicitySavingsLabel.setText(budget.getSavingsPeriodicity() + " Savings:");
        periodicitySavings.setText(currency.format(budget.getSavingsPeriodic()));

        try {

        boolean negativeBalanceCheck = false;
        boolean nextBillCheck = false;
        boolean nextIncreaseCheck = false;
        boolean currentBalanceCheck = false;
        Date paymentDate = new Date();
        double paymentAmount = budget.getSavingsStart();
        String paymentDirection = "In";

            paymentDate = sdf.parse(budget.getSavingsStartDate());

        Date lastPaymentDate = new Date();
        double lastPaymentAmount = 0;
        double ongoingFunds = paymentAmount;


        List<Payment> payments = SQLite.select()
                .from(Payment.class)
                .where(Payment_Table.BudgetId.eq(budgetId))
                .orderBy(Payment_Table.Date, true)
                .queryList();

        for(int i = 0; i < payments.size(); i++){

            double onGoingFundsBefore = ongoingFunds;
            lastPaymentAmount = paymentAmount;
            lastPaymentDate = paymentDate;


            paymentDate = sdfStorage.parse(payments.get(i).getDate());
            paymentAmount = payments.get(i).getAmount();
            paymentDirection = payments.get(i).getDirection();

            if (paymentDate.after(dateNow) && !nextBillCheck && paymentDirection.equals("Out")){
                nextBillCheck = true;
                upcomingBill.setText(sdf.format(paymentDate));
            }
            if (paymentDate.after(dateNow) && !nextIncreaseCheck && paymentDirection.equals("In")){
                nextIncreaseCheck = true;
                upcomingIncrease.setText(sdf.format(paymentDate));
            }
            if (paymentDate.after(dateNow) && !currentBalanceCheck){
                currentBalanceCheck = true;
                currentEstimatedFunds.setText(currency.format(ongoingFunds));
            }

            ongoingFunds = ongoingFunds + paymentAmount;

            if (onGoingFundsBefore < 0 && ongoingFunds < 0 && !negativeBalanceCheck && lastPaymentDate.before(dateNow) && paymentDate.after(dateNow)){
                negativeBalanceCheck = true;
                nextNegative.setText("Now");
                nextNegative.setTextColor(getResources().getColor(R.color.colorError));
            } else if (onGoingFundsBefore > 0 && ongoingFunds < 0 && !negativeBalanceCheck && paymentDate.after(dateNow)) {
                negativeBalanceCheck = true;
                nextNegative.setText(sdf.format(paymentDate));
                nextNegative.setTextColor(getResources().getColor(R.color.colorError));
            }


        }

        predictedSurplus.setText(currency.format(ongoingFunds));
        if (ongoingFunds < 0){
            predictedSurplus.setTextColor(getResources().getColor(R.color.colorError));
        }

        } catch (ParseException e) {
            e.printStackTrace();
        }
            return view;

    }
}