package com.example.fbuapp.Fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.fbuapp.Models.Location;
import com.example.fbuapp.MapActivity;
import com.example.fbuapp.R;
import com.example.fbuapp.Models.RideOffer;
import com.parse.ParseUser;

import java.util.Calendar;

import static android.app.Activity.RESULT_OK;

public class RideOfferFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    public static final int START_REQUEST_CODE = 1234;
    public static final int END_REQUEST_CODE = 4321;

    private FragmentManager mFragmentManager;

    private EditText mStartLocationEditText;
    private EditText mEndLocationEditText;
    private TextView mDepartureDateTextView;
    private TextView mDepartureTimeTextView;
    private EditText mSeatPriceEditText;
    private EditText mSeatCountEditText;

    public Button mCreateButton;

    public Location mStartLocation;
    public Location mEndLocation;
    private Calendar mDepartureCalendar;

    public RideOfferFragment() {
        // Required empty public constructor
    }

    public RideOfferFragment(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ride_offer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mStartLocation = new Location();
        mEndLocation = new Location();
        mDepartureCalendar = Calendar.getInstance();

        mDepartureDateTextView = view.findViewById(R.id.tvDepartureDate);
        mDepartureTimeTextView = view.findViewById(R.id.tvDepartureTime);
        mStartLocationEditText = view.findViewById(R.id.etStartLocation);
        mEndLocationEditText = view.findViewById(R.id.etEndLocation);
        mSeatCountEditText = view.findViewById(R.id.etSeatCount);
        mSeatPriceEditText = view.findViewById(R.id.etSeatPrice);

        mCreateButton = view.findViewById(R.id.btnOffer);

        mDepartureDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        mDepartureTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog();
            }
        });

        mStartLocationEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent map = new Intent(getActivity(), MapActivity.class);
                startActivityForResult(map, START_REQUEST_CODE);
            }
        });

        mEndLocationEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent map = new Intent(getActivity(), MapActivity.class);
                startActivityForResult(map, END_REQUEST_CODE);
            }
        });

        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RideOffer rideOffer = new RideOffer();
                rideOffer.setUser(ParseUser.getCurrentUser());
                rideOffer.setDepartureTime(mDepartureCalendar.getTime());
                rideOffer.setSeatPrice((Number) Integer.parseInt(mSeatPriceEditText.getText().toString()));
                rideOffer.setSeatCount((Number) Integer.parseInt(mSeatCountEditText.getText().toString()));
                rideOffer.setStartLocation(mStartLocation);
                rideOffer.setEndLocation(mEndLocation);
                rideOffer.saveInBackground();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == START_REQUEST_CODE) {
            mStartLocation.setLatitude((Number) data.getExtras().getDouble("latitude"));
            mStartLocation.setLongitude((Number) data.getExtras().getDouble("longitude"));
            mStartLocation.setCity(data.getExtras().getString("city"));
            mStartLocation.setState(data.getExtras().getString("state"));
            mStartLocationEditText.setText(mStartLocation.getCity() + ", " + mStartLocation.getState());
        }
        else if(resultCode == RESULT_OK && requestCode == END_REQUEST_CODE){
            mEndLocation.setLatitude((Number) data.getExtras().getDouble("latitude"));
            mEndLocation.setLongitude((Number) data.getExtras().getDouble("longitude"));
            mEndLocation.setCity(data.getExtras().getString("city"));
            mEndLocation.setState(data.getExtras().getString("state"));
            mEndLocationEditText.setText(mEndLocation.getCity() + ", " + mEndLocation.getState());
        }
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
        mDepartureCalendar.set(year, month-1, dayOfMonth);
        mDepartureDateTextView.setText(date);

    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        String time;
        mDepartureCalendar.set(mDepartureCalendar.get(Calendar.YEAR), mDepartureCalendar.get(Calendar.MONTH), mDepartureCalendar.get(Calendar.DAY_OF_MONTH), hour, minute);
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
        mDepartureTimeTextView.setText(time);
    }

}