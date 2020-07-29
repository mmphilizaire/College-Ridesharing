package com.example.fbuapp.Fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.fbuapp.MapActivity;
import com.example.fbuapp.Models.Location;
import com.example.fbuapp.Models.RideOfferFilter;
import com.example.fbuapp.Models.RideRequestFilter;
import com.example.fbuapp.R;
import com.example.fbuapp.databinding.FragmentDialogFilterRideOfferBinding;
import com.example.fbuapp.databinding.FragmentDialogFilterRideRequestBinding;
import com.parse.ParseGeoPoint;

import java.util.Calendar;

import static android.app.Activity.RESULT_OK;

public class FilterRideRequestDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private FragmentDialogFilterRideRequestBinding mBinding;

    public static final int START_REQUEST_CODE = 1234;
    public static final int END_REQUEST_CODE = 4321;

    public RideRequestFilter mRideRequestFilter;
    public Location mStartLocation;
    public Location mEndLocation;
    private Calendar mDateCalendar;

    private EditText mStartLocationEditText;
    private EditText mStartMileRadiusEditText;
    private EditText mEndLocationEditText;
    private EditText mEndMileRadiusEditText;
    private TextView mDateTextView;
    private Button mApplyFilterButton;

    public interface FilterRideRequestDialogListener{
        void onFinishRideRequestFilterDialog(RideRequestFilter filter);
    }

    public FilterRideRequestDialogFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        mBinding = FragmentDialogFilterRideRequestBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRideRequestFilter = new RideRequestFilter();
        bind();
        setOnClickListeners();
    }

    private void setOnClickListeners() {
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

        mDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        mApplyFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilterRideRequestDialogListener listener = (FilterRideRequestDialogListener) getTargetFragment();
                mRideRequestFilter.setStartMileRadius(Integer.parseInt(mStartMileRadiusEditText.getText().toString()));
                mRideRequestFilter.setEndMileRadius(Integer.parseInt(mEndMileRadiusEditText.getText().toString()));
                listener.onFinishRideRequestFilterDialog(mRideRequestFilter);
                dismiss();
            }
        });
    }

    private void bind() {
        mStartLocationEditText = mBinding.etStartLocation;
        mStartMileRadiusEditText = mBinding.etStartMileRadius;
        mEndLocationEditText = mBinding.etEndLocation;
        mEndMileRadiusEditText = mBinding.etEndMileRadius;
        mDateTextView = mBinding.tvDate;
        mApplyFilterButton = mBinding.btnApply;
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
            mRideRequestFilter.setStartLocation(mStartLocation);
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
            mRideRequestFilter.setEndLocation(mEndLocation);
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
        if(mDateCalendar == null){
            mDateCalendar = Calendar.getInstance();
        }
        mDateCalendar.set(year, month, dayOfMonth);
        mRideRequestFilter.setDeparture(mDateCalendar.getTime());
        mDateTextView.setText(date);
    }


}
