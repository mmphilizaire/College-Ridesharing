package com.example.fbuapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.fbuapp.DetailActivity;
import com.example.fbuapp.Models.RideRequest;
import com.example.fbuapp.databinding.ItemRideRequestBinding;
import com.parse.ParseFile;

import java.util.List;

import static com.example.fbuapp.RideStreamPageFragment.CREATE_REQUEST_CODE;

public class RideRequestsAdapter extends RecyclerView.Adapter<RideRequestsAdapter.ViewHolder> {

    private List<RideRequest> mRideRequests;
    private Fragment mFragment;
    private Context mContext;

    public RideRequestsAdapter(List<RideRequest> rideRequests, Fragment fragment){
        mRideRequests = rideRequests;
        mFragment = fragment;
        mContext = fragment.getContext();
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRideRequestBinding binding = ItemRideRequestBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new RideRequestsAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RideRequest rideRequest = mRideRequests.get(position);
        holder.bindData(rideRequest);
    }

    @Override
    public int getItemCount() {
        return mRideRequests.size();
    }

    public void clear(){
        mRideRequests.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<RideRequest> rideRequests){
        mRideRequests.addAll(rideRequests);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ItemRideRequestBinding mBinding;

        public ImageView mProfilePictureImageView;
        public TextView mNameTextView;
        public TextView mEarliestDateTextView;
        public TextView mEarliestTimeTextView;
        public TextView mLatestDateTextView;
        public TextView mLatestTimeTextView;
        public TextView mStartLocationTextView;
        public TextView mEndLocationTextView;

        public ViewHolder(@NonNull ItemRideRequestBinding binding){
            super(binding.getRoot());
            mBinding = binding;
            mBinding.getRoot().setOnClickListener(this);
            bind();
        }

        private void bind() {
            mProfilePictureImageView = mBinding.ivProfilePicture;
            mNameTextView = mBinding.tvName;
            mEarliestDateTextView = mBinding.tvEarliestDate;
            mEarliestTimeTextView = mBinding.tvEarliestTime;
            mLatestDateTextView = mBinding.tvLatestDate;
            mLatestTimeTextView = mBinding.tvLatestTime;
            mStartLocationTextView = mBinding.tvStart;
            mEndLocationTextView = mBinding.tvEnd;
        }

        public void bindData(RideRequest rideRequest) {
            ParseFile profilePicture = rideRequest.getUser().getParseFile("profilePicture");
            Glide.with(mContext).load(profilePicture.getUrl()).transform(new CircleCrop()).into(mProfilePictureImageView);
            mNameTextView.setText(rideRequest.getUser().getString("firstName"));
            mEarliestDateTextView.setText(rideRequest.getDateNoYear(rideRequest.getEarliestDeparture()));
            mEarliestTimeTextView.setText(rideRequest.getTime(rideRequest.getEarliestDeparture()));
            mLatestDateTextView.setText(rideRequest.getDateNoYear(rideRequest.getLatestDeparture()));
            mLatestTimeTextView.setText(rideRequest.getTime(rideRequest.getLatestDeparture()));
            mStartLocationTextView.setText(rideRequest.getStartLocation().getCity()+", "+rideRequest.getStartLocation().getState());
            mEndLocationTextView.setText(rideRequest.getEndLocation().getCity()+", "+rideRequest.getEndLocation().getState());
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Intent rideRequestDetails = new Intent(mContext, DetailActivity.class);
            rideRequestDetails.putExtra("rideRequest", mRideRequests.get(position));
            mFragment.startActivityForResult(rideRequestDetails, CREATE_REQUEST_CODE);
        }

    }

}
