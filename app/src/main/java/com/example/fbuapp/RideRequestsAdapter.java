package com.example.fbuapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.fbuapp.Models.RideOffer;
import com.example.fbuapp.Models.RideRequest;
import com.parse.ParseFile;

import java.util.List;

public class RideRequestsAdapter extends RecyclerView.Adapter<RideRequestsAdapter.ViewHolder> {

    private List<RideRequest> mRideRequests;
    private Context mContext;

    public RideRequestsAdapter(List<RideRequest> rideRequests, Context context){
        mRideRequests = rideRequests;
        mContext = context;
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

    public class ViewHolder extends RecyclerView.ViewHolder{

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
    }

}
