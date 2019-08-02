package com.example.yzs.customcollection.showActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.example.yzs.customcollection.R;
import com.example.yzs.customcollection.views.roadWheel.HomeRoadView;
import com.example.yzs.customcollection.views.roadWheel.HomeWheelView;

public class RoadWheelActivity extends AppCompatActivity {
  private HomeRoadView homeRoadView;
  private HomeWheelView homeWheelView;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_road_wheel);
    homeRoadView = findViewById(R.id.llRoad);
    homeWheelView = findViewById(R.id.homeWheelView);

    homeRoadView.registerScrollListener(new HomeRoadView.ScrollListener() {
      @Override public void onScroll(int distance) {
        homeWheelView.updateScrollValue(distance);
      }
    });
  }
}
