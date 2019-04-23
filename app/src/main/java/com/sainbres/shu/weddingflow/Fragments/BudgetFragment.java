package com.sainbres.shu.weddingflow.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.sainbres.shu.weddingflow.R;

import java.util.Calendar;
import java.util.Date;


public class BudgetFragment extends Fragment {

    private View view;

    public BudgetFragment() {
        // Required empty public constructor
    }

    public static BudgetFragment newInstance(String param1, String param2) {
        BudgetFragment fragment = new BudgetFragment();
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
        view = inflater.inflate(R.layout.fragment_budget, container, false);

        GraphView graph = (GraphView) view.findViewById(R.id.graph);

        Calendar cal = Calendar.getInstance();
        Date d1 = cal.getTime();
        cal.add(Calendar.DATE, 1);
        Date d2 = cal.getTime();
        cal.add(Calendar.DATE, 2);
        Date d3 = cal.getTime();

        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(d1, 2500),
                new DataPoint(d2, 2750),
                new DataPoint(d3, 2000)
        });
        graph.addSeries(series);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Months");
        graph.getGridLabelRenderer().setVerticalAxisTitle("£");

        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
        graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space

        graph.getViewport().setMinX(d1.getTime());
        graph.getViewport().setMaxX(d3.getTime());
        graph.getViewport().setXAxisBoundsManual(true);

        graph.getGridLabelRenderer().setHumanRounding(false);

        return view;
    }

}