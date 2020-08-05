package com.example.fbuapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.fbuapp.Activities.DetailActivity;
import com.example.fbuapp.Fragments.DeleteDialogFragment;
import com.example.fbuapp.Models.RideOffer;
import com.example.fbuapp.OnDoubleTapListener;
import com.example.fbuapp.databinding.ItemRideOfferBinding;
import com.example.fbuapp.databinding.ItemRideOfferProfileBinding;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.json.JSONException;

import java.util.List;

import static com.example.fbuapp.RideStreamPageFragment.REFRESH_REQUEST_CODE;

public class RideOffersProfileAdapter extends RecyclerView.Adapter<RideOffersProfileAdapter.ViewHolder> {

    private List<RideOffer> mRideOffers;
    private Context mContext;
    Fragment mFragment;

    public RideOffersProfileAdapter(List<RideOffer> rideOffers, Fragment fragment){
        mRideOffers = rideOffers;
        mContext = fragment.getContext();
        mFragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRideOfferProfileBinding binding = ItemRideOfferProfileBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemRideOfferProfileBinding mBinding;

        public TextView mTitleTextView;
        public TextView mDepartureTextView;
        public TextView mStartLocationTextView;
        public TextView mEndLocationTextView;
        public TextView mSeatTextView;

        public ViewHolder(@NonNull ItemRideOfferProfileBinding binding){
            super(binding.getRoot());
            mBinding = binding;
            bind();
        }

        public void bind(){
            mTitleTextView = mBinding.tvTitle;
            mDepartureTextView = mBinding.tvDeparture;
            mStartLocationTextView = mBinding.tvStartLocation;
            mEndLocationTextView = mBinding.tvEndLocation;
            mSeatTextView = mBinding.tvSeats;
        }

        public void bindData(final RideOffer rideOffer) {
            if(rideOffer.hasPassenger(ParseUser.getCurrentUser())){
                mTitleTextView.setText(" \tPassenger\t ");
            }
            else{
                mTitleTextView.setText(" \tDriver\t ");
            }
            mDepartureTextView.setText(rideOffer.getDateNoYear() +" at " + rideOffer.getTime());
            mStartLocationTextView.setText(rideOffer.getStartLocation().getCity()+", "+rideOffer.getStartLocation().getState());
            mEndLocationTextView.setText(rideOffer.getEndLocation().getCity()+", "+rideOffer.getEndLocation().getState());
            mSeatTextView.setText(rideOffer.getSeatsAvailable() + " seats left for $" + rideOffer.getSeatPrice().toString() + " each");

            mBinding.getRoot().setOnTouchListener(new OnDoubleTapListener(mContext){
                @Override
                public void onDoubleTap(MotionEvent e) {
                    if(rideOffer.hasPassenger(ParseUser.getCurrentUser())){
                        showDeleteDialog("Cancel Your Seat in Ride");
                    }
                    else{
                        showDeleteDialog("Delete Your Ride Request");
                    }
                }
            });
        }

        private void showDeleteDialog(String title) {
            FragmentManager fm = mFragment.getActivity().getSupportFragmentManager();
            DeleteDialogFragment deleteDialog = DeleteDialogFragment.newInstance(title, mRideOffers.get(getAdapterPosition()), null);
            deleteDialog.setTargetFragment(mFragment, 300);
            deleteDialog.show(fm, "fragment_alert");
        }

    }

}
