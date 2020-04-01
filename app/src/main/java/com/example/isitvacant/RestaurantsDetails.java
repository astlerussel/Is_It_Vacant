package com.example.isitvacant;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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
import com.vivekkaushik.datepicker.DatePickerTimeline;
import com.vivekkaushik.datepicker.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestaurantsDetails extends AppCompatActivity {
    private RecyclerView reviewsRecyclerList;
    private RecyclerView menuRecyclerList;


    private RecyclerView timeSlotRecyclerList;
    private RecyclerView menu2Recycler;
    private CollectionReference timeRef,menuRef;



    private TimeSlotAdapter Tadapter;
    private DisplayMenuAdapter menuAdapter;
    Query Tquery,menuQuery,menu2Query;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference contactsRef ;
    private ReviewsAdapter adapter;

    int minteger = 0;
    static int flag =0;
    static String time_slot;
    FirebaseFirestore mstore;
    Button submit_rat_bt,book_bt,increse_bt,deacrease_bt,Book;
    FirebaseAuth mAuth;
    EditText reviewText;
    String book_date,book_date2;
    String ratingStr;
    String currentUid;
    TextView RESTO_NAME,Resto_Type,Resto_location,restoRating,book_resto_title,book_resto_location,Time_slot;
     String review_text;
    ImageView Restaurant_image,resto_book_image;
    RatingBar ratingBar,ratingBar2;

    Query query;
    String profileRestoName,proUid,total_rating,proRestoImage,proMobile,proRestoGstin,proRestoAddr,proRestoDesc,proRestoType,currentUserName,userProImage;
 Dialog dialog;
 BottomSheetDialog dialog1;
    private static String types = "BreakFast";
    private DisplayMenuAdapter menu2Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants_details);


        reviewsRecyclerList = (RecyclerView) findViewById(R.id.reviews_recycler_list);
        reviewsRecyclerList.setLayoutManager(new LinearLayoutManager(this));

        menuRecyclerList = (RecyclerView) findViewById(R.id.breakfast_menu_recycler);
        menuRecyclerList.setLayoutManager(new LinearLayoutManager(this));
        menu2Recycler = (RecyclerView) findViewById(R.id.lunch_menu_recycler);
        menu2Recycler.setLayoutManager(new LinearLayoutManager(this));


        mstore=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        RESTO_NAME = findViewById(R.id.restoName);
        restoRating = findViewById(R.id.restaurant_rating_number);
        book_bt = findViewById(R.id.Book_now);


        Resto_Type = findViewById(R.id.resto_type);
        Restaurant_image = findViewById(R.id.res_background_image);
        Resto_location = findViewById(R.id.resto_location);

        currentUid= mAuth.getCurrentUser().getUid();
        proUid=getIntent().getStringExtra("uid");
        contactsRef = db.collection("/restaurants/"+proUid+"/reviews");
    timeRef = db.collection("/restaurants/"+proUid+"/time_slots");
        menuRef = db.collection("/restaurants/"+proUid+"/menu");
        menuQuery = menuRef;

        Spinner spinner2 = (Spinner) findViewById(R.id.type_spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.dish_type, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                types= (String) adapterView.getItemAtPosition(i);
                menu2Query = menuRef.whereEqualTo("type",types);
                setUpMenu2RecyclerView(menu2Query);
                menu2Adapter.startListening();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        menu2Query = menuRef.whereEqualTo("type",types);
        setUpMenu2RecyclerView(menu2Query);








        setUpRecyclerView();
        Resto_location.setText(proRestoAddr);
        Glide.with(getApplicationContext()).load(proRestoImage).into(Restaurant_image);
        ratingBar = findViewById(R.id.rating_bar);
        setUpMenuRecyclerView(menuQuery);



        book_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {








                dialog1 = new BottomSheetDialog(RestaurantsDetails.this,R.style.book_now_pop);
                dialog1.setContentView(R.layout.book_now);
                book_resto_location = dialog1.findViewById(R.id.resto_location);
                resto_book_image = dialog1.findViewById(R.id.menu_image);
                book_resto_title = dialog1.findViewById(R.id.restaurant_title);
                increse_bt=dialog1.findViewById(R.id.increase);
                deacrease_bt = dialog1.findViewById(R.id.decrease);
                Book = dialog1.findViewById(R.id.Book);

                book_resto_title.setText(profileRestoName);
                book_resto_location.setText(proRestoAddr);

                DatePickerTimeline datePickerTimeline = dialog1.findViewById(R.id.datePickerTimeline);
                Date today = new Date(); // Fri Jun 17 14:54:28 PDT 2016

                Calendar cal = Calendar.getInstance();
                cal.setTime(today); // don't forget this if date is arbitrary e.g. 01-01-2014

                int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK); // 6
                int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH); // 17
                int dayOfYear = cal.get(Calendar.DAY_OF_YEAR); //169

                final int month = cal.get(Calendar.MONTH); // 5
                int year = cal.get(Calendar.YEAR); // 2016
                int months = month +1;


                book_date = dayOfMonth+"/"+months+"/"+year;
                book_date2 = dayOfMonth+"-"+months+"-"+year;

                Tquery = timeRef.whereEqualTo("date",book_date);
                FirestoreRecyclerOptions<ModelTimeSlot> options = new FirestoreRecyclerOptions.Builder<ModelTimeSlot>()
                        .setQuery(Tquery, ModelTimeSlot.class)
                        .build();

                Tadapter = new TimeSlotAdapter(options);

                RecyclerView recyclerView = dialog1.findViewById(R.id.time_slot_recycler);

                recyclerView.setLayoutManager(new LinearLayoutManager(RestaurantsDetails.this,LinearLayoutManager.HORIZONTAL,false));
                recyclerView.setAdapter(Tadapter);

                Tadapter.startListening();


                Tquery = timeRef.whereEqualTo("date",book_date).orderBy("time_slot");
                FirestoreRecyclerOptions<ModelTimeSlot> optionsss = new FirestoreRecyclerOptions.Builder<ModelTimeSlot>()
                        .setQuery(Tquery, ModelTimeSlot.class)
                        .build();

                Tadapter = new TimeSlotAdapter(optionsss);

                RecyclerView recyclerView2 = dialog1.findViewById(R.id.time_slot_recycler);

                recyclerView2.setLayoutManager(new LinearLayoutManager(RestaurantsDetails.this,LinearLayoutManager.HORIZONTAL,false));
                recyclerView2.setAdapter(Tadapter);

                Tadapter.startListening();

                datePickerTimeline.setInitialDate(year, month, dayOfMonth);
                datePickerTimeline.setActiveDate(cal);
                // Set a date Selected Listener
                datePickerTimeline.setOnDateSelectedListener(new OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(int year, int month, int day, int dayOfWeek) {
                        month=month+1;


                        book_date = day+"/"+month+"/"+year;
                        book_date2 = day+"-"+month+"-"+year;



                        Tquery = timeRef.whereEqualTo("date",book_date);

                        FirestoreRecyclerOptions<ModelTimeSlot> options = new FirestoreRecyclerOptions.Builder<ModelTimeSlot>()
                                .setQuery(Tquery, ModelTimeSlot.class)
                                .build();

                        Tadapter = new TimeSlotAdapter(options);

                        RecyclerView recyclerView = dialog1.findViewById(R.id.time_slot_recycler);

                        recyclerView.setLayoutManager(new LinearLayoutManager(RestaurantsDetails.this,LinearLayoutManager.HORIZONTAL,false));
                        recyclerView.setAdapter(Tadapter);
                        Tadapter.startListening();



                        Tquery = timeRef.whereEqualTo("date",book_date).orderBy("time_slot");

                        FirestoreRecyclerOptions<ModelTimeSlot> optionss = new FirestoreRecyclerOptions.Builder<ModelTimeSlot>()
                                .setQuery(Tquery, ModelTimeSlot.class)
                                .build();

                        Tadapter = new TimeSlotAdapter(optionss);

                        RecyclerView recyclerView1 = dialog1.findViewById(R.id.time_slot_recycler);

                        recyclerView1.setLayoutManager(new LinearLayoutManager(RestaurantsDetails.this,LinearLayoutManager.HORIZONTAL,false));
                        recyclerView1.setAdapter(Tadapter);
                        Tadapter.startListening();
                        Tadapter.setOnItemClickListener(new TimeSlotAdapter.OnItemClickListener() {
                            @Override
                            public void OnItemClick(DocumentSnapshot documentSnapshot, int position) {


                                String[] pathwithuid;
                                String path =documentSnapshot.getReference().getPath();
                                //Toast.makeText(FindFriendsActivity.this,"Position"+position+"\t UID:"+id,Toast.LENGTH_LONG).show();


                                pathwithuid = path.split("/");
                                String uid2=pathwithuid[3];

                                DocumentReference documentReferences = mstore.collection("/restaurants/"+proUid+"/time_slots").document(uid2);
                                documentReferences.addSnapshotListener(RestaurantsDetails.this, new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {


                                        //Picasso.get().load(documentSnapshot.getString("image")).into(circleImageView);


                                        time_slot = documentSnapshot.getString("time_slot");
                                        Toast.makeText(RestaurantsDetails.this, time_slot, Toast.LENGTH_SHORT).show();









                                    }
                                });







                            }
                        });


                    }

                    @Override
                    public void onDisabledDateSelected(int year, int month, int day, int dayOfWeek, boolean isDisabled) {
                        // Do Something
                    }
                });

                 // Disable date
                //Date[] dates = {Calendar.getInstance().getTime()};
                //datePickerTimeline.deactivateDates(dates);
                datePickerTimeline.setDateTextColor(Color.BLACK);

                datePickerTimeline.setDayTextColor(Color.parseColor("#055AC7"));
                datePickerTimeline.setMonthTextColor(Color.parseColor("#055AC7"));


                Tadapter.setOnItemClickListener(new TimeSlotAdapter.OnItemClickListener() {
                    @Override
                    public void OnItemClick(DocumentSnapshot documentSnapshot, int position) {


                        String[] pathwithuid;
                        String path =documentSnapshot.getReference().getPath();
                        //Toast.makeText(FindFriendsActivity.this,"Position"+position+"\t UID:"+id,Toast.LENGTH_LONG).show();


                        pathwithuid = path.split("/");
                        String uid2=pathwithuid[3];

                        DocumentReference documentReferences = mstore.collection("/restaurants/"+proUid+"/time_slots").document(uid2);
                        documentReferences.addSnapshotListener(RestaurantsDetails.this, new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {


                                //Picasso.get().load(documentSnapshot.getString("image")).into(circleImageView);


                                time_slot = documentSnapshot.getString("time_slot");










                            }
                        });







                    }
                });





                Book.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView increaseInteger = (TextView) dialog1.findViewById(
                                R.id.integer_number);
                        String no_of_people = increaseInteger.getText().toString();


                        if (no_of_people.equals("0")){



                            Toast.makeText(RestaurantsDetails.this,"Please select Number of people!!", Toast.LENGTH_SHORT).show();

                        }
                        else if ( time_slot == null ){


                            Toast.makeText(RestaurantsDetails.this, "Please select a Time Slot!!", Toast.LENGTH_SHORT).show();

                        }
                        else{


                            Intent intent = new Intent(RestaurantsDetails.this,BookingActivity.class);
                            intent.putExtra("bookDate",book_date2);
                            intent.putExtra("timeSlot",time_slot);
                            intent.putExtra("no_of_people",no_of_people);
                            intent.putExtra("restoUid",proUid);
                            startActivity(intent);

                        }


                    }
                });
                Glide.with(getApplicationContext()).load(proRestoImage).into(resto_book_image);
                dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog1.show();




            }
        });


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
            public void onRatingChanged(final RatingBar ratingBar, final float rating, boolean fromUser) {
                Toast.makeText(RestaurantsDetails.this, ""+rating, Toast.LENGTH_SHORT).show();



                dialog = new Dialog(RestaurantsDetails.this,R.style.book_now_pop);
                dialog.setContentView(R.layout.activity_pop_up_rating);


                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                ratingBar2 = dialog.findViewById(R.id.rating_bar2);
                ratingBar2.setRating(ratingBar.getRating());
                dialog.show();
                dialog.setCanceledOnTouchOutside(true);

                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(final DialogInterface dialogs) {


                        DocumentReference reviewReference = mstore.collection("restaurants").document(proUid).collection("reviews").document(currentUid);
                        reviewReference.addSnapshotListener(RestaurantsDetails.this, new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {


                                //Picasso.get().load(documentSnapshot.getString("image")).into(circleImageView);
                                if (documentSnapshot.exists()) {


                                    String ratings = documentSnapshot.getString("rating");

                                    ratingBar.setRating(Float.parseFloat(ratings));
                                    dialog.dismiss();

                                }








                            }
                        });

                    }
                });



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

    private void setUpMenu2RecyclerView(Query query) {











        FirestoreRecyclerOptions<ModelDisplayMenu> options = new FirestoreRecyclerOptions.Builder<ModelDisplayMenu>()
                .setQuery(query, ModelDisplayMenu.class)
                .build();

        menu2Adapter = new DisplayMenuAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.lunch_menu_recycler);



        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(menu2Adapter);
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);
    }







    private void setUpMenuRecyclerView(Query query) {











        FirestoreRecyclerOptions<ModelDisplayMenu> options = new FirestoreRecyclerOptions.Builder<ModelDisplayMenu>()
                .setQuery(query, ModelDisplayMenu.class)
                .build();

        menuAdapter = new DisplayMenuAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.breakfast_menu_recycler);



        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(menuAdapter);
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);
    }



    private void setUpRecyclerView() {



        query = contactsRef.orderBy("name");







        FirestoreRecyclerOptions<ModelReviews> options = new FirestoreRecyclerOptions.Builder<ModelReviews>()
                .setQuery(query, ModelReviews.class)
                .build();

        adapter = new ReviewsAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.reviews_recycler_list);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);
    }

    @Override
    protected void onStart() {
        super.onStart();


        proUid=getIntent().getStringExtra("uid");




        adapter.startListening();
        menuAdapter.startListening();
menu2Adapter.startListening();


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
        currentUid=mAuth.getCurrentUser().getUid();




        DocumentReference reviewReference = mstore.collection("restaurants").document(proUid).collection("reviews").document(currentUid);
        reviewReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {


                //Picasso.get().load(documentSnapshot.getString("image")).into(circleImageView);
                if (documentSnapshot.exists()) {


                    String ratings = documentSnapshot.getString("rating");

                    ratingBar.setRating(Float.parseFloat(ratings));
                    dialog.dismiss();
                }








            }
        });


    }
    public void increaseInteger(View view) {
        minteger = minteger + 1;
        if (minteger>=10){
            display(minteger);
            increse_bt.setEnabled(false);
            deacrease_bt.setEnabled(true);
        }
        else if (minteger<10){
            display(minteger);

            deacrease_bt.setEnabled(true);

        }



    }public void decreaseInteger(View view) {
        minteger = minteger - 1;
        if (minteger<=0){
            display(minteger);
            deacrease_bt.setEnabled(false);
            increse_bt.setEnabled(true);
        }
        else if(minteger>0){
            display(minteger);

            increse_bt.setEnabled(true);
        }
    }

    private void display(int number) {
        TextView displayInteger = (TextView) dialog1.findViewById(
                R.id.integer_number);
        displayInteger.setText("" + number);
    }

}
