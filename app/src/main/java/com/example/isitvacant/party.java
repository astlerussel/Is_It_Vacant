package com.example.isitvacant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class party extends AppCompatActivity {
    private RecyclerView partyRecyclerList;
    EditText searchText;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference contactsRef = db.collection("restaurants");
    private RestaurantsAdapter adapter;
    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party);
        partyRecyclerList = (RecyclerView) findViewById(R.id.party_recycler);
        partyRecyclerList.setLayoutManager(new LinearLayoutManager(this));
        searchText= findViewById(R.id.search);
        query = contactsRef.orderBy("name").whereEqualTo("category","party");
        setUpRecyclerView(query);

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                query = contactsRef.orderBy("name").whereEqualTo("category","party");
                setUpRecyclerView(query);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()==0){
                    query = contactsRef.orderBy("name").whereEqualTo("category","party");
                    setUpRecyclerView(query);
                    adapter.setOnItemClickListener(new RestaurantsAdapter.OnItemClickListener() {
                        @Override
                        public void OnItemClick(DocumentSnapshot documentSnapshot, int position) {
                            String[] pathwithuid;
                            String path =documentSnapshot.getReference().getPath();
                            //Toast.makeText(FindFriendsActivity.this,"Position"+position+"\t UID:"+id,Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(party.this,RestaurantsDetails.class);

                            pathwithuid = path.split("/");
                            String uid2=pathwithuid[1];
                            intent.putExtra("uid",uid2);


                            startActivity(intent);

                        }
                    });
                    adapter.startListening();

                }
                else {
                    query = contactsRef.orderBy("name").whereEqualTo("category","party").startAt(s.toString()).endAt(s.toString() + "\uf8ff");
                    setUpRecyclerView(query);
                    adapter.setOnItemClickListener(new RestaurantsAdapter.OnItemClickListener() {
                        @Override
                        public void OnItemClick(DocumentSnapshot documentSnapshot, int position) {
                            String[] pathwithuid;
                            String path =documentSnapshot.getReference().getPath();
                            //Toast.makeText(FindFriendsActivity.this,"Position"+position+"\t UID:"+id,Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(party.this,RestaurantsDetails.class);

                            pathwithuid = path.split("/");
                            String uid2=pathwithuid[1];
                            intent.putExtra("uid",uid2);


                            startActivity(intent);

                        }
                    });
                    adapter.startListening();
                }





            }

            @Override
            public void afterTextChanged(Editable s) {








            }
        });
        adapter.setOnItemClickListener(new RestaurantsAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(DocumentSnapshot documentSnapshot, int position) {
                String[] pathwithuid;
                String path =documentSnapshot.getReference().getPath();
                //Toast.makeText(FindFriendsActivity.this,"Position"+position+"\t UID:"+id,Toast.LENGTH_LONG).show();
                Intent intent = new Intent(party.this,RestaurantsDetails.class);

                pathwithuid = path.split("/");
                String uid2=pathwithuid[1];
                intent.putExtra("uid",uid2);


                startActivity(intent);

            }
        });
    }

    private void setUpRecyclerView(Query query) {







        FirestoreRecyclerOptions<RestaurantsModel> options = new FirestoreRecyclerOptions.Builder<RestaurantsModel>()
                .setQuery(query, RestaurantsModel.class)
                .build();

        adapter = new RestaurantsAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.party_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void onStart() {
        super.onStart();



        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            adapter.startListening();

        }

    }
}
