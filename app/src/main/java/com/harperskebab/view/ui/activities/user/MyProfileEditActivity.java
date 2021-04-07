package com.harperskebab.view.ui.activities.user;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.harperskebab.R;
import com.harperskebab.databinding.ActivityMyProfileEditBinding;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.utils.Constant;
import com.harperskebab.utils.PopMessage;
import com.harperskebab.utils.Utility;
import com.harperskebab.view.ui.activities.BaseActivity;
import com.harperskebab.viewmodel.ProfileViewModel;
import com.harperskebab.viewmodel.UserViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyProfileEditActivity extends BaseActivity {
    private static final String TAG = "MyProfileEditActivity";

    private ActivityMyProfileEditBinding binding;
    private UserViewModel userViewModel;
    private ProfileViewModel profileViewModel;
    private Bitmap bitmap;
    private File destination = null;
    private InputStream inputStreamImg;
    private String imgPath = null;
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
    private FragmentManager fragmentManager = null;
    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyProfileEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle(languageViewModel.getLanguageResponse().getValue().getEditProfile());

        setSupportActionBar(binding.header.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        fragmentManager = getSupportFragmentManager();

        userViewModel = ViewModelFactory.getInstance(this).create(UserViewModel.class);
        profileViewModel = ViewModelFactory.getInstance(this).create(ProfileViewModel.class);

        savedInstanceState = getIntent().getExtras();
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(Constant.Data.CURRENT_PHOTO_PATH)) {
                currentPhotoPath = savedInstanceState.getString(Constant.Data.CURRENT_PHOTO_PATH);
            }
        }

        setupView();


    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(Constant.Data.CURRENT_PHOTO_PATH, currentPhotoPath);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentPhotoPath = savedInstanceState.getString(Constant.Data.CURRENT_PHOTO_PATH);
    }

    private void onClick(View v) {

        if (v.getId() == binding.imageViewEdit.getId()) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);
                return;
            } else {
//                dispatchTakePictureIntent();
                selectImage();
            }

        } else if (v.getId() == binding.buttonSubmit.getId()) {

            String userid = userViewModel.getSignInResponse().getValue().getCustomerId();

            String firstName = binding.editTextFirstName.getText().toString();
            String lastName = binding.editTextLastName.getText().toString();
            String houseNo = binding.editTextHouseNo.getText().toString();
            String flatName = binding.editTextFlatName.getText().toString();
            String streetName = binding.editTextStreetName.getText().toString();
            String postalCode = binding.editTextPostalCode.getText().toString();
            String cityName = binding.editTextCityName.getText().toString();
            String mobileNo = binding.editTextMobileNo.getText().toString();

            try {

                File user_photo = null;
                if (currentPhotoPath != null && !currentPhotoPath.equals("")) {
                    user_photo = new File(currentPhotoPath);
                }

                if (firstName.equals("")) {
                    PopMessage.makeLongToast(this, languageViewModel.getLanguageResponse().getValue().getPleaseEnterFirstName());
                } else if (lastName.equals("")) {
                    PopMessage.makeLongToast(this, languageViewModel.getLanguageResponse().getValue().getPleaseEnterLastName());
                } else if (houseNo.equals("")) {
                    PopMessage.makeLongToast(this, languageViewModel.getLanguageResponse().getValue().getPleaseEnterHouseNo());
                }  else if (streetName.equals("")) {
                    PopMessage.makeLongToast(this, languageViewModel.getLanguageResponse().getValue().getPleaseEnterStreetName());
                } else if (postalCode.equals("")) {
                    PopMessage.makeLongToast(this, languageViewModel.getLanguageResponse().getValue().getPleaseEnterPostalCode());
                } else if (cityName.equals("")) {
                    PopMessage.makeLongToast(this, languageViewModel.getLanguageResponse().getValue().getPleaseEnterCity());
                } else if (mobileNo.equals("")) {
                    PopMessage.makeLongToast(this, languageViewModel.getLanguageResponse().getValue().getPleaseEnterMobileNo());
                } else {

                    profileViewModel.updateProfile(this, userid, firstName, lastName, mobileNo, houseNo, flatName, streetName, postalCode, cityName, user_photo,
                            new NetworkOperations(true));

                    profileViewModel.getUpdateProfileResponse().observe(this, updateProfileResponse -> {

                        if (updateProfileResponse != null) {

                            if (updateProfileResponse.getSuccess().equals(1L)) {

                                downloadUpdateUserDetails(userid);

                            } else {
                                showMessage("Alert", updateProfileResponse.getErrorMsg(), "OK", null, dialogInterface -> {
                                    dialogInterface.dismiss();
                                }, null);
                            }


                            profileViewModel.getUpdateProfileResponse().removeObservers(this);
                        }

                    });
                }
            } catch (Exception e) {
                Log.e(TAG, "onClick: Exception:", e);
            }

        }
    }

    private void downloadUpdateUserDetails(String userid) {

        profileViewModel.getProfile(this, Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, userid, new NetworkOperations(true));
        profileViewModel.getSignInResponse().observe(this, signInResponse -> {

            if (signInResponse != null) {
                if (signInResponse.getSuccess().equals(0L)) {
                    Toast.makeText(this, signInResponse.getSuccessMsg(), Toast.LENGTH_SHORT).show();
                    finish();
//                    showMessage("Alert", signInResponse.getSuccessMsg(), "OK", "Close", dialogInterface -> {
//                        userViewModel.setSignInResponse(signInResponse);
//                        profileViewModel.getSignInResponse().removeObservers(this);
//                        dialogInterface.dismiss();
//                        finish();
//                    }, dialogInterface -> {
//                        dialogInterface.dismiss();
//                    });
                } else {
                    showMessage("Alert", signInResponse.getErrorMsg(), "OK", null, dialogInterface -> {
                        dialogInterface.dismiss();
                    }, null);
                }
            }

        });

    }

    private void selectImage() {
        try {
            PackageManager pm = getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MyProfileEditActivity.this);
                builder.setTitle("Select Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            dialog.dismiss();
                            if (ActivityCompat.checkSelfPermission(MyProfileEditActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(MyProfileEditActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            dispatchTakePictureIntent();
                        } else if (options[item].equals("Choose From Gallery")) {
                            dialog.dismiss();
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else
                Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    @RequiresPermission(allOf = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    private void dispatchTakePictureIntent() {
        try {

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                photoFile = Utility.createImageFile(this);
                if (photoFile != null) {
                    currentPhotoPath = photoFile.getAbsolutePath();
                    Uri photoURI = FileProvider.getUriForFile(this, "com.harperskebab.fileprovider", photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, 1001);
                }
            }

        } catch (Exception e) {
            Log.e(TAG, "dispatchTakePictureIntent: Exception:", e);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            if (resultCode == Activity.RESULT_OK) {
                File file = new File(currentPhotoPath);
                if (file.exists()) {
                    Glide.with(this)
                            .load(file.getAbsolutePath())
                            .placeholder(R.drawable.user)
                            .circleCrop()
                            .into(binding.imageViewUser);
                }
            }
        } else if (requestCode == PICK_IMAGE_GALLERY) {
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                Log.e("Activity", "Pick from Gallery::>>> ");

                imgPath = getRealPathFromURI(selectedImage);
                destination = new File(imgPath.toString());
//                binding.imageViewUser.setImageBitmap(bitmap);
                currentPhotoPath=destination.getAbsolutePath();
                Glide.with(this)
                        .load(destination.getAbsolutePath())
                        .placeholder(R.drawable.user)
                        .circleCrop()
                        .into(binding.imageViewUser);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1001) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);
                return;
            }
//            dispatchTakePictureIntent();

        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupView() {

        userViewModel.getSignInResponse().observe(this, signInResponse -> {

            if (signInResponse != null) {
                if (signInResponse.getSuccess() == 0) {

                    Glide.with(this).load(signInResponse.getUserPhoto()).placeholder(R.drawable.user).into(binding.imageViewUser);

                    binding.editTextFirstName.setText(signInResponse.getFirstName());
                    binding.editTextLastName.setText(signInResponse.getLastName());
                    binding.editTextHouseNo.setText(signInResponse.getCustomerFloorHouseNumber());
                    binding.editTextFlatName.setText(signInResponse.getCustomerFlatName());
                    binding.editTextStreetName.setText(signInResponse.getCompanyStreet());
                    binding.editTextPostalCode.setText(signInResponse.getUserPostcode());
                    binding.editTextCityName.setText(signInResponse.getUserCity());
                    binding.editTextMobileNo.setText(signInResponse.getUserPhone());
                }
            }

        });

        binding.imageViewEdit.setOnClickListener(this::onClick);
        binding.buttonSubmit.setOnClickListener(this::onClick);

    }

}