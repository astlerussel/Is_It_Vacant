package com.example.isitvacant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

import static maes.tech.intentanim.CustomIntent.customType;

public class pop_up_rating extends Activity {
    FirebaseFirestore mstore;
    Button submit_rat_bt;
    FirebaseAuth mAuth;
    EditText reviewText;
    String ratingStr;
    String currentUid;
    TextView RESTO_NAME,Resto_Type,Resto_location;
    String review_text;
    ImageView Restaurant_image;
    RatingBar ratingBar;
    String profileRestoName,proUid,proRestoImage,proMobile,proRestoGstin,proRestoAddr,proRestoDesc,proRestoType,currentUserName,userProImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_rating);
        submit_rat_bt =  findViewById(R.id.submit_rat);
        reviewText = findViewById(R.id.review_text);
        currentUid= mAuth.getCurrentUser().getUid();
        proUid=getIntent().getStringExtra("uid");

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);









        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.7));

        WindowManager.LayoutParams params =  getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x =0;
        params.y = -20;

        getWindow().setAttributes(params);
    }
}
