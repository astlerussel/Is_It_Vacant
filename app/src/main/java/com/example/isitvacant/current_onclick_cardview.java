package com.example.isitvacant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.CollapsingToolbarLayout;
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

import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.graphics.Color.RED;

public class current_onclick_cardview extends AppCompatActivity {
    CollapsingToolbarLayout collapsingToolbarLayout;
    private RecyclerView menuRecyclerList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference menuRef ;
    private MenuInfoAdapter adapter;
    Button checkout;
    FirebaseAuth mAuth;
    String uid;
    FirebaseFirestore mstore;
    String username,hotelName,guests,timeSlot,tableNo,bookDate,location;
    TextView hotel_name,reservation_date,reservation_time,table_id,numberOfGuests,Location;
     String restoID;
    String name,foodImage,type;
    double price,quantity;
    double totalPrice;
    TextView grandTotal;
    TextView mobile_no,Username;
    Query query;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_onclick_cardview);
        collapsingToolbarLayout = findViewById(R.id.collapsingtoolbar2);
        collapsingToolbarLayout.setExpandedTitleColor(Color.parseColor("#099ae1"));
        collapsingToolbarLayout.setExpandedTitleGravity(Gravity.BOTTOM);
        collapsingToolbarLayout.setExpandedTitleTypeface(Typeface.DEFAULT_BOLD);
        checkout = findViewById(R.id.checkout);
        mAuth = FirebaseAuth.getInstance();
        mstore = FirebaseFirestore.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        Username = findViewById(R.id.Username2);
        mobile_no = findViewById(R.id.Mobile_No2);
        hotel_name = findViewById(R.id.hotel_name2);
        reservation_date = findViewById(R.id.Reservation_date2);
        reservation_time = findViewById(R.id.Reservation_time2);
        numberOfGuests = findViewById(R.id.no_of_guests2);
        Location = findViewById(R.id.Location2);
        table_id = findViewById(R.id.Table_no2);
        RelativeLayout layout  = findViewById(R.id.menu_info_t);

        layout.setVisibility(View.VISIBLE);
        menuRecyclerList = (RecyclerView) findViewById(R.id.menu_info_recycler);
        menuRecyclerList.setLayoutManager(new LinearLayoutManager(this));
        String invoiceId =getIntent().getStringExtra("uid");
        String reserve = getIntent().getStringExtra("reserve");
        if(reserve.equals("current_reservations")){
            checkout.setVisibility(View.VISIBLE);
        }
        else{
            checkout.setVisibility(View.INVISIBLE);
        }
        menuRef = db.collection("/users/"+uid+"/"+reserve+"/"+invoiceId+"/cart");
        query = menuRef.orderBy("foodName");
        setUpRecyclerView(query);
        grandTotal = findViewById(R.id.total_no_of_items);

        mstore.collection("/users/"+uid+"/"+reserve+"/"+invoiceId+"/cart")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {


                        List<Double> allPrice = new ArrayList<>();
                        double sum =0;
                        for (QueryDocumentSnapshot doc : value) {
                            if (doc.get("foodName") != null) {
                                allPrice.add(doc.getDouble("totalPrice"));

                            }
                        }




                        for(int i=0; i<allPrice.size(); i++)

                        {


                            sum = sum+allPrice.get(i);




                        }
                        grandTotal.setText(String.valueOf(sum));
                    }
                });


        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String invoiceId =getIntent().getStringExtra("uid");

                DocumentReference documentReferences2 = mstore.collection("users").document(uid).collection("current_reservations").document(invoiceId);
                documentReferences2.addSnapshotListener(current_onclick_cardview.this, new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if(documentSnapshot.exists()){
                        String invoiceId = getIntent().getStringExtra("uid");

                        username = documentSnapshot.getString("userName");
                        hotelName = documentSnapshot.getString("hotelName");
                        guests = documentSnapshot.getString("guests");
                        timeSlot = documentSnapshot.getString("timeSlot");
                        tableNo = documentSnapshot.getString("tableId");
                        bookDate = documentSnapshot.getString("date");
                        location = documentSnapshot.getString("location");
                        restoID = documentSnapshot.getString("restoID");
                        totalPrice = documentSnapshot.getDouble("totalSeatPrice");


                        Map<String, Object> userMap = new HashMap<>();

                        userMap.put("userName", username);
                        userMap.put("date", bookDate);
                        userMap.put("timeSlot", timeSlot);
                        userMap.put("guests", guests);
                        userMap.put("tableId", tableNo);
                        userMap.put("location", location);
                        userMap.put("totalSeatPrice", (int) totalPrice);
                        userMap.put("hotelName", hotelName);
                        userMap.put("restoID", restoID);


                        mstore.collection("users")
                                .document(uid).collection("previous_reservations").document(invoiceId)
                                .set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //Toast.makeText(current_onclick_cardview.this, "Seat Reservation Successful", Toast.LENGTH_LONG).show();

                            }


                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                String error = e.getMessage();
                                Toast.makeText(current_onclick_cardview.this, "Error" + error, Toast.LENGTH_LONG).show();
                            }
                        });
                        mstore.collection("restaurants")
                                .document(restoID).collection("previous_reservations").document(invoiceId)
                                .set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {


                            }


                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                String error = e.getMessage();
                                Toast.makeText(current_onclick_cardview.this, "Error" + error, Toast.LENGTH_LONG).show();
                            }
                        });


                        mstore.collection("/users/" + uid + "/current_reservations/" + invoiceId + "/cart")
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot value,
                                                        @Nullable FirebaseFirestoreException e) {


                                        final List<String> cartUid = new ArrayList<>();

                                        for (QueryDocumentSnapshot doc : value) {
                                            if (doc.get("foodName") != null) {
                                                cartUid.add(doc.getString("foodName"));
                                                //Toast.makeText(GroupProfileActivity.this, "Messages is: "+value,Toast.LENGTH_LONG).show();
                                            }
                                        }
                                        //Toast.makeText(GroupChatActivity.this, "Messages are: "+allMssages,Toast.LENGTH_LONG).show();


                                        if (cartUid.size() != 0) {

                                            for (int i = 0; i < cartUid.size(); i++) {

                                                final String foodName = cartUid.get(i);

                                                final String uid2 = FirebaseAuth.getInstance().getUid();
                                                String invoiceId = getIntent().getStringExtra("uid");
                                                DocumentReference documentReferences2 = mstore.collection("users").document(uid2).collection("current_reservations").document(invoiceId).collection("cart").document(foodName);
                                                final int finalI = i;
                                                documentReferences2.addSnapshotListener(current_onclick_cardview.this, new EventListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                                                        if (documentSnapshot.exists()) {
                                                            name = documentSnapshot.getString("foodName");
                                                            foodImage = documentSnapshot.getString("image");
                                                            quantity = documentSnapshot.getDouble("quantity");
                                                            price = documentSnapshot.getDouble("totalPrice");
                                                            type = documentSnapshot.getString("type");
                                                            //Toast.makeText(current_onclick_cardview.this, documentSnapshot.getString("foodName")+"  "+foodImage+"  "+quantity+"  "+price+"   "+type,Toast.LENGTH_LONG).show();

                                                            Map<String, Object> userMap = new HashMap<>();

                                                            userMap.put("foodName", name);
                                                            userMap.put("image", foodImage);
                                                            userMap.put("totalPrice", (int) price);
                                                            userMap.put("quantity", (int) quantity);
                                                            userMap.put("type", type);


                                                            String invoiceId = getIntent().getStringExtra("uid");

                                                            mstore.collection("users")
                                                                    .document(uid2).collection("previous_reservations").document(invoiceId).collection("cart").document(name)
                                                                    .set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {


                                                                }


                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    String error = e.getMessage();
                                                                    Toast.makeText(current_onclick_cardview.this, "Error" + error, Toast.LENGTH_LONG).show();
                                                                }
                                                            });
                                                            mstore.collection("restaurants")
                                                                    .document(restoID).collection("previous_reservations").document(invoiceId).collection("cart").document(foodName)
                                                                    .set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {


                                                                }


                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    String error = e.getMessage();
                                                                    Toast.makeText(current_onclick_cardview.this, "Error" + error, Toast.LENGTH_LONG).show();
                                                                }
                                                            });

                                                        }


                                                    }
                                                });


                                            }
                                            deleteDocs(cartUid);


                                        }


                                    }
                                });


                        mstore.collection("restaurants")
                                .document(documentSnapshot.getString("restoID")).collection("current_reservations").document(invoiceId)
                                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {

                            @Override
                            public void onSuccess(Void aVoid) {


                            }


                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                String error = e.getMessage();
                                Toast.makeText(current_onclick_cardview.this, "Error" + error, Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                    }
                });










            }


        });


        collapsingToolbarLayout.setTitle("Chowks Food Court");

        collapsingToolbarLayout.setExpandedTitleTypeface(Typeface.DEFAULT_BOLD);
    }

    private void setUpRecyclerView(Query query) {







        FirestoreRecyclerOptions<ModelMenuInfo> options = new FirestoreRecyclerOptions.Builder<ModelMenuInfo>()
                .setQuery(query, ModelMenuInfo.class)
                .build();

        adapter = new MenuInfoAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.menu_info_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }
    public void deleteDocs(final List<String> cartUid){
        for(int i = 0 ;i<cartUid.size();i++){
            String invoiceId =getIntent().getStringExtra("uid");
            String uid3 = FirebaseAuth.getInstance().getUid();
            final int finalI = i;
            mstore.collection("users")
                    .document(uid3).collection("current_reservations").document(invoiceId).collection("cart").document(cartUid.get(i))
                    .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {


                }


            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String error = e.getMessage();
                    Toast.makeText(current_onclick_cardview.this, "Error" + error, Toast.LENGTH_LONG).show();
                }
            });

            if(i ==cartUid.size()-1){
                Intent intent = new Intent(current_onclick_cardview.this,MainActivity.class);
                startActivity(intent);

                mstore.collection("users")
                        .document(uid3).collection("current_reservations").document(invoiceId)
                        .delete().addOnSuccessListener(new OnSuccessListener<Void>() {

                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(current_onclick_cardview.this, "Successfully Checked Out!!", Toast.LENGTH_LONG).show();


                    }


                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String error = e.getMessage();
                        Toast.makeText(current_onclick_cardview.this, "Error" + error, Toast.LENGTH_LONG).show();
                    }
                });







            }

        }

    }



    @Override
    protected void onStart() {
        super.onStart();


            adapter.startListening();

        String invoiceId =getIntent().getStringExtra("uid");
        String reserve = getIntent().getStringExtra("reserve");

        uid  = mAuth.getCurrentUser().getUid();
        mstore = FirebaseFirestore.getInstance();
        DocumentReference documentReferences3 = mstore.collection("users").document(uid);
        documentReferences3.addSnapshotListener(current_onclick_cardview.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if (documentSnapshot.exists()){

                    mobile_no.setText(documentSnapshot.getString("Mobile"));

                }








            }
        });

        DocumentReference documentReferences2 = mstore.collection("users").document(uid).collection(reserve).document(invoiceId);
        documentReferences2.addSnapshotListener(current_onclick_cardview.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if (documentSnapshot.exists()){


                    hotel_name.setText(documentSnapshot.getString("hotelName"));
                    reservation_date.setText(documentSnapshot.getString("date"));
                    reservation_time.setText(documentSnapshot.getString("timeSlot"));
                    table_id.setText(documentSnapshot.getString("tableId"));
                    numberOfGuests.setText(documentSnapshot.getString("guests"));
                    Location.setText(documentSnapshot.getString("location"));
                    Username.setText(documentSnapshot.getString("userName"));
                }








            }
        });
    }


}
