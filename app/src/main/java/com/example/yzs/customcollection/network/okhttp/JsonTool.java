package com.example.yzs.customcollection.network.okhttp;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.lang.reflect.Type;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class JsonTool {

  public static final String TAG = "JsonTool";

  private static final Gson GOOGLE_GSON =
      new GsonBuilder().addSerializationExclusionStrategy(new IgnoreStrategy()).create();

  private static Handler mainHandler = new Handler(Looper.getMainLooper());

  private JsonTool() {
  }

  public static JSONObject parseToJSONObject(String json) {
    JSONObject parsedJsonObject = new JSONObject();
    JSONTokener jsonTokener = new JSONTokener(json);

    try {
      Object obj = jsonTokener.nextValue();
      if (obj != null) {
        if (obj instanceof JSONObject) {
          parsedJsonObject = ((JSONObject) obj);
        }
      }
    } catch (Exception ee) {
    }

    return parsedJsonObject;
  }

  public static JSONArray parseToJSONOArray(String json) {
    JSONArray parsedJsonObject = new JSONArray();

    if (json != null && !"".equals(json)) {
      JSONTokener jsonTokener = new JSONTokener(json);

      try {
        Object obj = jsonTokener.nextValue();
        if (obj != null) {
          if (obj instanceof JSONObject) {
            parsedJsonObject.put((JSONObject) obj);
          } else if (obj instanceof JSONArray) {
            parsedJsonObject = (JSONArray) obj;
          }
        }
      } catch (Exception ee) {
      }
    }

    return parsedJsonObject;
  }

  public static JSONObject getJSONObject(JSONObject jsonObj, String name) {
    if (jsonObj != null && jsonObj.has(name)) {
      try {
        return jsonObj.getJSONObject(name);
      } catch (JSONException e) {
        e.printStackTrace();
        return null;
      }
    }
    return null;
  }

  public static JsonObject getJsonObject(String key, String value) {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty(key, value);
    return jsonObject;
  }

  public static JSONObject getJSONObject(JSONArray jsonArr, int index) {
    if (jsonArr != null) {
      try {
        int length = jsonArr.length();
        if (index < length) {
          return jsonArr.getJSONObject(index);
        }
      } catch (JSONException e) {
        e.printStackTrace();
        return null;
      }
    }
    return null;
  }

  public static String getJsonValue(JSONObject json, String key) {
    if (json != null && json.has(key)) {
      try {
        return json.getString(key);
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    return "";
  }

  public static int getJsonIntValue(JSONObject json, String key) {
    if (json != null && json.has(key)) {
      try {
        return json.getInt(key);
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    return -1;
  }

  public static double getJsonDoubleValue(JSONObject json, String key) {
    if (json != null && json.has(key)) {
      try {
        return json.getDouble(key);
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    return -1;
  }

  public static String getJsonValue(JSONObject json, String key, String defValue) {
    if (json != null && json.has(key)) {
      try {
        String value = json.getString(key);
        if (TextUtils.isEmpty(value)) {
          return defValue;
        }
        return value;
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    return defValue;
  }

  public static boolean getJsonValue(JSONObject json, String key, boolean defValue) {
    if (json != null && json.has(key)) {
      try {
        return json.getBoolean(key);
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    return defValue;
  }

  public static int getJsonValue(JSONObject json, String key, int defValue) {
    if (json != null && json.has(key)) {
      try {
        return json.getInt(key);
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    return defValue;
  }

  public static double getJsonValue(JSONObject json, String key, double defValue) {
    if (json != null && json.has(key)) {
      try {
        return json.getDouble(key);
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    return defValue;
  }

  public static JSONArray getJsonArray(JSONObject jsonObj, String key) {
    if (jsonObj != null && jsonObj.has(key)) {
      try {
        return jsonObj.getJSONArray(key);
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  public static String toJson(Object object) {
    try {
      return GOOGLE_GSON.toJson(object);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static <T> T fromJson(String json, Class<T> classOfT) {
    if (TextUtils.isEmpty(json)) return null;
    try {
      return GOOGLE_GSON.fromJson(json, classOfT);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static <T> T fromJson(JsonObject json, Class<T> typeOfT) {
    if (json == null) return null;
    try {
      return GOOGLE_GSON.fromJson(json, typeOfT);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static <T> void fromJsonSync(final String json, final Class<T> classOfT,
      final DeserializeListener<T> listener) {
    if (TextUtils.isEmpty(json)) listener.onError("null");
    new Thread(new Runnable() {
      @Override public void run() {
        try {
          final T t = GOOGLE_GSON.fromJson(json, classOfT);
          mainHandler.post(new Runnable() {
            @Override public void run() {
              listener.onResponse(t);
            }
          });
        } catch (final Exception e) {
          e.printStackTrace();
          mainHandler.post(new Runnable() {
            @Override public void run() {
              listener.onError(e.toString());
            }
          });
        }
      }
    }).start();
  }

  public static <T> T fromJson(String json, Type typeToken) {
    if (TextUtils.isEmpty(json)) return null;
    try {
      return GOOGLE_GSON.fromJson(json, typeToken);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static void putJsonObjectValue(JSONObject objc, String key, Object value) {
    try {
      objc.put(key, value);
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  public interface DeserializeListener<T> {
    void onResponse(T response);

    void onError(String errorMessage);
  }

  public static class IgnoreStrategy implements ExclusionStrategy {

    @Override public boolean shouldSkipClass(Class<?> clazz) {
      return false;
    }

    @Override public boolean shouldSkipField(FieldAttributes fieldAttributes) {
      String field = fieldAttributes.getName();
      if ("baseObjId".equals(field) || "listToClearAssociatedFK".equals(field)
          || "listToClearSelfFK".equals(field) || "associatedModelsMapWithoutFK".equals(field)
          || "associatedModelsMapWithFK".equals(field) || "associatedModelsMapForJoinTable".equals(
          field)) {
        return true;
      }
      return false;
    }
  }
}
