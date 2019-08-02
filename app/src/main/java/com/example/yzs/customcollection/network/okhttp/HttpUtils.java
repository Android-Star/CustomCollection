package com.example.yzs.customcollection.network.okhttp;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtils {
  private static OkHttpClient okHttpClient;
  private static final String TAG = "HttpUtils";
  private static HashSet<Object> cancelTagList = new HashSet<>();
  private static final String ON_RESPONSE_NAME = "onResponse";
  private static Map<Object, Handler> handlerMap = new HashMap<>();

  private static final String CANCEL_ERROR = "java.io.IOException: Canceled";

  private HttpUtils() {
  }

  public static final MediaType MEDIA_TYPE_JSON =
      MediaType.parse("application/json; charset=utf-8");

  public static final MediaType MEDIA_TYPE_WWW =
      MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

  public static void init() {
    okHttpClient = new OkHttpClient.Builder().retryOnConnectionFailure(true)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build();
  }

  public static <T> Type getResponseType(ResponseCallBack<T> callBack) {
    Method[] methods = callBack.getClass().getMethods();
    for (Method method : methods) {
      if (method.getName().contains(ON_RESPONSE_NAME)) {
        return method.getGenericParameterTypes()[0];
      }
    }
    return null;
  }

  public static <T> void post(Object tag, String url, HashMap<String, String> headMap,
      MediaType mediaType, String param, final boolean callBackOnMainThread,
      final ResponseCallBack<T> callback) {
    Log.d(TAG, "HttpUtils post:url" + url + ",param:" + param);
    cancelTagList.remove(tag);
    Request.Builder builder = new Request.Builder().url(url).tag(tag);
    boolean firstHeader = true;
    if (headMap != null) {
      for (String key : headMap.keySet()) {
        if (firstHeader) {
          firstHeader = false;
          builder.header(key, headMap.get(key));
        } else {
          builder.addHeader(key, headMap.get(key));
        }
      }
    }
    final Request request = builder.post(RequestBody.create(mediaType, param)).build();
    doRequest(tag, request, callBackOnMainThread, callback);
  }

  public static <T> void post(Object tag, String url, HashMap<String, String> headMap,
      HashMap<String, Object> param, final boolean callBackOnMainThread,
      final ResponseCallBack<T> callback) {
    Log.d(TAG, "HttpUtils post:url" + url + ",param:" + param);
    cancelTagList.remove(tag);
    Request.Builder builder = new Request.Builder().url(url).tag(tag);
    boolean firstHeader = true;
    if (headMap != null) {
      for (String key : headMap.keySet()) {
        if (firstHeader) {
          firstHeader = false;
          builder.header(key, headMap.get(key));
        } else {
          builder.addHeader(key, headMap.get(key));
        }
      }
    }
    MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
    if (param != null) {
      for (String key : param.keySet()) {
        if (param.get(key) instanceof File) {
          File file = (File) param.get(key);
          requestBody.addFormDataPart(key, file.getName(),
              RequestBody.create(MediaType.parse("image/*"), file));
        }
        if (param.get(key) instanceof String) {
          requestBody.addFormDataPart(key, (String) param.get(key));
        }
      }
    }
    final Request request = builder.post(requestBody.build()).build();
    doRequest(tag, request, callBackOnMainThread, callback);
  }

  public static <T> void post(String tag, String url, HashMap<String, String> headMap,
      String fileKey, File file, String param, final boolean callBackOnMainThread,
      final ResponseCallBack<T> callback) {
    Log.d(TAG, "HttpUtils post url:" + url + ",param:" + param);
    MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
    RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
    requestBody.addFormDataPart("file", fileKey, body);
    requestBody.addFormDataPart("body", param);
    Request.Builder builder = new Request.Builder().url(url).tag(tag);
    boolean firstHeader = true;
    if (headMap != null) {
      for (String key : headMap.keySet()) {
        if (firstHeader) {
          firstHeader = false;
          builder.header(key, headMap.get(key));
        } else {
          builder.addHeader(key, headMap.get(key));
        }
      }
    }
    final Request request = builder.post(requestBody.build()).build();
    doRequest(tag, request, callBackOnMainThread, callback);
  }

  public static <T> void get(Object tag, String url, HashMap<String, String> headMap,
      final boolean callBackOnMainThread, final ResponseCallBack<T> callback) {
    Log.d(TAG, "HttpUtils get url:" + url);
    cancelTagList.remove(tag);
    Request.Builder builder = new Request.Builder().url(url).tag(tag);
    boolean firstHeader = true;
    if (headMap != null) {
      for (String key : headMap.keySet()) {
        if (firstHeader) {
          firstHeader = false;
          builder.header(key, headMap.get(key));
        } else {
          builder.addHeader(key, headMap.get(key));
        }
      }
    }
    Request request = builder.get().build();
    doRequest(tag, request, callBackOnMainThread, callback);
  }

  public static <T> void callBackResponse(T result, ResponseCallBack<T> callBack) {
    try {
      if (result != null) {
        callBack.onResponse(result);
      } else {
        callBack.onError("result:" + result);
      }
    } catch (Exception e) {
      e.printStackTrace();
      callBack.onError(e.toString());
    }
  }

  private static Handler getHandler(Object tag) {
    if (handlerMap.containsKey(tag)) {
      return handlerMap.get(tag);
    }
    Handler handler = new Handler(Looper.getMainLooper());
    handlerMap.put(tag, handler);
    return handler;
  }

  private static <T> void doRequest(final Object tag, Request request,
      final boolean callBackOnMainThread, final ResponseCallBack<T> callback) {
    final Call call = okHttpClient.newCall(request);
    call.enqueue(new Callback() {
      @Override public void onFailure(Call call, final IOException e) {
        Log.e(TAG, "get error:" + e.toString());
        if (CANCEL_ERROR.equals(e.toString())) {
          return;
        }
        if (cancelTagList.contains(tag)) {
          Log.d(TAG, "this request canceled");
          return;
        }
        if (callBackOnMainThread) {
          getHandler(tag).post(new Runnable() {
            @Override public void run() {
              callback.onError(e.toString());
            }
          });
        } else {
          callback.onError(e.toString());
        }
      }

      @Override public void onResponse(Call call, final Response response) throws IOException {
        if (cancelTagList.contains(tag)) {
          Log.d(TAG, "onResponse this request canceled");
          return;
        }
        final String resultT = response.body().string();
        Log.d(TAG, "HttpUtils success:" + resultT);
        final T result = JsonTool.fromJson(resultT, getResponseType(callback));
        if (callBackOnMainThread) {
          getHandler(tag).post(new Runnable() {
            @Override public void run() {
              callBackResponse(result, callback);
            }
          });
        } else {
          callBackResponse(result, callback);
        }
      }
    });
  }

  public static void cancel(Object tag) {
    Log.d(TAG, "cancel:" + tag);
    cancelTagList.add(tag);
    getHandler(tag).removeCallbacksAndMessages(null);
    for (Call call : okHttpClient.dispatcher().queuedCalls()) {
      try {
        if (call.request().tag().equals(tag)) {
          call.cancel();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    for (Call call : okHttpClient.dispatcher().runningCalls()) {
      try {
        if (call.request().tag().equals(tag)) {
          call.cancel();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
