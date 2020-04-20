package com.example.isitvacant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FoodOdering extends AppCompatActivity {
    Dialog dialog1;
    CardView Yes,No;

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
    FirebaseFirestore mstore ;
    String uid,invoiceID;
    FirebaseAuth mAuth;
    TextView noOfFoodItems;
     CartAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_odering);


        mViewPager = findViewById(R.id.main_tabs_pager);
        myFoodAccessAdapter = new FoodAccessAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(myFoodAccessAdapter);
        mTabLayout = findViewById(R.id.main_tab);
        mTabLayout.setupWithViewPager(mViewPager);
        noOfFoodItems = findViewById(R.id.no_of_food_items);
        mAuth = FirebaseAuth.getInstance();
        uid  = mAuth.getCurrentUser().getUid();
        invoiceID = getIntent().getStringExtra("invoiceID");
        mstore = FirebaseFirestore.getInstance();
        mstore.collection("/users/"+uid+"/current_reservations/"+invoiceID+"/cart")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {


                        List<Double> allItems = new ArrayList<>();
                        double sum =0;
                        for (QueryDocumentSnapshot doc : value) {
                            if (doc.get("foodName") != null) {
                                allItems.add(doc.getDouble("quantity"));
                                //Toast.makeText(GroupProfileActivity.this, "Messages is: "+value,Toast.LENGTH_LONG).show();
                            }
                        }
                        //Toast.makeText(GroupChatActivity.this, "Messages are: "+allMssages,Toast.LENGTH_LONG).show();



                        for(int i=0; i<allItems.size(); i++)

                        {


                            sum = sum+allItems.get(i);




                        }
                        int sumInt = (int)sum;
                        noOfFoodItems.setText(String.valueOf(sumInt));
                    }
                });

        cart = findViewById(R.id.cart);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


FirebaseFirestore db = FirebaseFirestore.getInstance();
Query query;

                dialog = new BottomSheetDialog(FoodOdering.this);
                dialog.setContentView(R.layout.view_cart);
                uid  = mAuth.getCurrentUser().getUid();
                invoiceID = getIntent().getStringExtra("invoiceID");
                CollectionReference menuRef = db.collection("/users/"+uid+"/current_reservations/"+invoiceID+"/cart");
                query = menuRef;
                FirestoreRecyclerOptions<ModelMenuInfo> options = new FirestoreRecyclerOptions.Builder<ModelMenuInfo>()
                        .setQuery(query, ModelMenuInfo.class)
                        .build();

                adapter = new CartAdapter(options);

                RecyclerView recyclerView = dialog.findViewById(R.id.cart_recycler);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(FoodOdering.this));
                recyclerView.setAdapter(adapter);
                adapter.startListening();
                Reserve = dialog.findViewById(R.id.Reserve_food);
                Reserve.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(getApplicationContext(),booking_summary.class);
                        intent.putExtra("restoUid",getIntent().getStringExtra("restoUid"));
                        ArrayList<String> tableID = getIntent().getStringArrayListExtra("tableIDlist");
                        Toast.makeText(FoodOdering.this, getIntent().getStringArrayListExtra("tableIDlist").toString() , Toast.LENGTH_SHORT).show();


                        intent.putExtra("invoiceID",getIntent().getStringExtra("invoiceID"));
                        intent.putExtra("tableIDlist",tableID);
                        intent.putExtra("bookDate",getIntent().getStringExtra("bookDate"));
                        intent.putExtra("timeSlot",getIntent().getStringExtra("timeSlot"));
                        intent.putExtra("no_of_people",getIntent().getStringExtra("no_of_people"));
                        intent.putExtra("flag","yes");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });




    }

    @Override
    public void onBackPressed() {

        dialog1 = new Dialog(FoodOdering.this,R.style.book_now_pop);
        WindowManager.LayoutParams wmlp = dialog1.getWindow().getAttributes();
        wmlp.gravity = Gravity.TOP;
        wmlp.y=110;

        dialog1.setContentView(R.layout.cancel_resevation);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        dialog1.setCanceledOnTouchOutside(true);
        Yes = dialog1.findViewById(R.id.Yes1);
        No = dialog1.findViewById(R.id.No1);

        Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDestroy();
            }
        });
        No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
        dialog1.show();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();


        final String restoID = getIntent().getStringExtra("restoUid");
        String invoiceId = getIntent().getStringExtra("invoiceID");



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
                                String invoiceId = getIntent().getStringExtra("invoiceID");

                                mstore.collection("users")
                                        .document(uid2).collection("current_reservations").document(invoiceId).collection("cart").document(foodName)
                                        .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {


                                    }


                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        String error = e.getMessage();
                                        Toast.makeText(FoodOdering.this, "Error" + error, Toast.LENGTH_LONG).show();
                                    }
                                });





                                mstore.collection("restaurants")
                                        .document(restoID).collection("current_reservations").document(invoiceId).collection("cart").document(foodName)
                                        .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {


                                    }


                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        String error = e.getMessage();
                                        Toast.makeText(FoodOdering.this, "Error" + error, Toast.LENGTH_LONG).show();
                                    }
                                });

                                mstore.collection("restaurants")
                                        .document(restoID).collection("current_reservations").document(invoiceId)
                                        .delete().addOnSuccessListener(new OnSuccessListener<Void>() {

                                    @Override
                                    public void onSuccess(Void aVoid) {


                                    }


                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        String error = e.getMessage();
                                        Toast.makeText(FoodOdering.this, "Error" + error, Toast.LENGTH_LONG).show();
                                    }
                                });
                                mstore.collection("users")
                                        .document(uid2).collection("current_reservations").document(invoiceId)
                                        .delete().addOnSuccessListener(new OnSuccessListener<Void>() {

                                    @Override
                                    public void onSuccess(Void aVoid) {



                                    }


                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        String error = e.getMessage();
                                        Toast.makeText(FoodOdering.this, "Error" + error, Toast.LENGTH_LONG).show();
                                    }
                                });











                            }





                        }



                    }
                });




    }
}




