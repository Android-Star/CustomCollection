package com.example.yzs.customcollection.network.volley;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.yzs.customcollection.R;

public class VolleyStringActivity extends Activity {
  private TextView tvVolley;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_volley);
    tvVolley = findViewById(R.id.tvVolley);
    RequestQueue requestQueue = Volley.newRequestQueue(this);

    StringRequest request =
        new StringRequest(Request.Method.GET, "https://unitoy.hivoice.cn/karpro/userAgreement.html",
            new Response.Listener<String>() {
              @Override public void onResponse(String s) {
                tvVolley.setText(s);
              }
            }, new Response.ErrorListener() {
          @Override public void onErrorResponse(VolleyError volleyError) {
          }
        });
    requestQueue.add(request);
  }
}
