package com.example.yzs.customcollection;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.yzs.customcollection.showActivity.ProgressActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  private Button btnWaveProgress;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initViews();
    initListener();
  }

  private void initListener() {
    btnWaveProgress.setOnClickListener(this);
  }

  private void initViews() {
    btnWaveProgress = findViewById(R.id.btnWaveProgress);
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btnWaveProgress:
        startActivity(new Intent(this, ProgressActivity.class));
        break;
      default:
        break;
    }
  }
}
