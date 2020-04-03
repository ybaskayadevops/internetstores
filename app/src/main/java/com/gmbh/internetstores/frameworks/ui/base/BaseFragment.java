package com.gmbh.internetstores.frameworks.ui.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment implements BaseInterface,
    FragmentInterface {


  private boolean isCreated = false;
  private boolean isFirstLoad = true;
  private BaseActivity waBaseActivity;

  private LifeCycle waLifeCycle;
  private ResultListener waResultListener;
  private PermissionResultListener waPermissionResultListener;

  public void setWaLifeCycle(LifeCycle waLifeCycle) {
    this.waLifeCycle = waLifeCycle;
  }

  public void setWaResultListener(ResultListener waResultListener) {
    this.waResultListener = waResultListener;
  }

  public void setWaPermissionResultListener(
      PermissionResultListener waPermissionResultListener) {
    this.waPermissionResultListener = waPermissionResultListener;
  }

  @Override
  public void setUserVisibleHint(boolean isVisibleToUser) {
    super.setUserVisibleHint(isVisibleToUser);
    if (isCreated) {
      if (getUserVisibleHint()) {
        if (isFirstLoad) {
          onFragmentActive(true);
          isFirstLoad = false;
        } else {
          onFragmentActive(false);
        }
      } else {
        onFragmentInActive(false);
      }
    }
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof BaseActivity) {
      waBaseActivity = (BaseActivity) context;
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    waBaseActivity = null;
  }

  public BaseActivity getWABaseActivity() {
    return waBaseActivity;
  }


  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

  }



  @Override
  public void onStart() {
    super.onStart();

    if (waLifeCycle != null) {
      waLifeCycle.onStarted();
    }
  }

  @Override
  public void onResume() {
    if (waLifeCycle != null) {
      waLifeCycle.onResumed();
    }
    super.onResume();
    if (!isCreated && getUserVisibleHint()) {
      onFragmentActive(isFirstLoad);
      isFirstLoad = false;
    }
    isCreated = true;
  }

  @Override
  public void onPause() {
    if (waLifeCycle != null) {
      waLifeCycle.onPaused();
    }
    isCreated = false;
    if (getUserVisibleHint()) {
      onFragmentInActive(true);
    }
    super.onPause();
  }

  @Override
  public void onStop() {
    super.onStop();
    if (waLifeCycle != null) {
      waLifeCycle.onStopped();
    }

  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (waLifeCycle != null) {
      waLifeCycle.onDestroyed();
    }
  }

  public boolean isVisibleToUser() {
    return getUserVisibleHint();
  }



  public void onBackPressed() {
    if (getActivity() != null) {
      getActivity().onBackPressed();
      finishActivity();
    }
  }

  public void finishActivity() {
    if (getActivity() != null) {
      getActivity().finish();
    }
  }

  @Override
  public void loadActivityForResult(Class activityClass, int requestCode) {
    loadActivityForResult(activityClass, null, requestCode);
  }

  @Override
  public void loadActivityForResult(Class activityClass, Bundle bundle, int requestCode) {
    loadActivityForResult(activityClass, bundle, requestCode, false);
  }

  @Override
  public void loadActivityForResult(Class activityClass, Bundle bundle, int requestCode,
      boolean single) {
    Context ctx = getContext();
    if (ctx != null) {
      Intent i = new Intent(ctx, activityClass);
      if (single) {
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      }
      if (bundle != null) {
        i.putExtras(bundle);
      }
      startActivityForResult(i, requestCode);
    }
  }

  @Override
  public void activityAnimation(int enterAnim, int exitAnim) {
    if (getActivity() != null) {
      getActivity().overridePendingTransition(enterAnim, exitAnim);
    }
  }

  @Override
  public void loadActivity(Class activityClass) {
    loadActivity(activityClass, null, null);
  }

  @Override
  public void loadActivity(Class activityClass, Bundle bundle) {
    loadActivity(activityClass, bundle, null);
  }

  @Override
  public void loadActivity(Class activityClass, Bundle bundle, Uri data) {
    loadActivity(activityClass, bundle, data, false);
  }

  @Override
  public void loadActivity(Class activityClass, Bundle bundle, Uri data, boolean singleTop) {
    Context ctx = getContext();
    if (ctx != null) {
      Intent i = new Intent(ctx, activityClass);
      if (singleTop) {
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
      }
      if (bundle != null) {
        i.putExtras(bundle);
      }
      if (data != null) {
        i.setData(data);
      }
      startActivity(i);

    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode != Activity.RESULT_CANCELED && data != null) {
      if (waResultListener != null) {
        waResultListener.onSuccess(requestCode, data);
      }
    } else {
      if (waResultListener != null) {
        waResultListener.onCancel();
      }
    }
  }

  private int permissionRequestCode = 976;

  public void requestAppPermissions(String[] permissions,
      PermissionResultListener waPermissionResultListener) {
    this.waPermissionResultListener = waPermissionResultListener;
    requestPermissions(permissions, permissionRequestCode);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == permissionRequestCode) {
      if (grantResults.length > 0) {
        for (int i = 0; i < grantResults.length; i++) {
          if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
            if (waPermissionResultListener != null) {
              waPermissionResultListener.onDenied();
            }
            return;
          }
        }
        if (waPermissionResultListener != null) {
          waPermissionResultListener.onAccepted();
        }
      } else {
        if (waPermissionResultListener != null) {
          waPermissionResultListener.onDenied();
        }
      }

    }
  }

  @Override
  public boolean isActivity() {
    return false;
  }

  @Override
  public boolean isFragment() {
    return true;
  }

  @Override
  public boolean isActive() {
    return super.isAdded();
  }

  public static class EmptyMessage {

  }

}
