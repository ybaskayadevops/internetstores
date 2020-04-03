package com.gmbh.internetstores.frameworks.ui.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;

public abstract class BaseActivity extends AppCompatActivity implements BaseInterface,
    LifecycleOwner {

  private LifeCycle waLifeCycle;
  private ResultListener waResultListener;
  private PermissionResultListener waPermissionResultListener;

  public void setWaLifeCycle(LifeCycle waLifeCycle) {
    this.waLifeCycle = waLifeCycle;
  }

  public void setWaResultListener(ResultListener waResultListener) {
    this.waResultListener = waResultListener;
  }



  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  protected void onStart() {
    super.onStart();
    if (waLifeCycle != null) {
      waLifeCycle.onStarted();
    }
  }

  @Override
  protected void onResume() {

    super.onResume();
    if (waLifeCycle != null) {
      waLifeCycle.onResumed();
    }
  }

  @Override
  public Resources getResources() {
    return super.getResources();
  }

  @Override
  protected void onPause() {

    super.onPause();
    if (waLifeCycle != null) {
      waLifeCycle.onPaused();
    }
  }

  @Override
  protected void onStop() {
    super.onStop();
    if (waLifeCycle != null) {
      waLifeCycle.onStopped();
    }

  }

  @Override
  protected void onDestroy() {

    super.onDestroy();
    if (waLifeCycle != null) {
      waLifeCycle.onDestroyed();
    }
  }

  @NonNull
  public Context getContext() {
    return this;
  }


  @Override
  public void loadActivityForResult(Class activityClass, int requestCode) {
    loadActivityForResult(activityClass, null, requestCode);
  }

  @Override
  public void loadActivityForResult(Class activityClass, Bundle bundle, int requestCode) {
    loadActivityForResult(activityClass, bundle, requestCode, false);
  }

  @Override
  public void loadActivityForResult(Class activityClass, Bundle bundle, int requestCode,
      boolean singleTop) {
    Intent i = new Intent(getContext(), activityClass);
    if (singleTop) {
      i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }
    if (bundle != null) {
      i.putExtras(bundle);
    }
    startActivityForResult(i, requestCode);
    //overridePendingTransition(R.anim.slide_up, R.anim.stay);
  }

  @Override
  public void loadActivity(Class activityClass) {
    loadActivity(activityClass, null, null);
  }

  @Override
  public void loadActivity(Class activityClass, Bundle bundle) {
    loadActivity(activityClass, bundle, null);
  }

  @Override
  public void loadActivity(Class activityClass, Bundle bundle, Uri data) {
    loadActivity(activityClass, bundle, data, false);
  }

  @Override
  public void loadActivity(Class activityClass, Bundle bundle, Uri data, boolean single) {
    Intent i = new Intent(getContext(), activityClass);
    if (single) {
      i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    }
    if (data != null) {
      i.setData(data);
    }
    if (bundle != null) {
      i.putExtras(bundle);
    }
    startActivity(i);
    //activityAnimation(R.anim.slide_up, R.anim.stay);
  }

  @Override
  public void activityAnimation(int enterAnim, int exitAnim) {
    overridePendingTransition(enterAnim, exitAnim);
  }

  @Override
  public boolean isActivity() {
    return true;
  }

  @Override
  public boolean isFragment() {
    return false;
  }

  @Override
  public boolean isActive() {
    return !isFinishing();
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode != Activity.RESULT_CANCELED && data != null) {
      if (waResultListener != null) {
        waResultListener.onSuccess(requestCode, data);
      }
    } else {
      if (waResultListener != null) {
        waResultListener.onCancel();
      }
    }
  }

  private int permissionRequestCode = 976;

  public void requestAppPermissions(String[] permissions,
      PermissionResultListener waPermissionResultListener) {
    this.waPermissionResultListener = waPermissionResultListener;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      requestPermissions(permissions, permissionRequestCode);
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == permissionRequestCode) {
      if (grantResults.length > 0) {
        for (int i = 0; i < grantResults.length; i++) {
          if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
            if (waPermissionResultListener != null) {
              waPermissionResultListener.onDenied();
            }
            return;
          }
        }
        if (waPermissionResultListener != null) {
          waPermissionResultListener.onAccepted();
        }
      } else {
        if (waPermissionResultListener != null) {
          waPermissionResultListener.onDenied();
        }
      }

    }
  }
  public static class EmptyMessage {

  }
}
