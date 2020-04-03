package com.gmbh.internetstores.frameworks.ui.detailbike;

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

public class DetailBikeActivity extends BaseActivity {


  public static String ARG_IS_BIKES_DATA = "ISBikesData";


  ImageView imageViewBike, imageViewBack;
  TextView textViewName, textViewCategory, textViewFrameSize, textViewPriceRange,
      textViewLocation, textViewDescription;
  ISBikesData bikeModel;
  RequestOptions requestOptions =
      new RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);


  public static void load(BaseInterface waBaseInterface, ISBikesData model) {
    Bundle bundle = new Bundle();
    bundle.putString(DetailBikeActivity.ARG_IS_BIKES_DATA, Utils.getGson().toJson(model));
    waBaseInterface.loadActivity(DetailBikeActivity.class, bundle);
  }


  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail_bike);

    setUI();
    setOnclickListener();
    String modelStr = getIntent().getStringExtra(ARG_IS_BIKES_DATA);
    bikeModel = Constants.getBikeModel(MvvmApp.getApplication(), modelStr);
    setData();


  }

  public void setUI() {
    imageViewBike = findViewById(R.id.imageViewBike);
    imageViewBack = findViewById(R.id.imageViewBack);
    textViewName = findViewById(R.id.textViewName);
    textViewCategory = findViewById(R.id.textViewCategory);
    textViewFrameSize = findViewById(R.id.textViewFrameSize);
    textViewPriceRange = findViewById(R.id.textViewPriceRange);
    textViewLocation = findViewById(R.id.textViewLocation);
    textViewDescription = findViewById(R.id.textViewDescription);

  }

  public void setOnclickListener() {

    imageViewBack.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        closeActivity();
      }
    });
  }

  public void setData() {
    textViewCategory.setText(bikeModel.getCategory());
    textViewLocation.setText(bikeModel.getLocation());
    textViewName.setText(bikeModel.getName());
    textViewDescription.setText(bikeModel.getDescription());
    if (bikeModel.getPhotoUrl() != null) {
      Glide.with(imageViewBike.getContext()).
          load(bikeModel.getPhotoUrl()).apply(requestOptions)
          .into(imageViewBike);

    }

    textViewFrameSize.setText(bikeModel.getFrameSize());
    textViewPriceRange.setText(bikeModel.getPriceRange());

  }

  private void closeActivity() {
    finish();
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    closeActivity();
  }
}
