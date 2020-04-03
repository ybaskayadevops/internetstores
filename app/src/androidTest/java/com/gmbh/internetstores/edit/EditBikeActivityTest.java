package com.gmbh.internetstores.edit;


import android.content.Context;
import android.content.Intent;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.gmbh.internetstores.frameworks.data.model.ISBikesData;
import com.gmbh.internetstores.frameworks.data.pref.PrefData;
import com.gmbh.internetstores.frameworks.ui.detailbike.DetailBikeActivity;
import com.gmbh.internetstores.frameworks.ui.editbike.EditBikeActivity;
import com.gmbh.internetstores.frameworks.utils.Utils;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class EditBikeActivityTest {

  @Rule
  public ActivityTestRule<EditBikeActivity> activityRule
      = new ActivityTestRule<>(
      EditBikeActivity.class,
      true,     // initialTouchMode
      false);   // launchActivity. False to customize the intent

  @Test
  public void intent() {
    Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

    Intent intent = new Intent();
    List<ISBikesData> bikeList = PrefData.getBikeList(appContext);
    ISBikesData model = bikeList.get(0);
    intent.putExtra(EditBikeActivity.ARG_OPEN_PROFILE_CAMERA, false);
    intent.putExtra(DetailBikeActivity.ARG_IS_BIKES_DATA, Utils.getGson().toJson(model));

    activityRule.launchActivity(intent);

    // Continue with your test
  }
}