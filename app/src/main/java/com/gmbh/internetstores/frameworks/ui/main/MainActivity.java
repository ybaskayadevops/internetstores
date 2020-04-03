package com.gmbh.internetstores.frameworks.ui.main;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.gmbh.internetstores.R;
import com.gmbh.internetstores.frameworks.adapter.BikeListAdapter;
import com.gmbh.internetstores.frameworks.data.model.ISBikesData;
import com.gmbh.internetstores.frameworks.data.pref.PrefData;
import com.gmbh.internetstores.frameworks.ui.addbike.AddBikeActivity;
import com.gmbh.internetstores.frameworks.ui.base.BaseActivity;
import com.gmbh.internetstores.frameworks.ui.camera.CameraActivity;
import com.gmbh.internetstores.frameworks.utils.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {


  RecyclerView recyclerViewList;
  FloatingActionButton fab;
  BikeListAdapter mAdapter;



  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    setUI();
    setOnClickListener();
    setRecyclerView();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  public void setUI() {
    recyclerViewList = findViewById(R.id.recyclerViewList);
    fab = findViewById(R.id.fab);
  }

  public void setOnClickListener() {

    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        AddBikeActivity.load(MainActivity.this, false, Constants.PROFIL_UPDATED);
      }
    });
  }

  @Override
  protected void onResume() {
    super.onResume();
    setAdapter();
  }

  public void setRecyclerView() {

    recyclerViewList.setHasFixedSize(true);

    LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
    recyclerViewList.setLayoutManager(layoutManager);

    mAdapter = new BikeListAdapter(MainActivity.this);
    recyclerViewList.setAdapter(mAdapter);
    setAdapter();
  }

  public void setAdapter() {
    List<ISBikesData> list = PrefData.getBikeList(getContext());
    if (list != null) {
      mAdapter.addUpdatedData(list);
    }
  }
}
