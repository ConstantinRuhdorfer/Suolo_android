
package com.ruhdocon.digitalfarming_tbd_ph;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Analysis.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Analysis#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Analysis extends Fragment {


    float[] phValues_data;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Analysis() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Analysis.
     */
    // TODO: Rename and change types and number of parameters
    public static Analysis newInstance(String param1, String param2) {
        Analysis fragment = new Analysis();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Bundle bundle = this.getArguments();
        phValues_data = bundle.getFloatArray("Ph_values");

        return inflater.inflate(R.layout.fragment_analysis, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setUpChart(phValues_data);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void setUpChart (float[] data) {
        LineChart lineChart = (LineChart) Objects.requireNonNull(getView()).findViewById(R.id.chartView);
        List<Entry> entries = new ArrayList<>();
        int i = 0;
        for (Float f : data) {
            i++;
            entries.add( new Entry(i, f) );
        }
        LineDataSet dataSet = new LineDataSet(entries, "Label");
        dataSet.setColor(R.color.redGauge);
        dataSet.setValueTextColor(R.color.white);

        dataSet.setLabel("PH Values");

        LineData lineData = new LineData(dataSet);

        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setDrawValues(false);
        dataSet.setLineWidth(1f);
        dataSet.setCircleRadius(1f);
        dataSet.setCircleHoleRadius(0);
        dataSet.setColor(Color.BLACK);
        dataSet.setCircleColor(Color.BLACK);
        dataSet.setHighLightColor(Color.BLACK);

        lineChart.getDescription().setEnabled(false);
        lineChart.setPinchZoom(true);
        lineChart.getLegend().setForm(Legend.LegendForm.LINE);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getAxisLeft().setAxisMaximum(14f);
        lineChart.getAxisRight().setAxisMaximum(14f);
        lineChart.getAxisLeft().setAxisMinimum(0f);
        lineChart.getAxisRight().setAxisMinimum(0f);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.setData(lineData);
        lineChart.setNoDataText("Sorry there is no data.");
        lineChart.invalidate();
    }
}
