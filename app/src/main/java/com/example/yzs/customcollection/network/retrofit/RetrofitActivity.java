package com.example.yzs.customcollection.network.retrofit;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.yzs.customcollection.R;
import com.example.yzs.customcollection.network.volley.IpModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitActivity extends Activity implements View.OnClickListener {
  private TextView tvResult;
  private Button btnGet;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_retrofit);
    tvResult = findViewById(R.id.tvResult);
    btnGet = findViewById(R.id.btnGet);

    btnGet.setOnClickListener(this);
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btnGet:
        Call<IpModel> ipMsg = RetrofitUtils.getInstance().getApiService().getIpMsg();
        ipMsg.enqueue(new Callback<IpModel>() {
          @Override public void onResponse(Call<IpModel> call, Response<IpModel> response) {
            tvResult.setText(response.message());
          }

          @Override public void onFailure(Call<IpModel> call, Throwable t) {

            tvResult.setText(t.getMessage());
          }
        });
        break;
      default:
        break;
    }
  }
}
