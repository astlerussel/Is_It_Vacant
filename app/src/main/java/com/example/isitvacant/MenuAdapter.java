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

import de.hdodenhof.circleimageview.CircleImageView;

public class MenuAdapter extends FirestoreRecyclerAdapter<ModelMenu, MenuAdapter.MenuHolder> {
    private OnItemClickListener listener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MenuAdapter(@NonNull FirestoreRecyclerOptions<ModelMenu> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MenuHolder holder, int position, @NonNull ModelMenu model) {

        holder.cost.setText(model.getCost());
        holder.Name.setText(model.getName());
        holder.type.setText(model.getType());




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

        public MenuHolder(@NonNull View itemView) {
            super(itemView);
            Name=itemView.findViewById(R.id.food_title);
            cost=itemView.findViewById(R.id.Food_cost);
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
                    elegantNumberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                        @Override
                        public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                            if(newValue == 0)
                            {
                                Add.setVisibility(View.VISIBLE);
                                elegantNumberButton.setVisibility(View.INVISIBLE);
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