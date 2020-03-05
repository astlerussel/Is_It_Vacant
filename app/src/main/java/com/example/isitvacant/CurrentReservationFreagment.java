package com.example.isitvacant;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;




/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentReservationFreagment extends Fragment {

     CardView currentReservation;
    private View contactsFreagmentView;






    public CurrentReservationFreagment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_current, container, false);
        currentReservation = view.findViewById(R.id.current_reservation_cardview);
        currentReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),current_onclick_cardview.class));
            }
        });












        return view;
    }





}
