package com.blink.dagger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import dagger.blink.com.customviewlibrary.widget.HorizontalPickerView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HorizontalPickerView pickerView = (HorizontalPickerView) findViewById(R.id.picker_view);
        List<String> data = new ArrayList<>();
        data.add("1");
        data.add("5");
        data.add("8");
        data.add("10");
        data.add("18");
        data.add("88");
        data.add("90");
        data.add("100");
        data.add("111");
        data.add("123");
        data.add("256");
        data.add("388");
        data.add("1000");
        data.add("1818");
        data.add("2000");
        data.add("3000");
        data.add("5000");
        pickerView.setDataList(data);
        pickerView.setOnScrollChangedListener(new HorizontalPickerView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(int curIndex) {

            }

            @Override
            public void onScrollFinished(int curIndex) {

            }
        });
    }
}
