package com.example.yzs.customcollection.showActivity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.yzs.customcollection.R;
import com.example.yzs.customcollection.views.bezier.QQBezierView;

public class QQBezierActivity extends Activity {
    private QQBezierView qqBerzierView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qqbubble
        );
        qqBerzierView = findViewById(R.id.qqBerzierView);
    }

    public void onClick(View view) {
        qqBerzierView.reset();
    }
}
