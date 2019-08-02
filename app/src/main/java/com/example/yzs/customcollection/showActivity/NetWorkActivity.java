package com.example.yzs.customcollection.showActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.example.yzs.customcollection.R;
import com.example.yzs.customcollection.network.okhttp.OkhttpActivity;
import com.example.yzs.customcollection.network.retrofit.RetrofitActivity;
import com.example.yzs.customcollection.network.volley.VolleyJsonActivity;
import com.example.yzs.customcollection.network.volley.VolleyStringActivity;

public class NetWorkActivity extends AppCompatActivity implements View.OnClickListener {
  private Button btnOkhttp;
  private Button btnRetrofit;
  private Button btnVolleyString;
  private Button btnVolleyJson;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_network);

    btnOkhttp = findViewById(R.id.btnOkhttp);
    btnRetrofit = findViewById(R.id.btnRetrofit);
    btnVolleyString = findViewById(R.id.btnVolleyString);
    btnVolleyJson = findViewById(R.id.btnVolleyJson);

    btnOkhttp.setOnClickListener(this);
    btnRetrofit.setOnClickListener(this);
    btnVolleyString.setOnClickListener(this);
    btnVolleyJson.setOnClickListener(this);
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btnOkhttp:
        startActivity(new Intent(NetWorkActivity.this, OkhttpActivity.class));
        break;
      case R.id.btnRetrofit:
        startActivity(new Intent(NetWorkActivity.this, RetrofitActivity.class));
        break;
      case R.id.btnVolleyString:
        startActivity(new Intent(NetWorkActivity.this, VolleyStringActivity.class));
        break;
        case R.id.btnVolleyJson:
        startActivity(new Intent(NetWorkActivity.this, VolleyJsonActivity.class));
        break;
      default:
        break;
    }
  }
}
