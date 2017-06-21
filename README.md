# MyCustomViews

* 2017/06/21 update HorizontalPickerView

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

* custom Attr
```
    <declare-styleable name="HorizontalPickerView">
        <attr name="textCount" format="integer"/>
        <attr name="textMaxScale" format="float"/>
        <attr name="textPadding" format="dimension"/>
        <attr name="textColor" format="color"/>
        <attr name="textSize" format="dimension"/>
    </declare-styleable>
```

* demo

![ezgif-1-5065d40614.gif](http://upload-images.jianshu.io/upload_images/2555073-a2289dff24f3344d.gif?imageMogr2/auto-orient/strip)
