package com.example.fbuapp.Fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.fbuapp.MapActivity;
import com.example.fbuapp.Models.Location;
import com.example.fbuapp.R;
import com.parse.ParseGeoPoint;

import java.util.Calendar;

import static android.app.Activity.RESULT_OK;

public class FilterRideOfferDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    public static final int START_REQUEST_CODE = 1234;
    public static final int END_REQUEST_CODE = 4321;

    public Location mStartLocation;
    public Location mEndLocation;
    private Calendar mEarliestDateCalendar;
    private Calendar mLatestDateCalendar;
    private boolean mEarliestDate;

    private EditText mStartLocationEditText;
    private EditText mStartMileRadiusEditText;
    private EditText mEndLocationEditText;
    private EditText mEndMileRadiusEditText;
    private TextView mEarliestDateTextView;
    private TextView mLatestDateTextView;
    private CheckBox mHideFullRidesCheckBox;
    private Button mApplyFilterButton;

    public interface FilterRideOfferDialogListener{
        void onFinishRideOfferFilterDialog(Location start, int radiusStart, Location end, int radiusEnd, Calendar earliest, Calendar latest, boolean hideFullRides);
    }

    public FilterRideOfferDialogFragment(){

    }

    public static FilterRideOfferDialogFragment newInstance(){
        FilterRideOfferDialogFragment filter = new FilterRideOfferDialogFragment();
        return filter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_dialog_filter_ride_offer, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mStartLocationEditText = view.findViewById(R.id.etStartLocation);
        mStartMileRadiusEditText = view.findViewById(R.id.etStartMileRadius);
        mEndLocationEditText = view.findViewById(R.id.etEndLocation);
        mEndMileRadiusEditText = view.findViewById(R.id.etEndMileRadius);
        mEarliestDateTextView = view.findViewById(R.id.tvEarliestDate);
        mLatestDateTextView = view.findViewById(R.id.tvLatestDate);
        mHideFullRidesCheckBox = view.findViewById(R.id.cbHideFullRides);
        mApplyFilterButton = view.findViewById(R.id.btnApply);

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

        mEarliestDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEarliestDate = true;
                showDatePickerDialog();
            }
        });

        mLatestDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEarliestDate = false;
                showDatePickerDialog();
            }
        });

        mApplyFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilterRideOfferDialogListener listener = (FilterRideOfferDialogListener) getTargetFragment();
                listener.onFinishRideOfferFilterDialog(mStartLocation, Integer.parseInt(mStartMileRadiusEditText.getText().toString()), mEndLocation, Integer.parseInt(mEndMileRadiusEditText.getText().toString()), mEarliestDateCalendar, mLatestDateCalendar, mHideFullRidesCheckBox.isChecked());
                dismiss();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == START_REQUEST_CODE) {
            if(mStartLocation == null){
                mStartLocation = new Location();
            }
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
            if(mEndLocation == null){
                mEndLocation = new Location();
            }
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
        if(mEarliestDate){
            if(mEarliestDateCalendar == null){
                mEarliestDateCalendar = Calendar.getInstance();
            }
            mEarliestDateCalendar.set(year, month-1, dayOfMonth);
            mEarliestDateTextView.setText(date);
        }
        else {
            if(mLatestDateCalendar == null){
                mLatestDateCalendar = Calendar.getInstance();
            }
            mLatestDateCalendar.set(year, month-1, dayOfMonth);
            mLatestDateTextView.setText(date);
        }
    }


}
