package com.gmbh.internetstores.frameworks.ui.camera;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.gmbh.internetstores.R;
import com.gmbh.internetstores.frameworks.ui.base.BaseActivity;
import com.gmbh.internetstores.frameworks.ui.base.BaseInterface;
import com.gmbh.internetstores.frameworks.utils.Constants;
import com.gmbh.internetstores.frameworks.utils.MediaFileUtils;
import com.gmbh.internetstores.frameworks.utils.MediaFileUtils.MediaSource;
import com.gmbh.internetstores.frameworks.utils.PermissionChecker;
import com.makeramen.roundedimageview.RoundedImageView;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.FileCallback;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.controls.Flash;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class CameraActivity extends BaseActivity implements View.OnClickListener {

  private CameraView cameraView;
  private RelativeLayout rlCamera;
  private LinearLayout llPermission;

  private RoundedImageView ivCaptureImage;
  private RoundedImageView ivClose;
  private RoundedImageView ivFlash;
  private RoundedImageView ivTurnCamera;
  private RoundedImageView ivGallery;
  private TextView tvOpen;
  private String cameraMode;
  private final static String CAMERA_MODE = "camera_mode";
  private final static String MODE_PROFILE = "profile_mode";
  private final static String MODE_PHOTO_CHECKPOINT = "photo_checkpoint_mode";


  public static void loadProfileCamera(BaseInterface waBaseActivity) {
    Bundle bundle = new Bundle();
    bundle.putString(CAMERA_MODE, MODE_PROFILE);
    waBaseActivity.loadActivityForResult(CameraActivity.class, bundle,
        Constants.REQUEST_PROFILE_CAMERA_GALLERY, true);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_camera);

    cameraMode = getIntent().getStringExtra(CAMERA_MODE);

    cameraView = findViewById(R.id.camera);
    if (checkPermission()) {
      cameraView.setLifecycleOwner(this);
    }
    rlCamera = findViewById(R.id.rl_camera_edit);
    llPermission = findViewById(R.id.ll_permission);
    llPermission.setVisibility(checkPermission() ? View.GONE : View.VISIBLE);
    rlCamera.setVisibility(checkPermission() ? View.VISIBLE : View.GONE);

    ivCaptureImage = findViewById(R.id.iv_take_photo);
    ivCaptureImage.setOnClickListener(this);
    ivClose = findViewById(R.id.iv_close);
    ivClose.setOnClickListener(this);
    ivFlash = findViewById(R.id.iv_flash_edit);
    ivFlash.setOnClickListener(this);
    ivTurnCamera = findViewById(R.id.turn_camera_edit);
    ivTurnCamera.setOnClickListener(this);
    ivGallery = findViewById(R.id.iv_gallery);

    if (cameraMode.equalsIgnoreCase(MODE_PROFILE)) {
      ivGallery.setOnClickListener(this);
    }

    tvOpen = findViewById(R.id.btn_open);
    tvOpen.setOnClickListener(this);
    tvOpen.setText(getResources().getString(R.string.open));

    cameraView.addCameraListener(new CameraListener() {
      @Override
      public void onPictureTaken(final PictureResult result) {
        super.onPictureTaken(result);

        //Save original image to file
        File outputFile = null;
        if (cameraMode.equalsIgnoreCase(MODE_PROFILE)) {
          outputFile = MediaFileUtils.getProfileCameraImage();
        }

        result.toFile(outputFile, new FileCallback() {
          @Override
          public void onFileReady(@Nullable File file) {

            if (file != null) {
              setResult(file.getAbsolutePath(),MediaSource.CAMERA,
                  result.getSize().getWidth(), result.getSize().getHeight());
            } else {

            }
          }
        });
      }
    });
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (checkPermission()) {
      rlCamera.setVisibility(View.VISIBLE);
      llPermission.setVisibility(View.GONE);
      cameraView.setLifecycleOwner(this);
    } else {
      rlCamera.setVisibility(View.GONE);
      llPermission.setVisibility(View.VISIBLE);
    }
  }

  private void takePhoto() {
    if (!cameraView.isTakingPicture()) {
      cameraView.takePicture();
    }
  }

  private void openGallery() {
    Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
    getIntent.setType("image/*");
    Intent pickIntent = new Intent(Intent.ACTION_PICK,
        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    pickIntent
        .setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
    Intent chooserIntent = Intent.createChooser(getIntent, getString(R.string.select_image));
    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
    chooserIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

    startActivityForResult(chooserIntent, Constants.GALLERY);
  }

  private String getPathFromInputStreamUri(Context context, Uri uri) {
    InputStream inputStream = null;
    String filePath = null;

    if (uri.getAuthority() != null) {
      try {
        inputStream = context.getContentResolver().openInputStream(uri);
        File photoFile = createTemporalFileFrom(inputStream);

        filePath = photoFile.getAbsolutePath();

      } catch (Exception e) {

      } finally {
        try {
          if (inputStream != null) {
            inputStream.close();
          }
        } catch (Exception e) {

        }
      }
    }

    return filePath;
  }

  private File createTemporalFileFrom(InputStream inputStream) throws IOException {
    File targetFile = null;

    if (inputStream != null) {
      int read;
      byte[] buffer = new byte[8 * 1024];

      targetFile = MediaFileUtils.getProfileCameraImage();
      OutputStream outputStream = new FileOutputStream(targetFile);

      while ((read = inputStream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, read);
      }
      outputStream.flush();

      try {
        outputStream.close();
      } catch (Exception e) {
      }
    }

    return targetFile;
  }

  private int rotationAngle = 0;

  @Override
  public void onClick(View v) {

    int vId = v.getId();
    switch (vId) {
      case R.id.iv_close:
        finish();
        break;
      case R.id.iv_take_photo:
        takePhoto();
        break;
      case R.id.iv_flash_edit:
        if (cameraView.isTakingPicture()) {
          return;
        }
        setFlashButton(cameraView.getFlash());
        break;
      case R.id.turn_camera_edit:
        if (cameraView.isTakingPicture()) {
          return;
        }
        ivTurnCamera.animate()
            .rotationY((rotationAngle += 180) % 360)
            .setDuration(500).start();
        cameraView.toggleFacing();
        break;
      case R.id.iv_gallery:
        if (cameraView.isTakingPicture()) {
          return;
        }
        openGallery();
        break;
      case R.id.btn_open:
        requestPermission();
        break;
      default:
        break;
    }
  }

  private void setResult(String imagePath, MediaSource mediaSource,
      int width,
      int height) {
    setResult(RESULT_OK,
        new Intent().putExtra("path", imagePath).putExtra("mediaSource", mediaSource)
            .putExtra("width", width)
            .putExtra("height", height));
    finish();
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode != RESULT_CANCELED) {
      if (requestCode == Constants.GALLERY && resultCode == RESULT_OK && null != data) {
        Uri selectedImage = data.getData();
        String path = getPathFromInputStreamUri(this, selectedImage);
        //We don't need to check FileCheckSum for profile
        setResult(path, MediaSource.GALLERY, 0, 0);
      }
    }
  }

  private boolean checkPermission() {
    return PermissionChecker.checkProfilePermission();
  }

  private void requestPermission() {
    if (!PermissionChecker.checkProfilePermission()) {
      PermissionChecker.requestProfilePermission(this);
    }
  }


  private void setFlashButton(Flash flash) {
    switch (flash) {
      case OFF:
        ivFlash.setImageResource(R.drawable.flash_off);
        break;
      case ON:
        ivFlash.setImageResource(R.drawable.flash_on);
        break;
      case AUTO:
        ivFlash.setImageResource(R.drawable.flash_auto);
        break;
      case TORCH:
        ivFlash.setImageResource(R.drawable.flash_torch);
        break;
    }
  }

}
