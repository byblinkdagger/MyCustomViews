package com.blink.dagger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.bt_horizontal, R.id.bt_scan})
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.bt_horizontal:
                Log.d("luck","1111111111");
                intent.setClass(this,HorizontalPickerActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_scan:
                Log.d("luck","2222222222222");
                intent.setClass(this,ScanActivity.class);
                startActivity(intent);
                break;
        }
    }
}
