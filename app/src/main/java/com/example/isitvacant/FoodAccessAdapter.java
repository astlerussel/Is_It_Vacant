package com.example.isitvacant;



import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class FoodAccessAdapter extends FragmentPagerAdapter {


    public FoodAccessAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                Breakfast_fragment breakfast_fragment = new Breakfast_fragment();
                return breakfast_fragment;
            case 1:
                Lunch_fragment lunch_fragment = new Lunch_fragment();
                return lunch_fragment;
            case 2:
                Dinner_fragment dinner_fragment = new Dinner_fragment();
                return dinner_fragment;
            default:
                return null;

        }

    }


    @Override
    public int getCount() {
        return 3;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "BreakFast";
            case 1:
                return "Lunch";
            case 2:
                return "Dinner";

            default:
                return null;

        }
    }
}
