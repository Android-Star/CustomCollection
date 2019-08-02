package com.example.yzs.customcollection.showActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.example.yzs.customcollection.R;
import com.example.yzs.customcollection.views.banner.CustomBanner;

public class BannerViewActivity extends AppCompatActivity {
  private CustomBanner banner;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.layout_banner);
    banner = findViewById(R.id.banner);
    banner.startAutoPlay();
  }
}
