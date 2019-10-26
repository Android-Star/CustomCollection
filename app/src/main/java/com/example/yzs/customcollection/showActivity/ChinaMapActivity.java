package com.example.yzs.customcollection.showActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.yzs.customcollection.R;
import com.example.yzs.customcollection.views.chinaMap.ChinaMap;

public class ChinaMapActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnChineInner, btnChinaTaiWan;
    private ChinaMap chinaMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chinamap);
        btnChineInner = findViewById(R.id.btnChineInner);
        btnChinaTaiWan = findViewById(R.id.btnChinaTaiWan);
        chinaMap = findViewById(R.id.chinaMap);

        btnChineInner.setOnClickListener(this);
        btnChinaTaiWan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnChineInner:
                chinaMap.setResId(R.raw.china);
                break;
            case R.id.btnChinaTaiWan:
                chinaMap.setResId(R.raw.taiwan);
                break;
            default:
                break;
        }
    }
}
