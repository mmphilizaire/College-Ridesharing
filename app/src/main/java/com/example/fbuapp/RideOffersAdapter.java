package com.example.fbuapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.fbuapp.Fragments.RideOfferDetailFragment;
import com.example.fbuapp.Models.RideOffer;
import com.parse.ParseFile;

import java.util.List;

public class RideOffersAdapter extends RecyclerView.Adapter<RideOffersAdapter.ViewHolder> {

    private List<RideOffer> mRideOffers;
    private Context mContext;
    private Fragment mFragment;

    public RideOffersAdapter(List<RideOffer> rideOffers, Context context, Fragment fragment){
        mRideOffers = rideOffers;
        mContext = context;
        mFragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_ride_offer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RideOffer rideOffer = mRideOffers.get(position);
        holder.bind(rideOffer);
    }

    @Override
    public int getItemCount() {
        return mRideOffers.size();
    }

    public void clear(){
        mRideOffers.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<RideOffer> rideOffers){
        mRideOffers.addAll(rideOffers);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

       // private RideOffer mRideOffer;

        public ImageView mProfilePictureImageView;
        public TextView mNameTextView;
        public TextView mDateTextView;
        public TextView mTimeTextView;
        public TextView mStartLocationTextView;
        public TextView mEndLocationTextView;
        public TextView mPricePerSeatTextView;
        public TextView mSeatsAvailableTextView;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            itemView.setOnClickListener(this);

            mProfilePictureImageView = itemView.findViewById(R.id.ivProfilePicture);
            mNameTextView = itemView.findViewById(R.id.tvName);
            mDateTextView = itemView.findViewById(R.id.tvDate);
            mTimeTextView = itemView.findViewById(R.id.tvTime);
            mStartLocationTextView = itemView.findViewById(R.id.tvStart);
            mEndLocationTextView = itemView.findViewById(R.id.tvEnd);
            mPricePerSeatTextView = itemView.findViewById(R.id.tvSeatPrice);
            mSeatsAvailableTextView = itemView.findViewById(R.id.tvSeats);

        }

        public void bind(RideOffer rideOffer) {
            ParseFile profilePicture = rideOffer.getUser().getParseFile("profilePicture");
            Glide.with(mContext).load(profilePicture.getUrl()).transform(new CircleCrop()).into(mProfilePictureImageView);
            mNameTextView.setText(rideOffer.getUser().getString("firstName"));
            mDateTextView.setText(rideOffer.getDay() + ", " + rideOffer.getDateNoYear());
            mTimeTextView.setText(rideOffer.getTime());
            mStartLocationTextView.setText(rideOffer.getStartLocation().getCity()+", "+rideOffer.getStartLocation().getState());
            mEndLocationTextView.setText(rideOffer.getEndLocation().getCity()+", "+rideOffer.getEndLocation().getState());
            mPricePerSeatTextView.setText("$" + rideOffer.getSeatPrice().toString() + "\nper seat");
            mSeatsAvailableTextView.setText(rideOffer.getSeatsAvailable() + " seats left");
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            FragmentManager fragmentManager = mFragment.getFragmentManager();
            RideOfferDetailFragment detailFragment = RideOfferDetailFragment.newInstance(mRideOffers.get(position));
            detailFragment.setTargetFragment(mFragment, 200);
            detailFragment.show(fragmentManager, "detail_fragment");
        }
    }

}
