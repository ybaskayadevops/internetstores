package com.gmbh.internetstores.frameworks.ui.base;

import android.content.Intent;
import androidx.annotation.NonNull;

public interface ResultListener {

  void onSuccess(int requestCode, @NonNull Intent data);

  void onCancel();

}
