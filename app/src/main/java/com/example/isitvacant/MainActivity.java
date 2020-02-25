package com.example.isitvacant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MainActivity extends AppCompatActivity {
    boolean doubleBackToExitPressedOnce = false;
    FirebaseAuth mAuth;


    private FirebaseFirestore firebaseFirestore;

    private String uid,username;
    String currentUserName,currentUserImage,currentUserMobile;

    BottomNavigationView bottomNavigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        openFragments(Home_fragment.newInstance("", "", "",""));



    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();
    }

    public void openFragments(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);


        transaction.commit();
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            openFragment(Home_fragment.newInstance("", "", "",""));
                            return true;
                        case R.id.navigation_search:
                            openFragment(Search_fragment.newInstance("", "", "",""));
                            return true;
                        case R.id.navigation_reservation:
                            openFragment(Resevation_fragment.newInstance("", "", "",""));
                            return true;
                        case R.id.navigation_profile:
                            openFragment(profile_fragment.newInstance("", "", "",""));
                            return true;


                    }
                    return false;
                }
            };


    @Override
    public void onBackPressed() {
        //Checking for fragment count on backstack

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {

            getSupportFragmentManager().popBackStack();



        } else if (!doubleBackToExitPressedOnce) {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this,"Please click BACK again to exit.", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            super.onBackPressed();
            return;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent intent = new Intent(MainActivity.this, AuthActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
        }

        else {
            uid = mAuth.getCurrentUser().getUid();
            DocumentReference documentReference = firebaseFirestore.collection("users").document(uid);
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                    if (documentSnapshot.exists()) {
                        username = documentSnapshot.getString("name");

                        String gender2 = documentSnapshot.getString("Gender");
                        String dob2 = documentSnapshot.getString("Date Of Birth");
                        String nationality2 = documentSnapshot.getString("Nationality");

                        if (username==""|| gender2=="" || nationality2==""|| dob2=="") {
                            Intent intent1 = new Intent(MainActivity.this, HomeName.class);
                            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent1);
                        }
                    }
                    else{
                        Intent intent1 = new Intent(MainActivity.this, HomeName.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                    }

                }
            });



            DocumentReference documentReference1 = firebaseFirestore.collection("users").document(uid);
            documentReference1.addSnapshotListener(MainActivity.this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                    currentUserName = documentSnapshot.getString("name");
                    currentUserImage=documentSnapshot.getString("image");

                    currentUserMobile=documentSnapshot.getString("Mobile");

                }
            });


        }
    }
}
