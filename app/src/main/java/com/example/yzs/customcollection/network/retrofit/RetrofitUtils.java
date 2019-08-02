package com.example.yzs.customcollection.network.retrofit;

import android.content.Context;
import android.util.Log;
import java.io.File;
import java.util.concurrent.TimeUnit;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtils {
  private static final long DEFAULT_TIMEOUT = 10;
  private static volatile RetrofitUtils instance;
  private HttpLoggingInterceptor loggingInterceptor;
  private ApiService apiService;
  private Context context;

  public static RetrofitUtils getInstance() {
    if (instance == null) {
      synchronized (RetrofitUtils.class) {
        if (instance == null) {
          instance = new RetrofitUtils();
        }
      }
    }
    return instance;
  }

  public void init(Context context) {
    this.context = context;
    loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
      @Override public void log(String message) {            //打印retrofit日志
        Log.i("RetrofitLog", "retrofitBack ======================= " + message);
      }
    });

    String url = "http://10.30.10.32:10080/rest/v2/";
    Retrofit retrofit = new Retrofit.Builder().baseUrl(url)
        .client(getOkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    apiService = retrofit.create(ApiService.class);
  }

  private OkHttpClient getOkHttpClient() {
    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    //定制OkHttp
    OkHttpClient.Builder builder = new OkHttpClient.Builder();
    builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
    builder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
    builder.addInterceptor(loggingInterceptor);
    //设置缓存
    File httpCacheDirectory = new File(context.getCacheDir(), context.getPackageName());
    builder.cache(new Cache(httpCacheDirectory, 10 * 1024 * 1024));
    return builder.build();
  }

  public ApiService getApiService() {
    return apiService;
  }
}
