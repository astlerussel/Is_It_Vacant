package com.example.isitvacant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Thank_You extends AppCompatActivity {
    TextView Home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank__you);
        Home = findViewById(R.id.Home);
        Home.setPaintFlags(Home.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }
}
