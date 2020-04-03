package com.gmbh.internetstores.detail;


import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.gmbh.internetstores.R;
import com.gmbh.internetstores.frameworks.data.model.ISBikesData;
import com.gmbh.internetstores.frameworks.data.pref.PrefData;
import com.gmbh.internetstores.frameworks.ui.detailbike.DetailBikeActivity;
import com.gmbh.internetstores.frameworks.ui.main.MainActivity;
import com.gmbh.internetstores.frameworks.utils.Utils;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class DetailBikeActivityTest {

  @Rule
  public ActivityTestRule<DetailBikeActivity> activityRule
      = new ActivityTestRule<>(
      DetailBikeActivity.class,
      true,     // initialTouchMode
      false);   // launchActivity. False to customize the intent

  @Test
  public void intent() {
    Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

    Intent intent = new Intent();
    List<ISBikesData> bikeList = PrefData.getBikeList(appContext);
    ISBikesData model = bikeList.get(0);
    intent.putExtra(DetailBikeActivity.ARG_IS_BIKES_DATA, Utils.getGson().toJson(model));

    activityRule.launchActivity(intent);

    // Continue with your test
  }
}