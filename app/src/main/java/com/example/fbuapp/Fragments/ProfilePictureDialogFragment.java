package com.example.fbuapp.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.fbuapp.MainActivity;
import com.example.fbuapp.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.parse.Parse.getApplicationContext;

public class ProfilePictureDialogFragment extends DialogFragment {

    private TextView mChoosePictureTextView;
    private TextView mTakePictureTextView;
    private TextView mRemovePictureTextView;
    private TextView mCancelTextView;

    public ProfilePictureDialogFragment(){

    }

    public static ProfilePictureDialogFragment newInstance(){
        ProfilePictureDialogFragment dialogFragment = new ProfilePictureDialogFragment();
        return dialogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_dialog_profile_picture, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mChoosePictureTextView = view.findViewById(R.id.tvLibrary);
        mTakePictureTextView = view.findViewById(R.id.tvTakePhoto);
        mRemovePictureTextView = view.findViewById(R.id.tvRemove);
        mCancelTextView = view.findViewById(R.id.tvCancel);

        mChoosePictureTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
                        //TODO: check if access if granted from mainactivity and if so, call startGallery
                    } else {
                        startGallery();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mCancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
//    {
//        switch (requestCode) {
//            case 1000:
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    startGallery();
//                } else {
//                    //didn't grant access
//                }
//                break;
//        }
//    }

    private void startGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (galleryIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(galleryIntent, 1000);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null && requestCode == 1000) {
            Uri returnUri = data.getData();
            Bitmap image = loadFromUri(returnUri);
            File newProfilePicture = persistImage(image, "profilePicture.png");
            ParseFile newProfilePictureParse = new ParseFile(newProfilePicture);
            newProfilePictureParse.saveInBackground();
            ParseUser user = ParseUser.getCurrentUser();
            user.put("profilePicture", newProfilePictureParse);
            user.saveInBackground();
            dismiss();
            ProfileFragment fragment = (ProfileFragment)getTargetFragment();
            fragment.setProfilePicture(returnUri);
        }
    }

    // Converts gallery selection from Uri to Bitmap
    public Bitmap loadFromUri(Uri photoUri) {
        Bitmap image = null;
        try {
            // check version of Android on device
            if(Build.VERSION.SDK_INT > 27){
                // on newer versions of Android, use the new decodeBitmap method
                ImageDecoder.Source source = ImageDecoder.createSource(getContext().getContentResolver(), photoUri);
                image = ImageDecoder.decodeBitmap(source);
            } else {
                // support older versions of Android by using getBitmap
                image = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), photoUri);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    private File persistImage(Bitmap image, String s) {
        File file = getFile();
        if(file == null){
            return null;
        }
        try{
            OutputStream os = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e){
            //error
        }
        return file;
    }

    private File getFile() {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");

        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName="MI_"+ timeStamp +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }
}
