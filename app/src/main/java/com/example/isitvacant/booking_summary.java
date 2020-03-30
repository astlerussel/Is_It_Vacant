package com.example.isitvacant;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.appbar.CollapsingToolbarLayout;

public class booking_summary extends AppCompatActivity {
    CollapsingToolbarLayout collapsingToolbarLayout;
    Button Payment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_summary);
        collapsingToolbarLayout = findViewById(R.id.collapsingtoolbar);
        collapsingToolbarLayout.setTitle("Chowks Food Court");
        collapsingToolbarLayout.setExpandedTitleColor(Color.parseColor("#099ae1"));
        collapsingToolbarLayout.setExpandedTitleGravity(Gravity.BOTTOM);
        collapsingToolbarLayout.setExpandedTitleTypeface(Typeface.DEFAULT_BOLD);
        Payment = findViewById(R.id.Payment);
        Payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(booking_summary.this, "Payment", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
