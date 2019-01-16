package com.example.yzs.customcollection;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.yzs.customcollection.showActivity.ProgressActivity;
import com.example.yzs.customcollection.showActivity.ScanActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  private Button btnWaveProgress;
  private Button btnScan;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initViews();
    initListener();
  }

  private void initListener() {
    btnWaveProgress.setOnClickListener(this);
    btnScan.setOnClickListener(this);
  }

  private void initViews() {
    btnWaveProgress = findViewById(R.id.btnProgress);
    btnScan = findViewById(R.id.btnScan);
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btnProgress:
        startActivity(new Intent(this, ProgressActivity.class));
        break;
      case R.id.btnScan:
        startActivity(new Intent(this, ScanActivity.class));
        break;
      default:
        break;
    }
  }
}
