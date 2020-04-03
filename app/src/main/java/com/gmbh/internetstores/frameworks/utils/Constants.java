package com.gmbh.internetstores.frameworks.utils;

import static com.gmbh.internetstores.frameworks.data.pref.PrefData.PREF_NAME;

import android.content.Context;
import android.content.SharedPreferences;
import com.gmbh.internetstores.frameworks.data.model.ISBikesData;
import com.gmbh.internetstores.frameworks.data.pref.PrefData;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;


public class Constants {

  public static int REQUEST_PROFILE_CAMERA_GALLERY = 722;
  public static final int PERMISSIONS_REQUEST_LOCATION = 65;
  public static final int PERMISSIONS_REQUEST_SHOT = 66;
  public static int GALLERY = 697;
  public static int PROFIL_UPDATED = 702;
  public static String[] FrameSizeArray = new String[]{"S", "M", "L"};
  public static String[] PriceRangeArray = new String[]{"C", "N", "E"};

  public static ISBikesData getBikeModel(Context context, String json) {

    ISBikesData bikeModel = null;

    if (!json.isEmpty()) {
      Type type = new TypeToken<ISBikesData>() {
      }.getType();
      bikeModel = Utils.getGson().fromJson(json, type);
    }
    return bikeModel;
  }




}
