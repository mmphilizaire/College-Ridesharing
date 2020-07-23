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
import com.example.fbuapp.Fragments.RideRequestDetailFragment;
import com.example.fbuapp.Fragments.RideRequestFragment;
import com.example.fbuapp.Models.RideRequest;
import com.parse.ParseFile;

import java.util.List;

public class RideRequestsAdapter extends RecyclerView.Adapter<RideRequestsAdapter.ViewHolder> {

    private List<RideRequest> mRideRequests;
    private Context mContext;
    private Fragment mFragment;

    public RideRequestsAdapter(List<RideRequest> rideRequests, Context context, Fragment fragment){
        mRideRequests = rideRequests;
        mContext = context;
        mFragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_ride_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RideRequest rideRequest = mRideRequests.get(position);
        holder.bind(rideRequest);
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

        public ImageView mProfilePictureImageView;
        public TextView mNameTextView;
        public TextView mEarliestDateTextView;
        public TextView mEarliestTimeTextView;
        public TextView mLatestDateTextView;
        public TextView mLatestTimeTextView;
        public TextView mStartLocationTextView;
        public TextView mEndLocationTextView;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            itemView.setOnClickListener(this);

            mProfilePictureImageView = itemView.findViewById(R.id.ivProfilePicture);
            mNameTextView = itemView.findViewById(R.id.tvName);
            mEarliestDateTextView = itemView.findViewById(R.id.tvEarliestDate);
            mEarliestTimeTextView = itemView.findViewById(R.id.tvEarliestTime);
            mLatestDateTextView = itemView.findViewById(R.id.tvLatestDate);
            mLatestTimeTextView = itemView.findViewById(R.id.tvLatestTime);
            mStartLocationTextView = itemView.findViewById(R.id.tvStart);
            mEndLocationTextView = itemView.findViewById(R.id.tvEnd);

        }

        public void bind(RideRequest rideRequest) {
            ParseFile profilePicture = rideRequest.getUser().getParseFile("profilePicture");
            Glide.with(mContext).load(profilePicture.getUrl()).transform(new CircleCrop()).into(mProfilePictureImageView);
            mNameTextView.setText(rideRequest.getUser().getString("firstName"));
            mEarliestDateTextView.setText(rideRequest.getDay(rideRequest.getEarliestDeparture()) + ", " + rideRequest.getDateNoYear(rideRequest.getEarliestDeparture()));
            mEarliestTimeTextView.setText(rideRequest.getTime(rideRequest.getEarliestDeparture()));
            mLatestDateTextView.setText(rideRequest.getDay(rideRequest.getLatestDeparture()) + ", " + rideRequest.getDateNoYear(rideRequest.getLatestDeparture()));
            mLatestTimeTextView.setText(rideRequest.getTime(rideRequest.getLatestDeparture()));
            mStartLocationTextView.setText(rideRequest.getStartLocation().getCity()+", "+rideRequest.getStartLocation().getState());
            mEndLocationTextView.setText(rideRequest.getEndLocation().getCity()+", "+rideRequest.getEndLocation().getState());
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Intent rideRequestDetails = new Intent(mContext, DetailActivity.class);
            rideRequestDetails.putExtra("rideRequest", mRideRequests.get(position));
            mContext.startActivity(rideRequestDetails);
//            int position = getAdapterPosition();
//            FragmentManager fragmentManager = mFragment.getFragmentManager();
//            RideRequestDetailFragment detailFragment = RideRequestDetailFragment.newInstance(mRideRequests.get(position));
//            detailFragment.setTargetFragment(mFragment, 200);
//            detailFragment.show(fragmentManager, "detail_fragment");
        }

    }

}
