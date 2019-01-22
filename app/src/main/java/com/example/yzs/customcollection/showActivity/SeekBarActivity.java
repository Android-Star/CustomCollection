package com.example.yzs.customcollection.showActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.example.yzs.customcollection.R;
import com.example.yzs.customcollection.views.seekBar.ChengyuSeekBar;

public class SeekBarActivity extends AppCompatActivity implements View.OnClickListener {
  private Button btnAdd;
  private Button btnLess;
  private Button btnComplete;
  private ChengyuSeekBar seekbar;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.layout_seekbar);

    initViews();

    initListeners();
  }

  private void initListeners() {
    btnAdd.setOnClickListener(this);
    btnLess.setOnClickListener(this);
    btnComplete.setOnClickListener(this);
  }

  private void initViews() {
    btnAdd = findViewById(R.id.btnAdd);
    btnLess = findViewById(R.id.btnLess);
    seekbar = findViewById(R.id.seekbar);
    btnComplete = findViewById(R.id.btnComplete);
  }

  @Override public void onClick(View v) {

    switch (v.getId()) {
      case R.id.btnAdd:
        seekbar.add();
        break;
      case R.id.btnLess:
        seekbar.less();
        break;
      case R.id.btnComplete:
        seekbar.complete();
        break;
      default:
        break;
    }
  }
}
