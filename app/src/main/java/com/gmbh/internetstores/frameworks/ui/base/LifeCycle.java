package com.gmbh.internetstores.frameworks.ui.base;

public interface LifeCycle {

  void onStarted();

  void onResumed();

  void onStopped();

  void onPaused();

  void onDestroyed();

}
