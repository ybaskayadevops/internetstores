package com.gmbh.internetstores.frameworks.ui.base;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;

public interface BaseInterface {


  void setWaLifeCycle(LifeCycle waLifecycle);

  void loadActivityForResult(Class activityClass, Bundle bundle, int requestCode);

  void loadActivityForResult(Class activityClass, Bundle bundle, int requestCode, boolean single);

  void loadActivity(Class activityClass, Bundle bundle);

  void loadActivity(Class activityClass, Bundle bundle, Uri data);

  void loadActivity(Class activityClass, Bundle bundle, Uri data, boolean single);

  void loadActivityForResult(Class activityClass, int requestCode);

  void loadActivity(Class activityClasss);

  void activityAnimation(int enterAnim, int exitAnim);

  Context getContext();

  void onBackPressed();

  boolean isFragment();

  boolean isActivity();

  boolean isActive();

  Resources getResources();

  void setWaResultListener(ResultListener waResultListener);

  void requestAppPermissions(String[] permissions,
      PermissionResultListener waPermissionResultListener);
}
