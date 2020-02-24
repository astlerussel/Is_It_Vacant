package com.example.isitvacant;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static maes.tech.intentanim.CustomIntent.customType;

public class RestaurantsDetails extends AppCompatActivity {
    private RecyclerView reviewsRecyclerList;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference contactsRef ;
    private ReviewsAdapter adapter;
    FirebaseFirestore mstore;
    Button submit_rat_bt;
    FirebaseAuth mAuth;
    EditText reviewText;
    String ratingStr;
    String currentUid;
    TextView RESTO_NAME,Resto_Type,Resto_location,restoRating;
     String review_text;
    ImageView Restaurant_image;
    RatingBar ratingBar,ratingBar2;
    Activity activity;
    Query query;
    String profileRestoName,proUid,total_rating,proRestoImage,proMobile,proRestoGstin,proRestoAddr,proRestoDesc,proRestoType,currentUserName,userProImage;
 Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants_details);


        reviewsRecyclerList = (RecyclerView) findViewById(R.id.reviews_recycler_list);
        reviewsRecyclerList.setLayoutManager(new LinearLayoutManager(this));

        mstore=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        RESTO_NAME = findViewById(R.id.restoName);
        restoRating = findViewById(R.id.restaurant_rating_number);



        Resto_Type = findViewById(R.id.resto_type);
        Restaurant_image = findViewById(R.id.res_background_image);
        Resto_location = findViewById(R.id.resto_location);

        currentUid= mAuth.getCurrentUser().getUid();
        proUid=getIntent().getStringExtra("uid");
        contactsRef = db.collection("/restaurants/"+proUid+"/reviews");
        setUpRecyclerView();
        Resto_location.setText(proRestoAddr);
        Glide.with(getApplicationContext()).load(proRestoImage).into(Restaurant_image);
        ratingBar = findViewById(R.id.rating_bar);


        DocumentReference documentReferences = mstore.collection("users").document(currentUid);
        documentReferences.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {


                //Picasso.get().load(documentSnapshot.getString("image")).into(circleImageView);


                currentUserName=documentSnapshot.getString("name");
                userProImage = documentSnapshot.getString("image");








            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, final float rating, boolean fromUser) {
                Toast.makeText(RestaurantsDetails.this, ""+rating, Toast.LENGTH_SHORT).show();



                customType(RestaurantsDetails.this,"bottom-to-up");
                dialog = new Dialog(RestaurantsDetails.this);
                dialog.setContentView(R.layout.activity_pop_up_rating);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                ratingBar2 = dialog.findViewById(R.id.rating_bar2);
                ratingBar2.setRating(ratingBar.getRating());
                dialog.show();

                submit_rat_bt =  dialog.findViewById(R.id.submit_rat);
                reviewText = dialog.findViewById(R.id.review_text);
                ratingStr=String.valueOf(rating);



                submit_rat_bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, String> userMap = new HashMap<>();

                        userMap.put("name", currentUserName);

                        userMap.put("rating", ratingStr);
                        userMap.put("review", reviewText.getText().toString());
                        userMap.put("image",userProImage);






                        mstore.collection("restaurants")
                                .document(proUid).collection("reviews").document(currentUid)
                                .set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(RestaurantsDetails.this, "THANKS FOR YOUR REVIEW!!", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }


                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                String error = e.getMessage();
                                Toast.makeText(RestaurantsDetails.this, "Error" + error, Toast.LENGTH_LONG).show();
                            }
                        });



                        mstore.collection("/restaurants/"+proUid+"/reviews")
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot value,
                                                        @Nullable FirebaseFirestoreException e) {


                                        List<String> allRatings = new ArrayList<>();
                                        for (QueryDocumentSnapshot doc : value) {
                                            if (doc.get("rating") != null) {
                                                allRatings.add(doc.getString("rating"));
                                                //Toast.makeText(GroupProfileActivity.this, "Messages is: "+value,Toast.LENGTH_LONG).show();
                                            }
                                        }
                                        //Toast.makeText(GroupChatActivity.this, "Messages are: "+allMssages,Toast.LENGTH_LONG).show();


                                        float sum=0;

                                        for(int i=0; i<allRatings.size(); i++)

                                        {



                                            sum=sum+Float.parseFloat(allRatings.get(i));









                                        }
                                        float avg;
                                        avg=sum/allRatings.size();
                                        double roundAvg = Math.round(avg*10.0)/10.0;

                                        Map<String, Object> totalRatingMap = new HashMap<>();
                                        totalRatingMap.put("total_rating", String.valueOf(roundAvg));



                                        mstore.collection("restaurants").document(proUid)
                                                .update(totalRatingMap)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        //Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                                        //Toast.makeText(GroupProfileActivity.this, "Group Deleted", Toast.LENGTH_LONG).show();

                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        String error = e.getMessage();
                                                        Toast.makeText(RestaurantsDetails.this, "Error"+error, Toast.LENGTH_LONG).show();

                                                    }
                                                });
                                    }
                                });
                    }
                });












            }






        });











    }



    private void setUpRecyclerView() {



        query = contactsRef.orderBy("name");







        FirestoreRecyclerOptions<ModelReviews> options = new FirestoreRecyclerOptions.Builder<ModelReviews>()
                .setQuery(query, ModelReviews.class)
                .build();

        adapter = new ReviewsAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.reviews_recycler_list);
        recyclerView.setHasFixedSize(false);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        proUid=getIntent().getStringExtra("uid");

        adapter.startListening();


        DocumentReference documentReference = mstore.collection("restaurants").document(proUid);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {


                //Picasso.get().load(documentSnapshot.getString("image")).into(circleImageView);


                proMobile=documentSnapshot.getString("Mobile");
                profileRestoName = documentSnapshot.getString("name");
                proRestoImage=documentSnapshot.getString("image");
                proRestoGstin=documentSnapshot.getString("GSTIN_NUMBER");
                proRestoAddr=documentSnapshot.getString("Address");
                proRestoDesc=documentSnapshot.getString("discription");
                total_rating=documentSnapshot.getString("total_rating");
                restoRating.setText(total_rating);
                proRestoType=documentSnapshot.getString("Type");
                RESTO_NAME.setText(profileRestoName);
                Resto_Type.setText(documentSnapshot.getString("Type"));
                Resto_location.setText(proRestoAddr);
                Picasso.get().load(proRestoImage).into(Restaurant_image);







            }
        });


    }
}
