package com.example.isitvacant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;

public class FoodOdering extends AppCompatActivity {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private Toolbar mToolbar;
    private FoodAccessAdapter myFoodAccessAdapter;


    private String mParam1;
    private String mParam2;
    private String mParam3;
    private String mParam4;


    BottomSheetDialog dialog;
    TextView cart,Reserve;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_odering);

        mViewPager = findViewById(R.id.main_tabs_pager);
        myFoodAccessAdapter = new FoodAccessAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(myFoodAccessAdapter);
        mTabLayout = findViewById(R.id.main_tab);
        mTabLayout.setupWithViewPager(mViewPager);

        cart = findViewById(R.id.cart);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new BottomSheetDialog(FoodOdering.this);
                dialog.setContentView(R.layout.view_cart);
                Reserve = dialog.findViewById(R.id.Reserve_food);
                Reserve.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(FoodOdering.this,booking_summary.class));
                    }
                });

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });




    }
}




