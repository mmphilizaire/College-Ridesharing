package com.example.fbuapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
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
import com.example.fbuapp.Fragments.DeleteDialogFragment;
import com.example.fbuapp.Models.RideRequest;
import com.example.fbuapp.OnDoubleTapListener;
import com.example.fbuapp.databinding.ItemRideRequestBinding;
import com.example.fbuapp.databinding.ItemRideRequestProfileBinding;
import com.parse.ParseFile;

import java.util.List;

import static com.example.fbuapp.RideStreamPageFragment.CREATE_REQUEST_CODE;

public class RideRequestsProfileAdapter extends RecyclerView.Adapter<RideRequestsProfileAdapter.ViewHolder> {

    private List<RideRequest> mRideRequests;
    private Fragment mFragment;
    private Context mContext;

    public RideRequestsProfileAdapter(List<RideRequest> rideRequests, Fragment fragment){
        mRideRequests = rideRequests;
        mFragment = fragment;
        mContext = fragment.getContext();
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRideRequestProfileBinding binding = ItemRideRequestProfileBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new RideRequestsProfileAdapter.ViewHolder(binding);
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemRideRequestProfileBinding mBinding;

        public TextView mEarliestDepartureTextView;
        public TextView mLatestDepartureTextView;
        public TextView mStartLocationTextView;
        public TextView mEndLocationTextView;

        public ViewHolder(@NonNull ItemRideRequestProfileBinding binding){
            super(binding.getRoot());
            mBinding = binding;
            bind();
        }

        private void bind() {
            mEarliestDepartureTextView = mBinding.tvEarliestDeparture;
            mLatestDepartureTextView = mBinding.tvLatestDeparture;
            mStartLocationTextView = mBinding.tvStartLocation;
            mEndLocationTextView = mBinding.tvEndLocation;
        }

        public void bindData(RideRequest rideRequest) {
            mEarliestDepartureTextView.setText(rideRequest.getDateNoYear(rideRequest.getEarliestDeparture()) + " at " + rideRequest.getTime(rideRequest.getEarliestDeparture()));
            mLatestDepartureTextView.setText(rideRequest.getDateNoYear(rideRequest.getLatestDeparture()) + " at " + rideRequest.getTime(rideRequest.getLatestDeparture()));
            mStartLocationTextView.setText(rideRequest.getStartLocation().getCity()+", "+rideRequest.getStartLocation().getState());
            mEndLocationTextView.setText(rideRequest.getEndLocation().getCity()+", "+rideRequest.getEndLocation().getState());
            mBinding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    showDeleteDialog("Delete Your Ride Request");
                    return true;
                }
            });
        }

        private void showDeleteDialog(String title) {
            FragmentManager fm = mFragment.getActivity().getSupportFragmentManager();
            DeleteDialogFragment deleteDialog = DeleteDialogFragment.newInstance(title, null, mRideRequests.get(getAdapterPosition()));
            deleteDialog.setTargetFragment(mFragment, 300);
            deleteDialog.show(fm, "fragment_alert");
        }

    }

}
