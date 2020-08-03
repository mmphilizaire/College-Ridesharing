package com.example.fbuapp.SignUp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.fbuapp.Navigation;
import com.example.fbuapp.R;
import com.example.fbuapp.databinding.FragmentRegister3Binding;
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

public class RegisterFragment3 extends Fragment {

    private FragmentRegister3Binding mBinding;

    private static final String PARSE_USER = "user";
    private static final String TAG = "RegisterFragment3";
    private static final int CAPTURE_IMAGE_REQUEST_CODE = 342;
    public static final int GALLERY_IMAGE_REQUEST_CODE = 762;

    private ParseUser mUser;
    private ImageView mProfilePictureImageView;
    private TextView mPhotoTextView;
    private TextView mGalleryTextView;
    private TextView mDefaultTextView;
    private Button mFinishButton;

    private File mPhotoFile;
    private String mPhotoFileName = "profilePicture.png";

    public RegisterFragment3() {
        // Required empty public constructor
    }

    public static RegisterFragment3 newInstance(ParseUser user) {
        RegisterFragment3 fragment = new RegisterFragment3();
        Bundle args = new Bundle();
        args.putParcelable(PARSE_USER, user);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mUser = getArguments().getParcelable(PARSE_USER);
        mBinding = FragmentRegister3Binding.inflate(getLayoutInflater(), container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bind();
        setOnClickListeners();

    }

    private void setOnClickListeners() {
        mPhotoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });

        mGalleryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGallery();
            }
        });

        mDefaultTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDefault();
            }
        });

        mFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.goMainActivity((RegisterActivity)getActivity());
            }
        });
    }

    private void bind() {
        mProfilePictureImageView = mBinding.ivProfilePicture;
        mPhotoTextView = mBinding.tvCamera;
        mGalleryTextView = mBinding.tvGallery;
        mDefaultTextView = mBinding.tvDefault;
        mFinishButton = mBinding.btnFinish;
    }

    private void setDefault() {
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

        setProfilePicture(defaultProfilePicture);

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
                setProfilePicture(returnUri);
            }
        }
        else if(requestCode == CAPTURE_IMAGE_REQUEST_CODE){
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(mPhotoFile.getAbsolutePath());
                saveProfilePicture();
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                setProfilePicture(takenImage);
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveProfilePicture() {
        ParseFile newProfilePictureParse = new ParseFile(mPhotoFile);
        newProfilePictureParse.saveInBackground();
        mUser.put("profilePicture", newProfilePictureParse);
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

    public void setProfilePicture(Uri profilePicture){
        Glide.with(getContext()).load(profilePicture).transform(new CircleCrop()).into(mProfilePictureImageView);
        mFinishButton.setVisibility(View.VISIBLE);
    }

    public void setProfilePicture(Bitmap profilePicture) {
        Glide.with(getContext()).load(profilePicture).transform(new CircleCrop()).into(mProfilePictureImageView);
        mFinishButton.setVisibility(View.VISIBLE);
    }

    public ParseUser getUser(){
        return mUser;
    }

}