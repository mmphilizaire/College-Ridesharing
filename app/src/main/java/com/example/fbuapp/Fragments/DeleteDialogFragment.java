package com.example.fbuapp.Fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.fbuapp.Models.RideOffer;
import com.example.fbuapp.Models.RideRequest;

public class DeleteDialogFragment extends DialogFragment {

    public interface DeleteDialogListener{
        void onFinishDeleteDialog(RideOffer rideOffer, RideRequest rideRequest);
    }

    public DeleteDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public static DeleteDialogFragment newInstance(String title, RideOffer rideOffer, RideRequest rideRequest) {
        DeleteDialogFragment frag = new DeleteDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putParcelable("rideOffer", rideOffer);
        args.putParcelable("rideRequest", rideRequest);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage("Are you sure?");
        alertDialogBuilder.setPositiveButton("OK",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RideOffer rideOffer = getArguments().getParcelable("rideOffer");
                RideRequest rideRequest = getArguments().getParcelable("rideRequest");
                DeleteDialogListener listener = (DeleteDialogListener) getTargetFragment();
                listener.onFinishDeleteDialog(rideOffer, rideRequest);
                dismiss();
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        return alertDialogBuilder.create();
    }
}