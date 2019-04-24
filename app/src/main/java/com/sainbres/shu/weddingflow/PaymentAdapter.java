package com.sainbres.shu.weddingflow;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sainbres.shu.weddingflow.Models.Payment;

import java.util.Collections;
import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<paymentViewHolder> {

    List<Payment> list = Collections.emptyList();
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
        paymentViewHolder.paymentName.setText(list.get(i).getName());
        paymentViewHolder.paymentMemo.setText(list.get(i).getMemo());
        paymentViewHolder.paymentAmount.setText(Double.toString(list.get(i).getAmount()));
        paymentViewHolder.paymentDate.setText(list.get(i).getDate());
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
}
