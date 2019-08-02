package com.example.yzs.customcollection;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.example.yzs.customcollection.showActivity.BannerViewActivity;
import com.example.yzs.customcollection.showActivity.ButtonActivity;
import com.example.yzs.customcollection.showActivity.Clean360Activity;
import com.example.yzs.customcollection.showActivity.ClockActivity;
import com.example.yzs.customcollection.showActivity.DashBoardActivity;
import com.example.yzs.customcollection.showActivity.NetWorkActivity;
import com.example.yzs.customcollection.showActivity.ProgressActivity;
import com.example.yzs.customcollection.showActivity.QQBezierActivity;
import com.example.yzs.customcollection.showActivity.RoadWheelActivity;
import com.example.yzs.customcollection.showActivity.ScanActivity;
import com.example.yzs.customcollection.showActivity.SeekBarActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  private Button btnWaveProgress;
  private Button btnScan;
  private Button btnClock;
  private Button btnBannerView;
  private Button btnDashBoard;
  private Button btnSeedBar;
  private Button btnButton;
  private Button cleanButton;
  private Button qqBubble;
  private Button networkButton;
  private Button roadWheelButton;

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
    btnBannerView.setOnClickListener(this);
    btnDashBoard.setOnClickListener(this);
    btnSeedBar.setOnClickListener(this);
    btnButton.setOnClickListener(this);
    cleanButton.setOnClickListener(this);
    qqBubble.setOnClickListener(this);
    networkButton.setOnClickListener(this);
    roadWheelButton.setOnClickListener(this);
  }

  private void initViews() {
    btnWaveProgress = findViewById(R.id.btnProgress);
    btnScan = findViewById(R.id.btnScan);
    btnClock = findViewById(R.id.btnClock);
    btnBannerView = findViewById(R.id.btnBannerView);
    btnDashBoard = findViewById(R.id.btnDashBoard);
    btnSeedBar = findViewById(R.id.btnSeekBar);
    btnButton = findViewById(R.id.btnButton);
    cleanButton = findViewById(R.id.cleanButton);
    qqBubble = findViewById(R.id.qqBubble);
    networkButton = findViewById(R.id.networkButton);
    roadWheelButton = findViewById(R.id.roadWheelButton);
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
      case R.id.btnBannerView:
        startActivity(new Intent(this, BannerViewActivity.class));
        break;
      case R.id.btnDashBoard:
        startActivity(new Intent(this, DashBoardActivity.class));
        break;
      case R.id.btnSeekBar:
        startActivity(new Intent(this, SeekBarActivity.class));
        break;
      case R.id.btnButton:
        startActivity(new Intent(this, ButtonActivity.class));
        break;
      case R.id.cleanButton:
        startActivity(new Intent(this, Clean360Activity.class));
        break;
      case R.id.qqBubble:
        startActivity(new Intent(this, QQBezierActivity.class));
        break;
      case R.id.networkButton:
        startActivity(new Intent(this, NetWorkActivity.class));
        break;
      case R.id.roadWheelButton:
        startActivity(new Intent(this, RoadWheelActivity.class));
        break;
      default:
        break;
    }
  }
}
