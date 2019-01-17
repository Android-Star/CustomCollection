package com.example.yzs.customcollection.showActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;
import com.example.yzs.customcollection.R;
import com.example.yzs.customcollection.views.dashboard.DashBoardView;

public class DashBoardActivity extends AppCompatActivity {
  private DashBoardView dashBoardView;
  private SeekBar seekbar;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.layout_dashboard);

    initViews();

    initListeners();
  }

  private void initListeners() {
    seekbar.setMax(100);
    seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        dashBoardView.setProgress(progress);
      }

      @Override public void onStartTrackingTouch(SeekBar seekBar) {

      }

      @Override public void onStopTrackingTouch(SeekBar seekBar) {

      }
    });
  }

  private void initViews() {
    dashBoardView = findViewById(R.id.dashBoardView);
    seekbar=findViewById(R.id.seekbar);
  }
}
