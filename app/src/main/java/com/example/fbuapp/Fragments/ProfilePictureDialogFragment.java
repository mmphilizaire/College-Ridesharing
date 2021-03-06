package com.example.fbuapp.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import com.example.fbuapp.Activities.MainActivity;
import com.example.fbuapp.R;
import com.example.fbuapp.databinding.FragmentDialogProfilePictureBinding;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static com.parse.Parse.getApplicationContext;

public class ProfilePictureDialogFragment extends DialogFragment {

    FragmentDialogProfilePictureBinding mBinding;

    private static final String TAG = "ProfilePictureFragment";
    private static final int CAPTURE_IMAGE_REQUEST_CODE = 342;
    public static final int GALLERY_IMAGE_REQUEST_CODE = 762;

    private TextView mChoosePictureTextView;
    private TextView mTakePictureTextView;
    private TextView mRemovePictureTextView;
    private TextView mCancelTextView;

    private File mPhotoFile;
    private String mPhotoFileName = "profilePicture.png";

    public ProfilePictureDialogFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        mBinding = FragmentDialogProfilePictureBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bind();
        setOnClickListeners();
    }

    private void setOnClickListeners() {
        mChoosePictureTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((MainActivity)getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, GALLERY_IMAGE_REQUEST_CODE);
                    } else {
                        startGallery();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mTakePictureTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });

        mRemovePictureTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removePicture();
            }
        });

        mCancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void bind() {
        mChoosePictureTextView = mBinding.tvLibrary;
        mTakePictureTextView = mBinding.tvTakePhoto;
        mRemovePictureTextView = mBinding.tvRemove;
        mCancelTextView = mBinding.tvCancel;
    }

    private void removePicture() {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");

        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return;
            }
        }

        Bitmap defaultProfilePicture = BitmapFactory.decodeResource(getContext().getResources(),
                R.drawable.default_pfp);

        persistImage(defaultProfilePicture);
        saveProfilePicture();

        ProfileFragment fragment = (ProfileFragment)getTargetFragment();
        fragment.setProfilePicture(defaultProfilePicture);
        dismiss();

    }

    private void takePicture() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        mPhotoFile = getPhotoFileUri(mPhotoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider.mishka-ridesharing", mPhotoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_REQUEST_CODE);
        }
    }

    public void startGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (galleryIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(galleryIntent, GALLERY_IMAGE_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_IMAGE_REQUEST_CODE){
            if (data != null) {
                Uri returnUri = data.getData();
                Bitmap image = loadFromUri(returnUri);
                persistImage(image);
                saveProfilePicture();
                ProfileFragment fragment = (ProfileFragment)getTargetFragment();
                fragment.setProfilePicture(returnUri);
                dismiss();
            }
        }
        else if(requestCode == CAPTURE_IMAGE_REQUEST_CODE){
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(mPhotoFile.getAbsolutePath());
                saveProfilePicture();
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                ProfileFragment fragment = (ProfileFragment)getTargetFragment();
                fragment.setProfilePicture(takenImage);
                dismiss();
                saveProfilePicture();
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveProfilePicture() {
        ParseFile newProfilePictureParse = new ParseFile(mPhotoFile);
        newProfilePictureParse.saveInBackground();
        ParseUser user = ParseUser.getCurrentUser();
        user.put("profilePicture", newProfilePictureParse);
        user.saveInBackground();
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
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

    private void persistImage(Bitmap image) {
        mPhotoFile = getFile();
        if(mPhotoFile == null){
            return;
        }
        try{
            OutputStream os = new FileOutputStream(mPhotoFile);
            image.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e){
            //error
        }
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
