package com.example.isitvacant;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class DisplayMenuAdapter extends FirestoreRecyclerAdapter<ModelDisplayMenu, DisplayMenuAdapter.DisplayMenuHolder> {
    private OnItemClickListener listener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public DisplayMenuAdapter(@NonNull FirestoreRecyclerOptions<ModelDisplayMenu> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull DisplayMenuHolder holder, int position, @NonNull ModelDisplayMenu model) {

        holder.cost.setText(model.getCost());
        holder.Name.setText(model.getName());
        holder.type.setText(model.getType());




        Picasso.get().load(model.getImage()).into(holder.menuImage);



    }

    @NonNull
    @Override
    public DisplayMenuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_card, parent, false);
        return new DisplayMenuHolder(view);
    }

    class DisplayMenuHolder extends RecyclerView.ViewHolder{


        TextView Name,cost,type;

        ImageView menuImage;

        public DisplayMenuHolder(@NonNull View itemView) {
            super(itemView);
            Name=itemView.findViewById(R.id.Food_name);
            cost=itemView.findViewById(R.id.food_costs);
            type= itemView.findViewById(R.id.Food_type);
            menuImage = itemView.findViewById(R.id.menu_image);




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