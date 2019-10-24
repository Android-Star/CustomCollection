package com.example.yzs.customcollection.showActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.yzs.customcollection.R;
import com.example.yzs.customcollection.views.qqzoom.QQHeaderListView;

public class QQZoomActivity extends AppCompatActivity {
    private QQHeaderListView qqHeadListView;
    private LinearLayout llTop;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_qqzoom);

        qqHeadListView = findViewById(R.id.qqHeadListView);
        llTop = findViewById(R.id.llTop);
        View headView = LayoutInflater.from(this).inflate(R.layout.layout_qqhead, null, false);
        ImageView ivHead = headView.findViewById(R.id.ivHead);
        qqHeadListView.setHeadImage(ivHead);
        qqHeadListView.setTopView(llTop);
        qqHeadListView.addHeaderView(headView);
        String[] data=getResources().getStringArray(R.array.qqlist_data);
        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,data);
        qqHeadListView.setAdapter(adapter);
    }
}
