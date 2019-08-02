package com.example.yzs.customcollection;

import android.app.Application;
import com.example.yzs.customcollection.network.retrofit.RetrofitUtils;

public class MyApplication extends Application {

  @Override public void onCreate() {
    super.onCreate();
    RetrofitUtils.getInstance().init(this);
  }
}
