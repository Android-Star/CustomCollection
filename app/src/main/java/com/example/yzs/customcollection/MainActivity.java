package com.example.yzs.customcollection;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.yzs.customcollection.showActivity.ButtonActivity;
import com.example.yzs.customcollection.showActivity.Clean360Activity;
import com.example.yzs.customcollection.showActivity.ClockActivity;
import com.example.yzs.customcollection.showActivity.DashBoardActivity;
import com.example.yzs.customcollection.showActivity.ProgressActivity;
import com.example.yzs.customcollection.showActivity.QQBezierActivity;
import com.example.yzs.customcollection.showActivity.ScanActivity;
import com.example.yzs.customcollection.showActivity.SeekBarActivity;
import com.example.yzs.customcollection.showActivity.WaveViewActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnWaveProgress;
    private Button btnScan;
    private Button btnClock;
    private Button btnWaveView;
    private Button btnDashBoard;
    private Button btnSeedBar;
    private Button btnButton;
    private Button cleanButton;
    private Button qqBubble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initListener();
    }

    private void initListener() {
        btnWaveProgress.setOnClickListener(this);
        btnScan.setOnClickListener(this);
        btnClock.setOnClickListener(this);
        btnWaveView.setOnClickListener(this);
        btnDashBoard.setOnClickListener(this);
        btnSeedBar.setOnClickListener(this);
        btnButton.setOnClickListener(this);
        cleanButton.setOnClickListener(this);
        qqBubble.setOnClickListener(this);
    }

    private void initViews() {
        btnWaveProgress = findViewById(R.id.btnProgress);
        btnScan = findViewById(R.id.btnScan);
        btnClock = findViewById(R.id.btnClock);
        btnWaveView = findViewById(R.id.btnWaveView);
        btnDashBoard = findViewById(R.id.btnDashBoard);
        btnSeedBar = findViewById(R.id.btnSeekBar);
        btnButton = findViewById(R.id.btnButton);
        cleanButton = findViewById(R.id.cleanButton);
        qqBubble = findViewById(R.id.qqBubble);
    }

    @Override
    public void onClick(View v) {
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
            case R.id.btnWaveView:
                startActivity(new Intent(this, WaveViewActivity.class));
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
            default:
                break;
        }
    }
}
