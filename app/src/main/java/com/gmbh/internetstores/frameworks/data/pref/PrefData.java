package com.gmbh.internetstores.frameworks.data.pref;

import android.content.Context;
import android.content.SharedPreferences;
import com.gmbh.internetstores.MvvmApp;
import com.gmbh.internetstores.frameworks.data.model.ISBikesData;
import com.gmbh.internetstores.frameworks.utils.CommonUtils;
import com.gmbh.internetstores.frameworks.utils.Utils;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public final class PrefData {

  public static final String PREF_NAME = "internetstores_pref";
  public static final String SEED_DATABASE_DATA = "seed/ISBikesData.json";

  public static void savedBikeList(List<ISBikesData> bikeList, Context context) {
    String bikeListStr = Utils.getGson().toJson(bikeList);
    SharedPreferences settings = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = settings.edit();
    if (editor != null) {
      editor.putString("bikeslist", bikeListStr);
      editor.apply();
    }
  }

  public static List<ISBikesData> getBikeList(Context context) {
    List<ISBikesData> list = new ArrayList<>();
    if (context != null) {
      SharedPreferences settings =
          context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

      String json = settings.getString("bikeslist", "");
      if (!json.isEmpty()) {
        Type type = new TypeToken<List<ISBikesData>>() {
        }.getType();
        list = Utils.getGson().fromJson(json, type);
      }
    }
    return list;
  }

  public static List<ISBikesData> updateBikeList(List<ISBikesData> list, ISBikesData model) {

    for (int i = 0; i < list.size(); i++) {
      if (list.get(i).getId().equals(model.getId())) {
        list.get(i).setCategory(model.getCategory());
        list.get(i).setLocation(model.getLocation());
        list.get(i).setName(model.getName());
        list.get(i).setPriceRange(model.getPriceRange());
        list.get(i).setFrameSize(model.getFrameSize());
        list.get(i).setPhotoUrl(model.getPhotoUrl());
        list.get(i).setDescription(model.getDescription());
      }
    }
    return list;
  }

  public static List<ISBikesData> deleteBikeList(List<ISBikesData> list, ISBikesData model) {

    List<ISBikesData> listGecici=new ArrayList<>();
    for (int i = 0; i < list.size(); i++) {
      if (!list.get(i).getId().equals(model.getId())) {
        listGecici.add(list.get(i));
      }
    }
    return listGecici;
  }


  public static void setSeedData() {

    try {
      Type type = new TypeToken<List<ISBikesData>>() {
      }.getType();
      List<ISBikesData> isBikesDataList = Utils.getGson().
          fromJson(CommonUtils
              .loadJSONFromAsset(MvvmApp.getApplication(), PrefData.SEED_DATABASE_DATA), type);

      if (getBikeList(MvvmApp.getApplication()).size() == 0) {
        savedBikeList(isBikesDataList, MvvmApp.getApplication());
      }

    } catch (IOException ex) {

    }
  }

  public static int getID(List<ISBikesData> list) {
    return list.size() + 1;
  }


}
