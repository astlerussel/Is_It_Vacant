package com.example.isitvacant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class spash_screen extends AppCompatActivity {
    private static int SPLASH_SCREEN = 5000;

    Animation topAnim,bottomAnim;
    TextView logo,slogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash_screen);
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_splash_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_splash_animation);

        logo = findViewById(R.id.logo);
        slogan = findViewById(R.id.slogan);


        logo.setAnimation(topAnim);
        slogan.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(spash_screen.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);
    }
}
