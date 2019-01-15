package com.example.yzs.customcollection.showActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.yzs.customcollection.R;
import com.example.yzs.customcollection.views.CircleProgressView;
import com.example.yzs.customcollection.views.GradientCircleProgressView;
import com.example.yzs.customcollection.views.WaveProgressView;
import java.text.DecimalFormat;

public class ProgressActivity extends AppCompatActivity implements View.OnClickListener {
  private WaveProgressView wvProgress;
  private CircleProgressView cpv;
  private GradientCircleProgressView gcpv;
  private TextView tvPercent;
  private Button btnStart;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.layout_circle_progress);
    initViews();
    initListener();

    initData();
  }

  private void initListener() {
    btnStart.setOnClickListener(this);

    wvProgress.setOnAnimationListener(new WaveProgressView.OnAnimationListener() {
      @Override
      public String howToChangeText(float interpolatedTime, float updateNum, float maxNum) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String s = decimalFormat.format(updateNum / maxNum * interpolatedTime * 100) + "%";
        return s;
      }
    });
  }

  private void initData() {
    wvProgress.setTextView(tvPercent);
  }

  private void initViews() {
    wvProgress = findViewById(R.id.waveView);
    tvPercent = findViewById(R.id.tv_percent);
    btnStart = findViewById(R.id.btnStart);
    cpv = findViewById(R.id.cpv);
    gcpv = findViewById(R.id.gcpv);
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btnStart:
        wvProgress.setProgress((int) (Math.random() * 100), 1000);
        cpv.setProgress((int) (Math.random() * 100));
        gcpv.setProgress((int) (Math.random() * 100));
        break;
      default:
        break;
    }
  }
}
