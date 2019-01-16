package com.example.yzs.customcollection.showActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.example.yzs.customcollection.R;
import com.example.yzs.customcollection.views.Clock.ClockView;

public class ClockActivity extends AppCompatActivity implements View.OnClickListener {
  private ClockView clockView;
  private Button btnStart;
  private Button btnStop;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.layout_clock);

    initViews();
    initListeners();
  }

  private void initListeners() {
    btnStart.setOnClickListener(this);
    btnStop.setOnClickListener(this);
  }

  private void initViews() {
    btnStart = findViewById(R.id.btnStart);
    clockView = findViewById(R.id.clockView);
    btnStop = findViewById(R.id.btnStop);
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btnStart:
        clockView.start();
        break;
      case R.id.btnStop:
        clockView.stop();
        break;
      default:
        break;
    }
  }
}
