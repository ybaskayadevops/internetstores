package com.gmbh.internetstores.frameworks.ui.addbike;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.gmbh.internetstores.MvvmApp;
import com.gmbh.internetstores.R;
import com.gmbh.internetstores.frameworks.data.model.ISBikesData;
import com.gmbh.internetstores.frameworks.data.pref.PrefData;
import com.gmbh.internetstores.frameworks.ui.addbike.validator.AddBikeValidator;
import com.gmbh.internetstores.frameworks.ui.base.BaseActivity;
import com.gmbh.internetstores.frameworks.ui.base.BaseInterface;
import com.gmbh.internetstores.frameworks.ui.camera.CameraActivity;
import com.gmbh.internetstores.frameworks.utils.Constants;
import com.gmbh.internetstores.frameworks.utils.MediaFileUtils.MediaSource;
import com.gmbh.internetstores.frameworks.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView.CropShape;
import de.hdodenhof.circleimageview.CircleImageView;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class AddBikeActivity extends BaseActivity implements View.OnClickListener {

  public static String ARG_OPEN_PROFILE_CAMERA = "openProfileCamera";

  private boolean isSucces = false;
  private MediaSource mediaSource;
  private ImageView imageViewBike,imageViewBack;
  private TextView textViewAddPhoto;

  private String previewFilePath = null;
  private int mWidth, mHeight;


  EditText editTextCategory, editTextLocation, editTextName, editTextDescription;
  RadioGroup radioGroupFrameSize, radioGroupPriceRange;
  AppCompatButton buttonAdd;


  public static void load(BaseInterface waBaseInterface, boolean openCamera, int requestCode) {
    Bundle bundle = new Bundle();
    bundle.putBoolean(AddBikeActivity.ARG_OPEN_PROFILE_CAMERA, openCamera);
    waBaseInterface.loadActivityForResult(AddBikeActivity.class, bundle, requestCode, true);
  }


  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_bike);

    setUI();
    setOnClickListener();

    textViewAddPhoto.setText(getResources().getString(R.string.add_picture));

    boolean openCamera = getIntent().getBooleanExtra(ARG_OPEN_PROFILE_CAMERA, false);
    if (openCamera) {
      openProfileCamera();
    }

  }

  public void setUI() {
    imageViewBike = findViewById(R.id.imageViewBike);
    imageViewBack=findViewById(R.id.imageViewBack);
    textViewAddPhoto = findViewById(R.id.textViewAddPhoto);
    editTextCategory = findViewById(R.id.editTextCategory);
    editTextLocation = findViewById(R.id.editTextLocation);
    editTextName = findViewById(R.id.editTextName);
    editTextDescription = findViewById(R.id.editTextDescription);
    radioGroupFrameSize = findViewById(R.id.radioGroupFrameSize);
    radioGroupPriceRange = findViewById(R.id.radioGroupPriceRange);
    buttonAdd = findViewById(R.id.buttonAdd);
  }

  public void setOnClickListener() {

    textViewAddPhoto.setOnClickListener(this);

    buttonAdd.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        int radioButtonFrameSizeSelectedId = radioGroupFrameSize.getCheckedRadioButtonId();
        int radioButtonPriceRangeSelectedId = radioGroupPriceRange.getCheckedRadioButtonId();

        // find the radiobutton by returned id
        RadioButton radioButtonFrameSize = findViewById(radioButtonFrameSizeSelectedId);
        RadioButton radioButtonPriceRange = findViewById(radioButtonPriceRangeSelectedId);

        String errorMessage = AddBikeValidator.
            addBikeValidator(editTextCategory, editTextLocation, editTextName,
                editTextDescription, radioButtonFrameSize, radioButtonPriceRange, previewFilePath);

        if (errorMessage.isEmpty()) {

          String selectedFrameSize = radioButtonFrameSize.getTag().toString();
          String selectedPriceRange = radioButtonPriceRange.getTag().toString();
          List<ISBikesData> bikeList = PrefData.getBikeList(getContext());

          ISBikesData model = new ISBikesData();
          model.setId(PrefData.getID(bikeList));
          model.setCategory(editTextCategory.getText().toString());
          model.setLocation(editTextLocation.getText().toString());
          model.setName(editTextName.getText().toString());
          model.setDescription(editTextDescription.getText().toString());
          model.setPhotoUrl(previewFilePath);
          model.setFrameSize("" + selectedFrameSize.charAt(0));
          model.setPriceRange("" + selectedPriceRange.charAt(0));

          bikeList.add(model);

          PrefData.savedBikeList(bikeList, MvvmApp.getApplication());

          Toast.makeText(AddBikeActivity.this,
              getString(R.string.success), Toast.LENGTH_SHORT).show();
          finish();

        } else {
          Toast.makeText(AddBikeActivity.this,
              errorMessage, Toast.LENGTH_SHORT).show();
        }

      }
    });

    imageViewBack.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        closeActivity();
      }
    });
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    beginCropResult(requestCode, resultCode, data);
    handleCropResult(requestCode, resultCode, data);
  }

  private void beginCropResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == Constants.REQUEST_PROFILE_CAMERA_GALLERY && resultCode == RESULT_OK) {
      if (data != null) {
        previewFilePath = data.getStringExtra("path");
        mediaSource = (MediaSource) data.getSerializableExtra("mediaSource");
        mWidth = data.getIntExtra("width", 0);
        mHeight = data.getIntExtra("height", 0);
        if (previewFilePath != null && mediaSource != null) {

          Uri source = FileProvider
              .getUriForFile(AddBikeActivity.this,
                  getApplicationContext().getPackageName() + ".provider",
                  new File(previewFilePath));
          CropImage.activity(source).setCropShape(
              Build.VERSION.SDK_INT >= Build.VERSION_CODES.P ? CropShape.RECTANGLE
                  : CropShape.OVAL).setAspectRatio(1, 1)
              .start(this);
          return;
        }
      }

    }
  }

  private void handleCropResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
      if (resultCode == RESULT_OK) {
        previewFilePath = CropImage.getActivityResult(data).getUri().getPath();
        setImageToUI();
      }
    }
  }


  private void setImageToUI() {
    if (previewFilePath != null) {
      Glide.with(AddBikeActivity.this).load(previewFilePath)
          .apply(new RequestOptions().skipMemoryCache(true)
              .diskCacheStrategy(DiskCacheStrategy.NONE)).into(imageViewBike);
    }
  }


  private void openProfileCamera() {
    CameraActivity.loadProfileCamera(AddBikeActivity.this);
  }

  private void closeActivity() {
    finish();
  }

  @Override
  public void onBackPressed() {
    if (isSucces) {
      setResult(RESULT_OK);
    }

    super.onBackPressed();
    closeActivity();
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.textViewAddPhoto:
        openProfileCamera();
        break;
    }
  }
}
