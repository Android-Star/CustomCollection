package com.example.yzs.customcollection.showActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.example.yzs.customcollection.R;
import com.example.yzs.customcollection.views.scanView.RadarScanView;

public class ScanActivity extends AppCompatActivity implements View.OnClickListener {
  private Button btnStart;
  private Button btnStop;
  private RadarScanView radarScanView;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.layout_scan);

    initViews();

    initListeners();

    initData();
  }

  private void initViews() {
    btnStart = findViewById(R.id.btnStart);
    btnStop = findViewById(R.id.btnStop);
    radarScanView = findViewById(R.id.radarScanView);
  }

  private void initListeners() {
    btnStart.setOnClickListener(this);
    btnStop.setOnClickListener(this);
  }

  private void initData() {

  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btnStart:
        radarScanView.start(1000);
        break;
      case R.id.btnStop:
        radarScanView.stop();
        break;
      default:
        break;
    }
  }
}
