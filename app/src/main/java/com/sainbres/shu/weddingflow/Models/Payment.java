package com.sainbres.shu.weddingflow.Models;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.sainbres.shu.weddingflow.WeddingFlowDB;

@Table(database = WeddingFlowDB.class, name = "Payments")
public class Payment extends BaseModel {

    public Payment() {
    }

    @PrimaryKey(autoincrement = true)
    int PaymentId;

    @Column
    int BudgetId;

    @Column
    String Name;

    @Column
    String Memo;

    @Column
    String Date;

    @Column
    double Amount;

    @Column
    String Direction;

    public int getPaymentId() {
        return PaymentId;
    }
    public void setPaymentId(int paymentId) {
        PaymentId = paymentId;
    }

    public int getBudgetId() {
        return BudgetId;
    }
    public void setBudgetId(int budgetId) {
        BudgetId = budgetId;
    }

    public String getName() { return Name; }
    public void setName(String name) { Name = name; }

    public String getMemo() { return Memo; }
    public void setMemo(String memo) { Memo = memo; }

    public String getDate() {
        return Date;
    }
    public void setDate(String date) {
        Date = date;
    }

    public double getAmount() {
        return Amount;
    }
    public void setAmount(double amount) {
        Amount = amount;
    }

    public String getDirection() {
        return Direction;
    }
    public void setDirection(String direction) {
        Direction = direction;
    }
}