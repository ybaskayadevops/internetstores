
package com.gmbh.internetstores;

import android.app.Application;
import com.gmbh.internetstores.frameworks.data.pref.PrefData;


public class MvvmApp extends Application {


  private static Application waApplication;

  public static Application getApplication() {
    return waApplication;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    waApplication = this;
    PrefData.setSeedData();
  }
}
