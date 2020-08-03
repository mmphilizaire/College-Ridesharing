package com.example.fbuapp.CreateRideOffer;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.fbuapp.Activities.MainActivity;
import com.example.fbuapp.Models.RideOffer;
import com.example.fbuapp.Models.RideRequest;
import com.example.fbuapp.R;
import com.example.fbuapp.databinding.FragmentRideOffer2Binding;
import com.example.fbuapp.databinding.FragmentRideRequest1Binding;
import com.example.fbuapp.databinding.FragmentRideRequest2Binding;

import java.util.Calendar;

public class RideOfferFragment2 extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    private static final String ARG_RIDE_OFFER = "rideOffer";

    FragmentRideOffer2Binding mBinding;

    private RideOffer mRideOffer;
    private Calendar mDepartureCalendar;

    private EditText mDateEditText;
    private EditText mTimeEditText;
    public Button mNextButton;

    public RideOfferFragment2() {
        // Required empty public constructor
    }

    public static RideOfferFragment2 newInstance(RideOffer rideOffer) {
        RideOfferFragment2 fragment = new RideOfferFragment2();
        Bundle args = new Bundle();
        args.putParcelable(ARG_RIDE_OFFER, rideOffer);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRideOffer = getArguments().getParcelable(ARG_RIDE_OFFER);
        mBinding = FragmentRideOffer2Binding.inflate(getLayoutInflater(), container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDepartureCalendar = Calendar.getInstance();

        bind();
        setOnClickListeners();
    }

    private void bind() {
        mDateEditText = mBinding.etDate;
        mTimeEditText = mBinding.etTime;
        mNextButton = mBinding.btnNext;
    }

    private void setOnClickListeners() {
        mDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        mTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog();
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!missingInfo()){
                    mRideOffer.setDepartureTime(mDepartureCalendar.getTime());
                    MainActivity activity = (MainActivity) getActivity();
                    activity.goToNextRideOfferFragment(mRideOffer);
                }
                else{
                    Toast.makeText(getActivity(), "Missing information!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean missingInfo() {
        if(mDateEditText.getText().toString().equals("")){
            return true;
        }
        if(mTimeEditText.getText().toString().equals("")){
            return true;
        }
        return false;
    }

    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), this,
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE),
                DateFormat.is24HourFormat(getContext()));
        timePickerDialog.show();
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        String date = month+1 + "/" + dayOfMonth + "/" + year;
        mDepartureCalendar.set(year, month, dayOfMonth);
        mDateEditText.setText(date);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        String time;
        if(DateFormat.is24HourFormat(getContext())){
            if(minute < 10){
                time = hour + ":0" + minute;
            }
            else{
                time = hour + ":" + minute;
            }
        }
        else{
            if(hour >= 12){
                if(hour == 12){
                    hour = 24;
                }
                if(minute < 10){
                    time = hour-12 + ":0" + minute + "PM";
                }
                else{
                    time = hour-12 + ":" + minute + "PM";
                }
            }
            else{
                if(hour == 0){
                    hour = 12;
                }
                if(minute < 10){
                    time = hour + ":0" + minute + "AM";
                }
                else{
                    time = hour + ":" + minute + "AM";
                }
            }
        }
        mDepartureCalendar.set(mDepartureCalendar.get(Calendar.YEAR), mDepartureCalendar.get(Calendar.MONTH), mDepartureCalendar.get(Calendar.DAY_OF_MONTH), hour, minute);
        mTimeEditText.setText(time);
    }

}