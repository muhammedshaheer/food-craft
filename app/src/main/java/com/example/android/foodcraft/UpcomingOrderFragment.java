package com.example.android.foodcraft;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kofigyan.stateprogressbar.StateProgressBar;

public class UpcomingOrderFragment extends Fragment  {

    private String[] descriptionData = {"Order\nPlaced", "Order\nDispatched", "Out for\nDelivery", "Delivered"};

    public UpcomingOrderFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_upcoming_order_fragment, container, false);

        StateProgressBar stateProgressBar = view.findViewById(R.id.your_state_progress_bar_id);
        stateProgressBar.setStateDescriptionData(descriptionData);

        stateProgressBar.setStateDescriptionTypeface("fonts/Lato-Regular.ttf");
        stateProgressBar.setStateNumberTypeface("fonts/Lato-Regular.ttf");



        return view;
    }
}
