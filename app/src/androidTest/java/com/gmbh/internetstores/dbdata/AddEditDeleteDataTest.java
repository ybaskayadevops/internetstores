package com.gmbh.internetstores.dbdata;

import android.content.Context;
import android.content.Intent;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.gmbh.internetstores.MvvmApp;
import com.gmbh.internetstores.frameworks.data.model.ISBikesData;
import com.gmbh.internetstores.frameworks.data.pref.PrefData;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class AddEditDeleteDataTest {

  @Test
  public void useAppContext() {
    // Context of the app under test.
    Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

    assertEquals("com.gmbh.internetstores", appContext.getPackageName());
  }

  @Test
  public void addNewBike() {

    Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();;
    List<ISBikesData> bikeList = PrefData.getBikeList(appContext);
    int beforeCount, afterCount = 0;
    beforeCount = bikeList.size();
    ISBikesData model = new ISBikesData();
    model.setId(PrefData.getID(bikeList));
    model.setCategory("Commuting");
    model.setLocation("Stuttgart");
    model.setName("Cube Town Hybrid Pro 500");
    model.setDescription(
        "Great bike for everyday usage, especially for commuting to work or doing grocery shopping.");
    model.setPhotoUrl(
        "https://images.internetstores.de/products//1066124/02/98ba28/Cube_Town_Hybrid_Pro_500_Easy_Entry_black_n_green[640x480].jpg?forceSize=true&forceAspectRatio=true&useTrim=true");
    model.setFrameSize("S");
    model.setPriceRange("NORMAL");
    bikeList.add(model);
    PrefData.savedBikeList(bikeList, MvvmApp.getApplication());

    afterCount = PrefData.getBikeList(appContext).size();

    assertEquals(beforeCount + 1, afterCount);
  }
  @Test
  public void updateNewBike() {
    Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    List<ISBikesData> bikeList = PrefData.getBikeList(appContext);

    if(bikeList.size()>0) {
      ISBikesData model = bikeList.get(0);
      model.setCategory("Commuting");
      model.setLocation("Stuttgart");
      model.setName("Cube Town Hybrid Pro 500");
      model.setDescription(
          "Great bike for everyday usage, especially for commuting to work or doing grocery shopping.");
      model.setPhotoUrl(
          "https://images.internetstores.de/products//1066124/02/98ba28/Cube_Town_Hybrid_Pro_500_Easy_Entry_black_n_green[640x480].jpg?forceSize=true&forceAspectRatio=true&useTrim=true");
      model.setFrameSize("S");
      model.setPriceRange("NORMAL");

      bikeList = PrefData.updateBikeList(bikeList, model);
      PrefData.savedBikeList(bikeList, MvvmApp.getApplication());

      assertEquals(bikeList.get(0).getCategory(), model.getCategory());
    }
  }

  @Test
  public void deleteNewBike() {
    Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

    int beforeCount, afterCount = 0;
    List<ISBikesData> bikeList = PrefData.getBikeList(appContext);
    beforeCount = bikeList.size();

    if (bikeList.size() > 0) {
      ISBikesData bikeModel = bikeList.get(0);
      bikeList = PrefData.deleteBikeList(bikeList, bikeModel);
      afterCount = bikeList.size();
    }

    assertEquals(beforeCount-1, afterCount);
  }

}
