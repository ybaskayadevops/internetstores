package com.gmbh.internetstores.frameworks.ui.editbike;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.FileProvider;
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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView.CropShape;
import java.io.File;
import java.util.List;

public class EditBikeActivity extends BaseActivity implements View.OnClickListener {

  public static String ARG_OPEN_PROFILE_CAMERA = "openProfileCamera";
  public static String ARG_IS_BIKES_DATA = "ISBikesData";

  private boolean isSucces = false;
  private MediaSource mediaSource;
  private ImageView imageViewBike,imageViewBack;
  private TextView textViewAddPhoto;

  private String previewFilePath = null;
  private int mWidth, mHeight;


  EditText editTextCategory, editTextLocation, editTextName, editTextDescription;
  RadioGroup radioGroupFrameSize, radioGroupPriceRange;
  AppCompatButton buttonEdit;
  ISBikesData bikeModel;
  RequestOptions requestOptions =
      new RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);


  public static void load(BaseInterface waBaseInterface, boolean openCamera, int requestCode,
      ISBikesData model) {
    Bundle bundle = new Bundle();
    bundle.putBoolean(EditBikeActivity.ARG_OPEN_PROFILE_CAMERA, openCamera);
    bundle.putString(EditBikeActivity.ARG_IS_BIKES_DATA, Utils.getGson().toJson(model));
    waBaseInterface.loadActivityForResult(EditBikeActivity.class, bundle, requestCode, true);
  }


  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit_bike);

    setUI();
    setOnClickListener();

    textViewAddPhoto.setText(getResources().getString(R.string.add_picture));

    boolean openCamera = getIntent().getBooleanExtra(ARG_OPEN_PROFILE_CAMERA, false);
    String modelStr = getIntent().getStringExtra(ARG_IS_BIKES_DATA);
    bikeModel = Constants.getBikeModel(MvvmApp.getApplication(), modelStr);
    setData();
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
    buttonEdit = findViewById(R.id.buttonEdit);
  }

  public void setData() {
    editTextCategory.setText(bikeModel.getCategory());
    editTextLocation.setText(bikeModel.getLocation());
    editTextName.setText(bikeModel.getName());
    editTextDescription.setText(bikeModel.getDescription());
    if (bikeModel.getPhotoUrl() != null) {
      Glide.with(imageViewBike.getContext()).
          load(bikeModel.getPhotoUrl()).apply(requestOptions)
          .into(imageViewBike);
      previewFilePath = bikeModel.getPhotoUrl();

    }

    int radioBottonFrameSizeID = radioGroupFrameSize
        .getChildAt(Utils.getPosition(Constants.FrameSizeArray, bikeModel.getFrameSize())).getId();

    radioGroupFrameSize.check(radioBottonFrameSizeID);

    int radioButtonPriceRangeID = radioGroupPriceRange
        .getChildAt(Utils.getPosition(Constants.PriceRangeArray, bikeModel.getPriceRange())).getId();

    radioGroupPriceRange.check(radioButtonPriceRangeID);


  }

  public void setOnClickListener() {

    textViewAddPhoto.setOnClickListener(this);

    buttonEdit.setOnClickListener(new View.OnClickListener() {
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

          bikeModel.setCategory(editTextCategory.getText().toString());
          bikeModel.setLocation(editTextLocation.getText().toString());
          bikeModel.setName(editTextName.getText().toString());
          bikeModel.setDescription(editTextDescription.getText().toString());
          bikeModel.setPhotoUrl(previewFilePath);
          bikeModel.setFrameSize("" + selectedFrameSize.charAt(0));
          bikeModel.setPriceRange("" + selectedPriceRange.charAt(0));

          bikeList = PrefData.updateBikeList(bikeList, bikeModel);
          PrefData.savedBikeList(bikeList, MvvmApp.getApplication());

          Toast.makeText(EditBikeActivity.this,
              getString(R.string.success), Toast.LENGTH_SHORT).show();
          finish();

        } else {
          Toast.makeText(EditBikeActivity.this,
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
              .getUriForFile(EditBikeActivity.this,
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
      Glide.with(EditBikeActivity.this).load(previewFilePath)
          .apply(new RequestOptions().skipMemoryCache(true)
              .diskCacheStrategy(DiskCacheStrategy.NONE)).into(imageViewBike);
    }
  }


  private void openProfileCamera() {
    CameraActivity.loadProfileCamera(EditBikeActivity.this);
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
