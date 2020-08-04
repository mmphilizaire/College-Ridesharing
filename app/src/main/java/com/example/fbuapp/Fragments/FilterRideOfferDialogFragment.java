package com.example.fbuapp.Fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.fbuapp.Activities.MapActivity;
import com.example.fbuapp.Models.Location;
import com.example.fbuapp.Models.RideOfferFilter;
import com.example.fbuapp.databinding.FragmentDialogFilterRideOfferBinding;
import com.parse.ParseGeoPoint;

import org.parceler.Parcels;

import java.util.Calendar;

import static android.app.Activity.RESULT_OK;

public class FilterRideOfferDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private FragmentDialogFilterRideOfferBinding mBinding;

    public static final int START_REQUEST_CODE = 1234;
    public static final int END_REQUEST_CODE = 4321;

    public RideOfferFilter mRideOfferFilter;
    public Location mStartLocation;
    public Location mEndLocation;
    private Calendar mEarliestDateCalendar;
    private Calendar mLatestDateCalendar;
    private boolean mEarliestDate;

    private EditText mStartLocationEditText;
    private EditText mStartMileRadiusEditText;
    private EditText mEndLocationEditText;
    private EditText mEndMileRadiusEditText;
    private EditText mEarliestDateEditText;
    private EditText mLatestDateEditText;
    private CheckBox mHideFullRidesCheckBox;
    private Button mApplyFilterButton;
    private Button mCancelFilterButton;

    private ImageView mDeleteStartLocationImageView;
    private ImageView mDeleteEndLocationImageView;
    private ImageView mDeleteEarliestDateImageView;
    private ImageView mDeleteLatestDateImageView;

    public interface FilterRideOfferDialogListener{
        void onFinishRideOfferFilterDialog(RideOfferFilter filter);
    }

    public FilterRideOfferDialogFragment(){

    }

    public static FilterRideOfferDialogFragment newInstance(RideOfferFilter filter) {
        FilterRideOfferDialogFragment fragment = new FilterRideOfferDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable("filter", Parcels.wrap(filter));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        mBinding = FragmentDialogFilterRideOfferBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RideOfferFilter filter = (RideOfferFilter) Parcels.unwrap(getArguments().getParcelable("filter"));
        mRideOfferFilter = copy(filter);
        bind();
        bindData();
        setOnClickListeners();
    }

    private RideOfferFilter copy(RideOfferFilter filter) {
        RideOfferFilter copy = new RideOfferFilter();
        if(filter.hasStartLocation()){
            copy.setStartLocation(filter.getStartLocation());
            copy.setStartMileRadius(filter.getStartMileRadius());
        }
        if(filter.hasEndLocation()){
            copy.setEndLocation(filter.getEndLocation());
            copy.setEndMileRadius(filter.getEndMileRadius());
        }
        if(filter.hasEarliestDeparture()){
            copy.setEarliestDeparture(filter.getEarliestDeparture());
        }
        if(filter.hasLatestDeparture()){
            copy.setLatestDeparture(filter.getLatestDeparture());
        }
        copy.setHideFullRides(filter.getHideFullRides());
        return copy;
    }

    private void bindData() {
        if(mRideOfferFilter.hasStartLocation()){
            mStartLocationEditText.setText(mRideOfferFilter.getStartLocation().getCity() + ", " + mRideOfferFilter.getStartLocation().getState());
            mStartMileRadiusEditText.setText(String.valueOf(mRideOfferFilter.getStartMileRadius()));
            mDeleteStartLocationImageView.setVisibility(View.VISIBLE);
        }
        if(mRideOfferFilter.hasEndLocation()){
            mEndLocationEditText.setText(mRideOfferFilter.getEndLocation().getCity() + ", " + mRideOfferFilter.getEndLocation().getState());
            mEndMileRadiusEditText.setText(String.valueOf(mRideOfferFilter.getEndMileRadius()));
            mDeleteEndLocationImageView.setVisibility(View.VISIBLE);
        }
        if(mRideOfferFilter.hasEarliestDeparture()){
            mEarliestDateEditText.setText((String) DateFormat.format("MM/d/yyyy", mRideOfferFilter.getEarliestDeparture()));
            mDeleteEarliestDateImageView.setVisibility(View.VISIBLE);
        }
        if(mRideOfferFilter.hasLatestDeparture()){
            mLatestDateEditText.setText((String) DateFormat.format("MM/d/yyyy", mRideOfferFilter.getLatestDeparture()));
            mDeleteLatestDateImageView.setVisibility(View.VISIBLE);
        }
        mHideFullRidesCheckBox.setChecked(mRideOfferFilter.getHideFullRides());
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

        mEarliestDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEarliestDate = true;
                showDatePickerDialog();
            }
        });

        mLatestDateEditText.setOnClickListener(new View.OnClickListener() {
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
                if(!mStartMileRadiusEditText.getText().toString().equals("")){
                    mRideOfferFilter.setStartMileRadius(Integer.parseInt(mStartMileRadiusEditText.getText().toString()));
                }
                if(!mEndMileRadiusEditText.getText().toString().equals("")){
                    mRideOfferFilter.setEndMileRadius(Integer.parseInt(mEndMileRadiusEditText.getText().toString()));
                }
                mRideOfferFilter.setHideFullRides(mHideFullRidesCheckBox.isChecked());
                listener.onFinishRideOfferFilterDialog(mRideOfferFilter);
                dismiss();
            }
        });

        mCancelFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        mDeleteStartLocationImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStartLocation = null;
                mRideOfferFilter.setStartLocation(mStartLocation);
                mStartLocationEditText.setText("");
                mStartMileRadiusEditText.setText("");
                mDeleteStartLocationImageView.setVisibility(View.GONE);
            }
        });

        mDeleteEndLocationImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEndLocation = null;
                mRideOfferFilter.setEndLocation(mEndLocation);
                mEndLocationEditText.setText("");
                mEndMileRadiusEditText.setText("");
                mDeleteEndLocationImageView.setVisibility(View.GONE);
            }
        });

        mDeleteEarliestDateImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEarliestDateCalendar = null;
                mRideOfferFilter.setEarliestDeparture(null);
                mEarliestDateEditText.setText("");
                mDeleteEarliestDateImageView.setVisibility(View.GONE);
            }
        });

        mDeleteLatestDateImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLatestDateCalendar = null;
                mRideOfferFilter.setLatestDeparture(null);
                mLatestDateEditText.setText("");
                mDeleteLatestDateImageView.setVisibility(View.GONE);
            }
        });

    }

    private void bind() {
        mStartLocationEditText = mBinding.etStartLocation;
        mStartMileRadiusEditText = mBinding.etStartMileRadius;
        mEndLocationEditText = mBinding.etEndLocation;
        mEndMileRadiusEditText = mBinding.etEndMileRadius;
        mEarliestDateEditText = mBinding.etEarliestDate;
        mLatestDateEditText = mBinding.etLatestDate;
        mHideFullRidesCheckBox = mBinding.cbHideFullRides;
        mApplyFilterButton = mBinding.btnApply;
        mCancelFilterButton = mBinding.btnCancel;
        mDeleteStartLocationImageView = mBinding.ivDeleteStart;
        mDeleteEndLocationImageView = mBinding.ivDeleteEnd;
        mDeleteEarliestDateImageView = mBinding.ivDeleteEarliestDate;
        mDeleteLatestDateImageView = mBinding.ivDeleteLatestDate;
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
            mRideOfferFilter.setStartLocation(mStartLocation);
            mStartLocationEditText.setText(mStartLocation.getCity() + ", " + mStartLocation.getState());
            if(mStartMileRadiusEditText.getText().toString().equals("")){
                mStartMileRadiusEditText.setText("10");
            }
            mDeleteStartLocationImageView.setVisibility(View.VISIBLE);
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
            mRideOfferFilter.setEndLocation(mEndLocation);
            mEndLocationEditText.setText(mEndLocation.getCity() + ", " + mEndLocation.getState());
            if(mEndMileRadiusEditText.getText().toString().equals("")){
                mEndMileRadiusEditText.setText("10");
            }
            mDeleteEndLocationImageView.setVisibility(View.VISIBLE);
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
            mEarliestDateCalendar.set(year, month, dayOfMonth);
            mRideOfferFilter.setEarliestDeparture(mEarliestDateCalendar.getTime());
            mEarliestDateEditText.setText(date);
            mDeleteEarliestDateImageView.setVisibility(View.VISIBLE);
        }
        else {
            if(mLatestDateCalendar == null){
                mLatestDateCalendar = Calendar.getInstance();
            }
            mLatestDateCalendar.set(year, month, dayOfMonth);
            mRideOfferFilter.setLatestDeparture(mLatestDateCalendar.getTime());
            mLatestDateEditText.setText(date);
            mDeleteLatestDateImageView.setVisibility(View.VISIBLE);
        }
    }


}
