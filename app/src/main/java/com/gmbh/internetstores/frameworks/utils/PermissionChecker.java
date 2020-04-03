package com.gmbh.internetstores.frameworks.utils;

import android.Manifest.permission;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.content.ContextCompat;
import com.gmbh.internetstores.MvvmApp;
import com.gmbh.internetstores.frameworks.ui.base.BaseInterface;

public class PermissionChecker {

  public static void requestLocationPermission(Activity activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      activity.requestPermissions(
          new String[]{
              android.Manifest.permission.ACCESS_COARSE_LOCATION,
              android.Manifest.permission.ACCESS_FINE_LOCATION
          },
          Constants.PERMISSIONS_REQUEST_LOCATION);
    }
  }


  public static boolean checkLocationPermission() {
    return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
        (ContextCompat.checkSelfPermission(MvvmApp.getApplication(),
            permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(MvvmApp.getApplication(),
            permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
  }


  public static void requestShotPermission(Activity activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      activity.requestPermissions(
          new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
              android.Manifest.permission.ACCESS_FINE_LOCATION,
              android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
              android.Manifest.permission.CAMERA,
              android.Manifest.permission.RECORD_AUDIO},
          Constants.PERMISSIONS_REQUEST_SHOT);
    }
  }

  public static boolean checkShotPermission() {
    return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
        (ContextCompat
            .checkSelfPermission(MvvmApp.getApplication(), permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(MvvmApp.getApplication(),
            permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(MvvmApp.getApplication(),
            permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(MvvmApp.getApplication(),
            permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(MvvmApp.getApplication(),
            permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED);
  }

  public static void requestProfilePermission(Activity activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      activity.requestPermissions(
          new String[]{
              android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
              android.Manifest.permission.CAMERA},
          Constants.PERMISSIONS_REQUEST_SHOT);
    }
  }

  public static boolean checkProfilePermission() {
    return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
        (ContextCompat
            .checkSelfPermission(MvvmApp.getApplication(), permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(MvvmApp.getApplication(),
            permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
  }
}
