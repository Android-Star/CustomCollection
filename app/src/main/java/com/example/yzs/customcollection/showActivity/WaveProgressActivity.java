package com.example.yzs.customcollection.showActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.yzs.customcollection.R;
import com.example.yzs.customcollection.views.WaveView;
import java.text.DecimalFormat;

public class WaveProgressActivity extends AppCompatActivity implements View.OnClickListener {
  private WaveView wvProgress;
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

    wvProgress.setOnAnimationListener(new WaveView.OnAnimationListener() {
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
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btnStart:
        wvProgress.setProgress(50, 1000);

        break;
      default:
        break;
    }
  }
}
