package com.sainbres.shu.weddingflow;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sainbres.shu.weddingflow.Models.Payment;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<paymentViewHolder> {

    public List<Payment> list = Collections.emptyList();
    Context context;

    public PaymentAdapter(List<Payment> list, Context context)
    {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public paymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the layout

        View photoView = inflater.inflate(R.layout.card_payment,
                parent, false);

        paymentViewHolder viewHolder = new paymentViewHolder(photoView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull paymentViewHolder paymentViewHolder, int i) {
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        paymentViewHolder.paymentName.setText(list.get(i).getName());
        paymentViewHolder.paymentMemo.setText(list.get(i).getMemo());
        double amount = list.get(i).getAmount();
        paymentViewHolder.paymentAmount.setText(currency.format(amount));
        if (amount < 0)
        {
            paymentViewHolder.paymentAmount.setTextColor(Color.parseColor("#b00200"));
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat sdfStorage = new SimpleDateFormat("yyyy-MM-dd");
        String dateShow = "";
        try {
            dateShow = sdf.format(sdfStorage.parse(list.get(i).getDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        paymentViewHolder.paymentDate.setText(dateShow);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public List<Payment> getPayments()
    {
        return list;
    }
}
