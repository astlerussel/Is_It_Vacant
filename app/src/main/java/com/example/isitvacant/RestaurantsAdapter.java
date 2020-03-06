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

public class RestaurantsAdapter extends FirestoreRecyclerAdapter<RestaurantsModel, RestaurantsAdapter.RestaurantsHolder> {
    private OnItemClickListener listener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public RestaurantsAdapter(@NonNull FirestoreRecyclerOptions<RestaurantsModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull RestaurantsHolder holder, int position, @NonNull RestaurantsModel model) {


        holder.restoName.setText(model.getName());
        holder.restoAddr.setText(model.getLocation());
        holder.restoType.setText(model.getTypes());
        holder.restoRating.setText(model.getTotal_rating());

        holder.restoDis.setText(model.getDiscription());

        Picasso.get().load(model.getImage()).into(holder.restoImage);



    }

    @NonNull
    @Override
    public RestaurantsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurants_cardview, parent, false);
        return new RestaurantsHolder(view);
    }

    class RestaurantsHolder extends RecyclerView.ViewHolder{

        TextView restoName,restoType,restoAddr;
        TextView restoDis,restoRating;
        ImageView restoImage;

        public RestaurantsHolder(@NonNull View itemView) {
            super(itemView);

            restoName = itemView.findViewById(R.id.restaurant_title);
            restoDis = itemView.findViewById(R.id.restaurant_description);
            restoImage = itemView.findViewById(R.id.menu_image);
            restoRating = itemView.findViewById(R.id.food_rating_number);
            restoType = itemView.findViewById(R.id.restaurant_type);
            restoAddr = itemView.findViewById(R.id.restaurant_location);
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
