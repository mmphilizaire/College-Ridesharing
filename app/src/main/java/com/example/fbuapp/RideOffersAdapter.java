package com.example.fbuapp;

import android.content.Context;
import android.content.Intent;
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
import com.example.fbuapp.databinding.ItemRideOfferBinding;
import com.parse.ParseFile;

import java.util.List;

import static com.example.fbuapp.RideStreamPageFragment.DETAIL_REQUEST_CODE;

public class RideOffersAdapter extends RecyclerView.Adapter<RideOffersAdapter.ViewHolder> {

    private List<RideOffer> mRideOffers;
    private Context mContext;
    Fragment mFragment;

    public RideOffersAdapter(List<RideOffer> rideOffers, Fragment fragment){
        mRideOffers = rideOffers;
        mContext = fragment.getContext();
        mFragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRideOfferBinding binding = ItemRideOfferBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RideOffer rideOffer = mRideOffers.get(position);
        holder.bindData(rideOffer);
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

        private ItemRideOfferBinding mBinding;

        public ImageView mProfilePictureImageView;
        public TextView mNameTextView;
        public TextView mDateTextView;
        public TextView mTimeTextView;
        public TextView mStartLocationTextView;
        public TextView mEndLocationTextView;
        public TextView mPricePerSeatTextView;
        public TextView mSeatsAvailableTextView;

        public ViewHolder(@NonNull ItemRideOfferBinding binding){
            super(binding.getRoot());
            mBinding = binding;
            mBinding.getRoot().setOnClickListener(this);
            bind();
        }

        public void bind(){
            mProfilePictureImageView = mBinding.ivProfilePicture;
            mNameTextView = mBinding.tvName;
            mDateTextView = mBinding.tvDate;
            mTimeTextView = mBinding.tvTime;
            mStartLocationTextView = mBinding.tvStart;
            mEndLocationTextView = mBinding.tvEnd;
            mPricePerSeatTextView = mBinding.tvSeatPrice;
            mSeatsAvailableTextView = mBinding.tvSeats;
        }

        public void bindData(RideOffer rideOffer) {
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
            Intent rideOfferDetails = new Intent(mContext, DetailActivity.class);
            rideOfferDetails.putExtra("rideOffer", mRideOffers.get(position));
            mFragment.startActivityForResult(rideOfferDetails, DETAIL_REQUEST_CODE);
        }
    }

}
