package com.example.isitvacant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class edit_profile extends AppCompatActivity {

    Button update_btn;
    EditText ET_NAME;
    EditText mob;
    TextView nation_tv,gender_tv,dob_tv1;
    String name1,mobb,uid;
    FirebaseFirestore mstore;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        update_btn = findViewById(R.id.update_bt);
        ET_NAME = findViewById(R.id.update_user_name);
        mob = findViewById(R.id.update_mob);
        mob.setEnabled(false);

        nation_tv = findViewById(R.id.nation_tv);
        gender_tv = findViewById(R.id.gender_tv);
        dob_tv1 = findViewById(R.id.dob_tv);
        mAuth = FirebaseAuth.getInstance();
        mstore = FirebaseFirestore.getInstance();
        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name1 = ET_NAME.getText().toString();
                if (name1.isEmpty()) {

                    ET_NAME.setError("Mandatory Field...");



                } else {
                    mobb=mob.getText().toString();







                    Map<String, Object> userMap = new HashMap<>();

                    userMap.put("name", name1);



                    mstore.collection("users")
                            .document(uid)
                            .update(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "User Profile Updated", Toast.LENGTH_LONG).show();
                        }


                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String error = e.getMessage();
                            Toast.makeText(getApplicationContext(), "Error" + error, Toast.LENGTH_LONG).show();
                        }
                    });










                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        uid = mAuth.getCurrentUser().getUid();
        DocumentReference documentReference = mstore.collection("users").document(uid);
        documentReference.addSnapshotListener(edit_profile.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                ET_NAME.setText(documentSnapshot.getString("name"));

                //Picasso.get().load(documentSnapshot.getString("image")).into(circleImageView);

                mob.setText(documentSnapshot.getString("Mobile"));

                gender_tv.setText(documentSnapshot.getString("Gender"));
                dob_tv1.setText(documentSnapshot.getString("Date Of Birth"));
                nation_tv.setText(documentSnapshot.getString("Nationality"));

            }
        });
    }
}
