package com.example.yzs.customcollection;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.yzs.customcollection.showActivity.ClockActivity;
import com.example.yzs.customcollection.showActivity.DashBoardActivity;
import com.example.yzs.customcollection.showActivity.ProgressActivity;
import com.example.yzs.customcollection.showActivity.ScanActivity;
import com.example.yzs.customcollection.showActivity.WaveViewActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  private Button btnWaveProgress;
  private Button btnScan;
  private Button btnClock;
  private Button btnWaveView;
  private Button btnDashBoard;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initViews();
    initListener();
  }

  private void initListener() {
    btnWaveProgress.setOnClickListener(this);
    btnScan.setOnClickListener(this);
    btnClock.setOnClickListener(this);
    btnWaveView.setOnClickListener(this);
    btnDashBoard.setOnClickListener(this);
  }

  private void initViews() {
    btnWaveProgress = findViewById(R.id.btnProgress);
    btnScan = findViewById(R.id.btnScan);
    btnClock = findViewById(R.id.btnClock);
    btnWaveView = findViewById(R.id.btnWaveView);
    btnDashBoard = findViewById(R.id.btnDashBoard);
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btnProgress:
        startActivity(new Intent(this, ProgressActivity.class));
        break;
      case R.id.btnScan:
        startActivity(new Intent(this, ScanActivity.class));
        break;
      case R.id.btnClock:
        startActivity(new Intent(this, ClockActivity.class));
        break;
      case R.id.btnWaveView:
        startActivity(new Intent(this, WaveViewActivity.class));
        break;
      case R.id.btnDashBoard:
        startActivity(new Intent(this, DashBoardActivity.class));
        break;
      default:
        break;
    }
  }
}
