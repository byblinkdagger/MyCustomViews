# MyCustomViews

###### 2017/08/11 update ScanningView

* usage
```
            scanningView.startAnim();
            scanningView.stopAnim();
```

* result

<img src="/screen/scan.gif" alt="screenshot" title="screenshot" width="270" height="486" />


###### 2017/06/21 update HorizontalPickerView

HorizontalPickerView is a picker view according to [huzenan/EasyPickerView](https://github.com/huzenan/EasyPickerView) which can horizontally scroll and pick the item content.

* usage

```
HorizontalPickerView pickerView = (HorizontalPickerView) findViewById(R.id.picker_view);
pickerView.setDataList(data);
pickerView.setOnScrollChangedListener(new HorizontalPickerView.OnScrollChangedListener() {
    @Override
    public void onScrollChanged(int curIndex) {

    }

    @Override
    public void onScrollFinished(int curIndex) {

    }
});
```

* result

<img src="/screen/horizontal.gif" alt="screenshot" title="screenshot" width="270" height="486" />
