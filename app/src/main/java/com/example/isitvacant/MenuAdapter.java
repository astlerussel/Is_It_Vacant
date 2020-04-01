package com.example.isitvacant;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
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

public class MenuAdapter extends FirestoreRecyclerAdapter<ModelMenu, MenuAdapter.MenuHolder> {

    private final String invoiceID;
    private final String restoID;
    private OnItemClickListener listener;
    String image;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MenuAdapter(@NonNull FirestoreRecyclerOptions<ModelMenu> options,String invoiceID,String restoID) {
        super(options);
        this.invoiceID =invoiceID;
        this.restoID = restoID;
    }

    @Override
    protected void onBindViewHolder(@NonNull MenuHolder holder, int position, @NonNull ModelMenu model) {

        holder.cost.setText(model.getCost());
        holder.Name.setText(model.getName());
        holder.type.setText(model.getType());
        image = model.getImage();





        Picasso.get().load(model.getImage()).into(holder.menuImage);



    }

    @NonNull
    @Override
    public MenuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_items, parent, false);
        return new MenuHolder(view);
    }

    class MenuHolder extends RecyclerView.ViewHolder{

        ElegantNumberButton elegantNumberButton;
       Button Add;

        TextView Name,cost,type;

        ImageView menuImage;
        FirebaseAuth mauth = FirebaseAuth.getInstance();
        FirebaseFirestore mstore = FirebaseFirestore.getInstance();

        public MenuHolder(@NonNull final View itemView) {
            super(itemView);
            Name=itemView.findViewById(R.id.food_title);
            cost=itemView.findViewById(R.id.food_costs);
            type= itemView.findViewById(R.id.food_type);
            menuImage = itemView.findViewById(R.id.food_image);


            elegantNumberButton = itemView.findViewById(R.id.elegantNumber);
            Add = itemView.findViewById(R.id.add_food);

            Add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Add.setVisibility(View.INVISIBLE);
                    elegantNumberButton.setVisibility(View.VISIBLE);

                    elegantNumberButton.setNumber("1");
                    ModelMenu tempMenu = new ModelMenu();

                    String name = Name.getText().toString();
                    String types = type.getText().toString();
                    int price = Integer.parseInt(cost.getText().toString());
                    String uid2 = mauth.getCurrentUser().getUid();
                    int newValue = 1;




                    String uid = mauth.getCurrentUser().getUid();

                    int totalPrice = price*newValue;

                    Map<String, Object> userMap = new HashMap<>();

                    userMap.put("foodName", name);
                    userMap.put("image", image);
                    userMap.put("totalPrice", totalPrice);
                    userMap.put("quantity", newValue);
                    userMap.put("type", types);

                    Toast.makeText(v.getContext(), name+"  "+image, Toast.LENGTH_SHORT).show();






                    mstore.collection("users")
                            .document(uid).collection("current_reservations").document(invoiceID).collection("cart").document(name)
                            .set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(itemView.getContext(), "Item Added", Toast.LENGTH_LONG).show();

                        }


                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String error = e.getMessage();
                            Toast.makeText(itemView.getContext(), "Error" + error, Toast.LENGTH_LONG).show();
                        }
                    });
                    mstore.collection("restaurants")
                            .document(restoID).collection("current_reservations").document(invoiceID).collection("cart").document(name)
                            .set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {


                        }


                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String error = e.getMessage();
                            Toast.makeText(itemView.getContext(), "Error" + error, Toast.LENGTH_LONG).show();
                        }
                    });

                    elegantNumberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                        @Override
                        public void onValueChange(ElegantNumberButton view, int oldValue, final int newValue) {
                            if(newValue == 0)
                            {
                                String name = Name.getText().toString();
                                String uid2 = mauth.getCurrentUser().getUid();
                                Add.setVisibility(View.VISIBLE);
                                elegantNumberButton.setVisibility(View.INVISIBLE);
                                mstore.collection("users")
                                        .document(uid2).collection("current_reservations").document(invoiceID).collection("cart").document(name)
                                        .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(itemView.getContext(), "Item Cleared", Toast.LENGTH_LONG).show();

                                    }


                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        String error = e.getMessage();
                                        Toast.makeText(itemView.getContext(), "Error" + error, Toast.LENGTH_LONG).show();
                                    }
                                });
                                mstore.collection("restaurants")
                                        .document(restoID).collection("current_reservations").document(invoiceID).collection("cart").document(name)
                                        .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //Toast.makeText(itemView.getContext(), "Item Cleared", Toast.LENGTH_LONG).show();

                                    }


                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        String error = e.getMessage();
                                        Toast.makeText(itemView.getContext(), "Error" + error, Toast.LENGTH_LONG).show();
                                    }
                                });
                            }

                            else{

                                ModelMenu tempMenu = new ModelMenu();

                                 String name = Name.getText().toString();
                                 String types = type.getText().toString();
                                 int price = Integer.parseInt(cost.getText().toString());
                                String uid2 = mauth.getCurrentUser().getUid();




                                String uid = mauth.getCurrentUser().getUid();

                                int totalPrice = price*newValue;

                                Map<String, Object> userMap = new HashMap<>();

                                userMap.put("foodName", name);
                                userMap.put("image", image);
                                userMap.put("totalPrice", totalPrice);
                                userMap.put("quantity", newValue);
                                userMap.put("type", types);

                                Toast.makeText(view.getContext(), name+"  "+image, Toast.LENGTH_SHORT).show();






                                mstore.collection("users")
                                        .document(uid).collection("current_reservations").document(invoiceID).collection("cart").document(name)
                                        .set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(itemView.getContext(), "Item Added", Toast.LENGTH_LONG).show();

                                    }


                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        String error = e.getMessage();
                                        Toast.makeText(itemView.getContext(), "Error" + error, Toast.LENGTH_LONG).show();
                                    }
                                });
                                mstore.collection("restaurants")
                                        .document(restoID).collection("current_reservations").document(invoiceID).collection("cart").document(name)
                                        .set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {


                                    }


                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        String error = e.getMessage();
                                        Toast.makeText(itemView.getContext(), "Error" + error, Toast.LENGTH_LONG).show();
                                    }
                                });





                            }
                        }
                    });


                }
            });


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int user_position = getAdapterPosition();
                    if (user_position!= RecyclerView.NO_POSITION && listener != null)
                    {
                        listener.OnItemClick(getSnapshots().getSnapshot(user_position),user_position);

                    }

                }
            });
        }
    }

    public interface OnItemClickListener{
        void OnItemClick(DocumentSnapshot documentSnapshot, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;

    }
}