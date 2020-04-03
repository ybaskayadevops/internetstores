package com.gmbh.internetstores.frameworks.utils;

import com.gmbh.internetstores.frameworks.data.model.ISBikesData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;

public class Utils {

  public static Gson getGson() {
    GsonBuilder gsonBuilder = new GsonBuilder();
    return gsonBuilder.create();
  }

  public static int getPosition(String[] array, String text) {
    int position = 0;
    for (int i = 0; i < array.length; i++) {
      if (array[i].equals(text)) {
        position = i;
      }
    }

    return position;

  }


}
