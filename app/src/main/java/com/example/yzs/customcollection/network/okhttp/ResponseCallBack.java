package com.example.yzs.customcollection.network.okhttp;

import java.io.Serializable;

public interface ResponseCallBack<T> extends Serializable {
  void onResponse(T response);

  void onError(String error);
}
