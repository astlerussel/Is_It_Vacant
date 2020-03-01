package com.example.isitvacant;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
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

import static com.example.isitvacant.RestaurantsDetails.flag;

public class TimeSlotAdapter extends FirestoreRecyclerAdapter<ModelTimeSlot, TimeSlotAdapter.TimeSlotHolder> {
    private OnItemClickListener listener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public TimeSlotAdapter(@NonNull FirestoreRecyclerOptions<ModelTimeSlot> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final TimeSlotHolder holder, int position, @NonNull ModelTimeSlot model) {


        holder.timeslot.setText(model.getTime_slot());






    }

    @NonNull
    @Override
    public TimeSlotHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_slot_card, parent, false);
        return new TimeSlotHolder(view);
    }

    class TimeSlotHolder extends RecyclerView.ViewHolder{

        TextView timeslot;



        public TimeSlotHolder(@NonNull View itemView) {
            super(itemView);

            timeslot = itemView.findViewById(R.id.Time_Slots);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int user_position = getAdapterPosition();
                    if (user_position!= RecyclerView.NO_POSITION && listener != null)
                    {
                        listener.OnItemClick(getSnapshots().getSnapshot(user_position),user_position);

                        if (flag==0) {



                            timeslot.setBackgroundResource(R.drawable.bg_button_release);

                            flag = 1;



                        }
                        else if (flag==1){

                            timeslot.setBackgroundResource(R.drawable.bg_button);

                            flag= 0;
                        }



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
