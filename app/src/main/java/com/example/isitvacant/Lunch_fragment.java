package com.example.isitvacant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

public class Lunch_fragment extends Fragment {


    private View groupsFreagmentView;

    ElegantNumberButton elegantNumberButton;
    Button Add;








    public Lunch_fragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        groupsFreagmentView = inflater.inflate(R.layout.activity_lunch_fragment, container, false);
        elegantNumberButton = groupsFreagmentView.findViewById(R.id.elegantNumber);
        Add = groupsFreagmentView.findViewById(R.id.add_food);

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Add.setVisibility(View.INVISIBLE);
                elegantNumberButton.setVisibility(View.VISIBLE);
                elegantNumberButton.setNumber("1");
                elegantNumberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                    @Override
                    public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                        if(newValue == 0)
                        {
                            Add.setVisibility(View.VISIBLE);
                            elegantNumberButton.setVisibility(View.INVISIBLE);
                        }
                    }
                });


            }
        });









        return groupsFreagmentView;
    }




}
