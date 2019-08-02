package com.example.yzs.customcollection.network;

import android.text.TextUtils;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

public class UrlConnManager {
  public static HttpURLConnection getUrlConnection(String url) {
    HttpURLConnection urlConnection = null;
    try {
      URL mUrl = new URL(url);

      urlConnection = (HttpURLConnection) mUrl.openConnection();
      urlConnection.setConnectTimeout(15000);
      urlConnection.setReadTimeout(15000);
      urlConnection.setRequestMethod("POST");
      urlConnection.setRequestProperty("Connection", "Keep-Alive");
      urlConnection.setDoOutput(true);
      urlConnection.setDoInput(true);
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return urlConnection;
  }

  public static void postParams(OutputStream outputStream, Map<String, String> params)
      throws IOException {
    StringBuilder stringBuilder = new StringBuilder();
    if (params.isEmpty()) {
      return;
    }
    Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
    while (iterator.hasNext()) {
      Map.Entry<String, String> next = iterator.next();
      if (!TextUtils.isEmpty(stringBuilder.toString())) {
        stringBuilder.append("&");
      }
      stringBuilder.append(URLEncoder.encode(next.getKey(), "UTF-8"));
      stringBuilder.append("=");
      stringBuilder.append(URLEncoder.encode(next.getValue(), "UTF-8"));
    }
    BufferedWriter bufferedWriter =
        new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
    bufferedWriter.write(stringBuilder.toString());
    bufferedWriter.flush();
    bufferedWriter.close();
  }
}
