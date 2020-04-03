package com.gmbh.internetstores.frameworks.ui.deletebike;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.AppCompatButton;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.gmbh.internetstores.MvvmApp;
import com.gmbh.internetstores.R;
import com.gmbh.internetstores.frameworks.data.model.ISBikesData;
import com.gmbh.internetstores.frameworks.data.pref.PrefData;
import com.gmbh.internetstores.frameworks.ui.base.BaseActivity;
import com.gmbh.internetstores.frameworks.ui.base.BaseInterface;
import com.gmbh.internetstores.frameworks.ui.editbike.EditBikeActivity;
import com.gmbh.internetstores.frameworks.utils.Constants;
import com.gmbh.internetstores.frameworks.utils.Utils;
import java.util.List;

public class DeleteBikeActivity extends BaseActivity {


  public static String ARG_IS_BIKES_DATA = "ISBikesData";


  ImageView imageViewBike, imageViewBack;
  TextView textViewName, textViewCategory, textViewFrameSize, textViewPriceRange,
      textViewLocation, textViewDescription;
  AppCompatButton buttonDelete;
  ISBikesData bikeModel;
  RequestOptions requestOptions =
      new RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);


  public static void load(BaseInterface waBaseInterface, ISBikesData model) {
    Bundle bundle = new Bundle();
    bundle.putString(DeleteBikeActivity.ARG_IS_BIKES_DATA, Utils.getGson().toJson(model));
    waBaseInterface.loadActivity(DeleteBikeActivity.class, bundle);
  }


  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_delete_bike);

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
    buttonDelete = findViewById(R.id.buttonDelete);


  }

  public void setOnclickListener() {

    imageViewBack.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        closeActivity();
      }
    });

    buttonDelete.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        showAlertDialog();
      }
    });
  }

  public void showAlertDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);

    builder.setTitle("Delete");
    builder.setMessage("Do you want delete this bike?");

    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

      public void onClick(DialogInterface dialog, int which) {
        // Do do my action here
        List<ISBikesData> bikeList = PrefData.getBikeList(getContext());
        bikeList = PrefData.deleteBikeList(bikeList, bikeModel);
        PrefData.savedBikeList(bikeList, MvvmApp.getApplication());

        Toast.makeText(DeleteBikeActivity.this,
            getString(R.string.success), Toast.LENGTH_SHORT).show();
        dialog.dismiss();
        finish();


      }

    });

    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

      @Override
      public void onClick(DialogInterface dialog, int which) {

        dialog.dismiss();
      }
    });

    AlertDialog alert = builder.create();
    alert.show();

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
