package com.example.yzs.customcollection.utils;

import android.content.Context;

public class DisplayUtils {
  private Context context;
  public static DisplayUtils instence;

  public static DisplayUtils getInstance(Context context) {
    if (instence == null) {
      synchronized (DisplayUtils.class) {
        if (instence == null) {
          instence = new DisplayUtils(context);
        }
      }
    }
    return instence;
  }

  private DisplayUtils(Context context) {
    this.context = context;
  }

  public float dp2px(float dpValue) {
    return context.getResources().getDisplayMetrics().density * dpValue + 0.5f;
  }
}
