package com.example.yzs.customcollection.showActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.example.yzs.customcollection.R;
import com.example.yzs.customcollection.views.button.RecordButtonView;

public class ButtonActivity extends AppCompatActivity implements View.OnClickListener {
  private RecordButtonView rbv;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.layout_button);

    initViews();
    initListeners();
  }

  private void initViews() {

    rbv = findViewById(R.id.rbv);
  }

  private void initListeners() {
    rbv.setOnClickListener(this);
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.rbv:
        rbv.startAnimation();
        break;
      default:
        break;
    }
  }
}
