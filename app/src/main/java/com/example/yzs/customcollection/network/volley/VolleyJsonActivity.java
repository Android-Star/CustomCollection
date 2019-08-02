package com.example.yzs.customcollection.network.volley;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.yzs.customcollection.R;
import org.json.JSONObject;

public class VolleyJsonActivity extends Activity {
  private TextView tvVolley;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_volley);
    tvVolley = findViewById(R.id.tvVolley);
    RequestQueue requestQueue = Volley.newRequestQueue(this);

    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
        "http://ip.taobao.com/service/getIpInfo.php?ip=59.108.54.37",
        new Response.Listener<JSONObject>() {
          @Override public void onResponse(JSONObject jsonObject) {
            tvVolley.setText(jsonObject.toString());
          }
        }, new Response.ErrorListener() {
      @Override public void onErrorResponse(VolleyError volleyError) {

      }
    });
    requestQueue.add(request);
  }
}
