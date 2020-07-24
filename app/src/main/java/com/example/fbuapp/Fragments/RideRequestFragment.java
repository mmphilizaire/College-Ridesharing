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
import android.widget.Toast;

import com.example.fbuapp.Models.Location;
import com.example.fbuapp.MapActivity;
import com.example.fbuapp.Models.RideOffer;
import com.example.fbuapp.R;
import com.example.fbuapp.Models.RideRequest;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

import java.util.Calendar;

import static android.app.Activity.RESULT_OK;

public class RideRequestFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    public static final int START_REQUEST_CODE = 1234;
    public static final int END_REQUEST_CODE = 4321;

    private FragmentManager mFragmentManager;

    private EditText mStartLocationEditText;
    private EditText mEndLocationEditText;
    private TextView mEarliestDateTextView;
    private TextView mEarliestTimeTextView;
    private TextView mLatestDateTextView;
    private TextView mLatestTimeTextView;

    public Button mCreateButton;

    public Location mStartLocation;
    public Location mEndLocation;
    private Calendar mEarliestDepartureCalendar;
    private Calendar mLatestDepartureCalendar;
    private boolean mEarliestDeparture;

    public RideRequestFragment() {
        // Required empty public constructor
    }

    public RideRequestFragment(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ride_request, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mStartLocation = new Location();
        mEndLocation = new Location();
        mEarliestDepartureCalendar = Calendar.getInstance();
        mLatestDepartureCalendar = Calendar.getInstance();
        mEarliestDeparture = false;

        mEarliestDateTextView = view.findViewById(R.id.tvEarliestDate);
        mEarliestTimeTextView = view.findViewById(R.id.tvEarliestTime);
        mLatestDateTextView = view.findViewById(R.id.tvLatestDate);
        mLatestTimeTextView = view.findViewById(R.id.tvLatestTime);
        mStartLocationEditText = view.findViewById(R.id.etStartLocation);
        mEndLocationEditText = view.findViewById(R.id.etEndLocation);

        mCreateButton = view.findViewById(R.id.btnRequest);

        mEarliestDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEarliestDeparture = true;
                showDatePickerDialog();
            }
        });

        mLatestDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEarliestDeparture = false;
                showDatePickerDialog();
            }
        });

        mEarliestTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEarliestDeparture = true;
                showTimePickerDialog();
            }
        });

        mLatestTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEarliestDeparture = false;
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
                if(!missingInfo()){
                    RideRequest rideRequest = new RideRequest();
                    rideRequest.setUser(ParseUser.getCurrentUser());
                    rideRequest.setEarliestDeparture(mEarliestDepartureCalendar.getTime());
                    rideRequest.setLatestDeparture(mLatestDepartureCalendar.getTime());
                    rideRequest.setStartLocation(mStartLocation);
                    rideRequest.setEndLocation(mEndLocation);
                    rideRequest.saveInBackground();
                }
                else{
                    Toast.makeText(getActivity(), "Missing information!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private boolean missingInfo() {
        if(mStartLocation == null || mEndLocation == null){
            return true;
        }
        if(mEarliestDateTextView.getText().toString().equals("Select a Date")){
            return true;
        }
        if(mLatestDateTextView.getText().toString().equals("Select a Date")){
            return true;
        }
        if(mEarliestTimeTextView.getText().toString().equals("Select a Time")){
            return true;
        }
        if(mLatestTimeTextView.getText().toString().equals("Select a Time")){
            return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == START_REQUEST_CODE) {
            double latitude = data.getExtras().getDouble("latitude");
            double longitude = data.getExtras().getDouble("longitude");
            mStartLocation.setLatitude((Number) latitude );
            mStartLocation.setLongitude((Number) longitude);
            mStartLocation.setCity(data.getExtras().getString("city"));
            mStartLocation.setState(data.getExtras().getString("state"));
            mStartLocation.setGeoPoint(new ParseGeoPoint(latitude, longitude));
            mStartLocationEditText.setText(mStartLocation.getCity() + ", " + mStartLocation.getState());
        }
        else if(resultCode == RESULT_OK && requestCode == END_REQUEST_CODE){
            double latitude = data.getExtras().getDouble("latitude");
            double longitude = data.getExtras().getDouble("longitude");
            mEndLocation.setLatitude((Number) latitude);
            mEndLocation.setLongitude((Number) longitude);
            mEndLocation.setCity(data.getExtras().getString("city"));
            mEndLocation.setState(data.getExtras().getString("state"));
            mEndLocation.setGeoPoint(new ParseGeoPoint(latitude, longitude));
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
        if(mEarliestDeparture){
            mEarliestDepartureCalendar.set(year, month, dayOfMonth);
            mEarliestDateTextView.setText(date);
        }
        else {
            mLatestDepartureCalendar.set(year, month, dayOfMonth);
            mLatestDateTextView.setText(date);
        }
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
        if(mEarliestDeparture){
            mEarliestDepartureCalendar.set(mEarliestDepartureCalendar.get(Calendar.YEAR), mEarliestDepartureCalendar.get(Calendar.MONTH), mEarliestDepartureCalendar.get(Calendar.DAY_OF_MONTH), hour, minute);
            mEarliestTimeTextView.setText(time);
        }
        else{
            mLatestDepartureCalendar.set(mLatestDepartureCalendar.get(Calendar.YEAR), mLatestDepartureCalendar.get(Calendar.MONTH), mLatestDepartureCalendar.get(Calendar.DAY_OF_MONTH), hour, minute);
            mLatestTimeTextView.setText(time);
        }
    }

}