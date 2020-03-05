package com.example.isitvacant;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;

import com.google.android.material.appbar.CollapsingToolbarLayout;

public class booking_summary extends AppCompatActivity {
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_summary);
        collapsingToolbarLayout = findViewById(R.id.collapsingtoolbar);
        collapsingToolbarLayout.setTitle("Chowks Food Court");
        collapsingToolbarLayout.setExpandedTitleColor(Color.parseColor("#099ae1"));
        collapsingToolbarLayout.setExpandedTitleGravity(Gravity.BOTTOM);
        collapsingToolbarLayout.setExpandedTitleTypeface(Typeface.DEFAULT_BOLD);
    }
}
