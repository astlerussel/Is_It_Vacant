package com.example.isitvacant;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class CartAdapter extends FirestoreRecyclerAdapter<ModelMenuInfo, CartAdapter.CartHolder> {
    private OnItemClickListener listener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public CartAdapter(@NonNull FirestoreRecyclerOptions<ModelMenuInfo> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CartHolder holder, int position, @NonNull ModelMenuInfo model) {


        holder.Name.setText(model.getFoodName());
        holder.noOfItems.setText(String.valueOf(model.getQuantity()));
        holder.type.setText(model.getType());
        holder.price.setText(String.valueOf(model.getTotalPrice()));






    }

    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_of_food_items, parent, false);
        return new CartHolder(view);
    }

    class CartHolder extends RecyclerView.ViewHolder{

        TextView Name,noOfItems,type,price;



        public CartHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.food_name);
            noOfItems = itemView.findViewById(R.id.no_of_items);
            type = itemView.findViewById(R.id.food_category);
            price = itemView.findViewById(R.id.food_cost);



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
