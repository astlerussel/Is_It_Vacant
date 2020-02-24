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

public class ReviewsAdapter extends FirestoreRecyclerAdapter<ModelReviews, ReviewsAdapter.ReviewsHolder> {
    private OnItemClickListener listener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ReviewsAdapter(@NonNull FirestoreRecyclerOptions<ModelReviews> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ReviewsHolder holder, int position, @NonNull ModelReviews model) {

        holder.review.setText(model.getReview());
        holder.Name.setText(model.getName());
        holder.rating.setText(model.getRating());



        Picasso.get().load(model.getImage()).into(holder.restoImage);



    }

    @NonNull
    @Override
    public ReviewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reviews_card_layout, parent, false);
        return new ReviewsHolder(view);
    }

    class ReviewsHolder extends RecyclerView.ViewHolder{

        TextView Name,rating,review;

        CircleImageView restoImage;

        public ReviewsHolder(@NonNull View itemView) {
            super(itemView);
            Name=itemView.findViewById(R.id.username);
            rating=itemView.findViewById(R.id.rating);
            review= itemView.findViewById(R.id.reviews);
            restoImage = itemView.findViewById(R.id.profile_image);


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
