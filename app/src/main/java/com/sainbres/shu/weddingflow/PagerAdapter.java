package com.sainbres.shu.weddingflow;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sainbres.shu.weddingflow.BudgetTabFragments.BudgetBreakdownFragment;
import com.sainbres.shu.weddingflow.BudgetTabFragments.BudgetOverviewFragment;

public class PagerAdapter extends FragmentPagerAdapter {

    int mNoOfTabs;

    public PagerAdapter(FragmentManager fm, int NumberOfTabs)
    {
        super(fm);
        this.mNoOfTabs = NumberOfTabs;
    }

    @Override
    public Fragment getItem(int i) {
        switch(i)
        {
            case 0:
                BudgetOverviewFragment budgetOverview = new BudgetOverviewFragment();
                return budgetOverview;
            case 1:
                BudgetBreakdownFragment budgetBreakdown = new BudgetBreakdownFragment();
                return budgetBreakdown;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}
