package com.sainbres.shu.weddingflow.Models;


import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.data.Blob;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.sainbres.shu.weddingflow.WeddingFlowDB;



@Table(database = WeddingFlowDB.class, name = "InitialBudgets")
public class InitialBudget extends BaseModel {

    public InitialBudget() {
    }

    @PrimaryKey(autoincrement = true)
    int BudgetId;

    @Column
    int EventId;

    @Column
    double SavingsStart;

    @Column
    double SavingsPeriodic;

    @Column
    String SavingsPeriodicity;

    @Column
    String SavingsStartDate;


    public int getBudgetId() {
        return BudgetId;
    }
    public void setBudgetId(int budgetId) {
        BudgetId = budgetId;
    }

    public int getEventId() {
        return EventId;
    }
    public void setEventId(int eventId) {
        EventId = eventId;
    }

    public double getSavingsStart() { return SavingsStart; }
    public void setSavingsStart(double savingsStart) { SavingsStart = savingsStart; }

    public double getSavingsPeriodic() { return SavingsPeriodic; }
    public void setSavingsPeriodic(double savingsPeriodic) { SavingsPeriodic = savingsPeriodic; }

    public String getSavingsPeriodicity() { return SavingsPeriodicity; }
    public void setSavingsPeriodicity(String savingsPeriodicity) { SavingsPeriodicity = savingsPeriodicity; }

    public String getSavingsStartDate() { return SavingsStartDate; }
    public void setSavingsStartDate(String savingsStartDate) { SavingsStartDate = savingsStartDate; }

}