package com.example.fbuapp.CreateRideRequest;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.fbuapp.Activities.MainActivity;
import com.example.fbuapp.Models.RideRequest;
import com.example.fbuapp.R;
import com.example.fbuapp.databinding.FragmentRideRequest1Binding;
import com.example.fbuapp.databinding.FragmentRideRequest2Binding;
import com.example.fbuapp.databinding.FragmentRideRequest4Binding;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;

import java.util.Calendar;

public class RideRequestFragment4 extends Fragment {

    private static final String ARG_RIDE_REQUEST = "rideRequest";

    FragmentRideRequest4Binding mBinding;

    private RideRequest mRideRequest;

    private TextView mStartLocationTextView;
    private TextView mEndLocationTextView;
    private TextView mEarliestTimeTextView;
    private TextView mEarliestDateTextView;
    private TextView mLatestTimeTextView;
    private TextView mLatestDateTextView;
    public Button mCreateButton;

    public RideRequestFragment4() {
        // Required empty public constructor
    }

    public static RideRequestFragment4 newInstance(RideRequest rideRequest) {
        RideRequestFragment4 fragment = new RideRequestFragment4();
        Bundle args = new Bundle();
        args.putParcelable(ARG_RIDE_REQUEST, rideRequest);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRideRequest = getArguments().getParcelable(ARG_RIDE_REQUEST);
        mBinding = FragmentRideRequest4Binding.inflate(getLayoutInflater(), container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bind();
        bindData();

        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRideRequest.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Log.e("Mishka", "save");
                        if(e != null){
                            Log.e("Mishka", "exception: " + e.toString());
                        }
                        else{
                            Log.e("Mishka", "no exception");
                        }
                    }
                });
                MainActivity activity = (MainActivity) getActivity();
                activity.goToRideRequestStream(mRideRequest);
            }
        });
    }

    private void bind() {
        mEarliestDateTextView = mBinding.tvEarliestDate;
        mEarliestTimeTextView = mBinding.tvEarliestTime;
        mLatestDateTextView = mBinding.tvLatestDate;
        mLatestTimeTextView = mBinding.tvLatestTime;
        mStartLocationTextView = mBinding.tvStartLocatoion;
        mEndLocationTextView = mBinding.tvEndLocation;
        mCreateButton = mBinding.btnCreate;
    }

    public void bindData() {
        mEarliestDateTextView.setText(mRideRequest.getDay(mRideRequest.getEarliestDeparture()) + ", " + mRideRequest.getDateNoYear(mRideRequest.getEarliestDeparture()));
        mEarliestTimeTextView.setText(mRideRequest.getTime(mRideRequest.getEarliestDeparture()));
        mLatestDateTextView.setText(mRideRequest.getDay(mRideRequest.getLatestDeparture()) + ", " + mRideRequest.getDateNoYear(mRideRequest.getLatestDeparture()));
        mLatestTimeTextView.setText(mRideRequest.getTime(mRideRequest.getLatestDeparture()));
        mStartLocationTextView.setText(mRideRequest.getStartLocation().getCity()+", "+mRideRequest.getStartLocation().getState());
        mEndLocationTextView.setText(mRideRequest.getEndLocation().getCity()+", "+mRideRequest.getEndLocation().getState());
    }

}