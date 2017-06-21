package dagger.blink.com.customviewlibrary.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import java.util.ArrayList;
import java.util.List;

import dagger.blink.com.customviewlibrary.R;
import dagger.blink.com.customviewlibrary.util.DensityUtil;

/**
 * Created by lucky on 2017/6/21.
 */
public class HorizontalPickerView extends View {

    // 数据
    private ArrayList<String> dataList = new ArrayList<>();
    // 中间x坐标
    private int cx;
    // 中间y坐标
    private int cy;
    // 按下时的x坐标
    private float downX;
    // 本次滑动的x坐标偏移值
    private float offsetX;
    // 在fling之前的offsetX
    private float oldOffsetX;
    //同屏可显示的文字数，默认为5
    private int textCount ;
    // 文字宽度
    private int textWidth;
    // 文字高度
    private int textHeight;
    //文字间隔
    private int textPadding;
    // 文字最大放大比例，默认2.0f
    private float textMaxScale;

    // 当前选中项
    private int curIndex;
    //偏移值
    private int offsetIndex;

    private Paint textPaint;
    private Paint.FontMetrics fm;
    // 文字大小
    private int textSize;
    // 颜色，默认Color.BLACK
    private int textColor;

    private Scroller scroller;
    private VelocityTracker velocityTracker;
    private int minimumVelocity;
    private int maximumVelocity;
    private int scaledTouchSlop;
    // 回弹距离
    private float bounceDistance;
    // 是否正处于滑动状态
    private boolean isSliding = false;

    public HorizontalPickerView(Context context) {
        this(context,null);
    }

    public HorizontalPickerView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HorizontalPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.HorizontalPickerView, defStyleAttr, 0);
        textCount = a.getInteger(R.styleable.HorizontalPickerView_textCount,5);
        textMaxScale = a.getFloat(R.styleable.HorizontalPickerView_textMaxScale,2.0f);
        textPadding = a.getDimensionPixelSize(R.styleable.HorizontalPickerView_textPadding, DensityUtil.dip2px(getContext(),25));
        textColor = a.getColor(R.styleable.HorizontalPickerView_textColor, Color.BLACK);
        textSize = a.getDimensionPixelSize(R.styleable.HorizontalPickerView_textSize,DensityUtil.dip2px(getContext(),12));

        textPaint = new Paint();
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setAntiAlias(true);
        fm = textPaint.getFontMetrics();
        textHeight = (int) (fm.bottom - fm.top);

        scroller = new Scroller(context);
        minimumVelocity = ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity();
        maximumVelocity = ViewConfiguration.get(getContext()).getScaledMaximumFlingVelocity();
        scaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode != MeasureSpec.EXACTLY){
            width =  (textWidth+textPadding)*textCount;
            Log.d("luck","textWidth :"+textWidth);
            Log.d("luck","textPadding :"+textPadding);
        }
        if (heightMode != MeasureSpec.EXACTLY){
            height = (int) (textHeight * textMaxScale + getPaddingTop() + getPaddingBottom());
        }
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        cx = w/2;
        cy = h/2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (dataList != null && dataList.size() > 0){
            Log.d("luck","start draw now !");
            int size = dataList.size();
            int centerPadding = textWidth + textPadding;
            //当前index为中心位置，向前/向后绘制half个text
            int half = textCount / 2 + 1;
            for (int i = -half; i < half ; i++) {
                int index = curIndex + i - offsetIndex;

                if (index >= 0 && index < size) {
                    // 计算每个字的中间x坐标
                    int tempX = cx + i * centerPadding;
                    //将偏移量加到x坐标中
                    tempX += offsetX % centerPadding;

                    // 根据每个字中间x坐标到cx的距离，计算出scale值
                    float scale = 1.0f - (1.0f * Math.abs(tempX - cx) / centerPadding);

                    // 根据textMaxScale，计算出tempScale值，即实际text应该放大的倍数，范围 1~textMaxScale
                    float tempScale = scale * (textMaxScale - 1.0f) + 1.0f;
                    tempScale = tempScale < 1.0f ? 1.0f : tempScale;
                    float tempAlpha = (tempScale - 1) / (textMaxScale - 1);
                    float textAlpha = (1 - 0.4f) * tempAlpha + 0.4f;
                    textPaint.setTextSize(textSize * tempScale);
                    textPaint.setAlpha((int) (255 * textAlpha));

                    // 绘制
                    Paint.FontMetrics tempFm = textPaint.getFontMetrics();
                    String text = dataList.get(index);
                    float textWidth = textPaint.measureText(text);
                    Log.d("luck","text:"+text);
                    Log.d("luck","textWidth :"+textWidth);
                    canvas.drawText(text, tempX - textWidth / 2,cy-(tempFm.ascent + tempFm.descent) / 2, textPaint);
                    Log.d("luck","end draw now !");
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        addVelocityTracker(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!scroller.isFinished()) {
                    scroller.forceFinished(true);
                    finishScroll();
                }
                downX = event.getX();
                break;

            case MotionEvent.ACTION_MOVE:
                offsetX = event.getX() - downX;
                if (isSliding || Math.abs(offsetX) > scaledTouchSlop) {
                    isSliding = true;
                    reDraw();
                }
                break;

            case MotionEvent.ACTION_UP:
                int scrollXVelocity = getScrollXVelocity() / 5;
                if (Math.abs(scrollXVelocity) > minimumVelocity) {
                    oldOffsetX = offsetX;
                    scroller.fling(0, 0, scrollXVelocity, 0, -Integer.MAX_VALUE, Integer.MAX_VALUE, 0, 0);
                    invalidate();
                } else {
                    finishScroll();
                }

//                // 没有滑动，则判断点击事件
//                if (!isSliding) {
//                    if (downX < contentHeight / 3)
//                        moveBy(-1);
//                    else if (downX > 2 * contentHeight / 3)
//                        moveBy(1);
//                }
                isSliding = false;
                recycleVelocityTracker();
                break;
        }
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        Log.d("luck","computeScroll ing...");
        if (scroller.computeScrollOffset()) {
            offsetX = oldOffsetX + scroller.getCurrX();

            if (!scroller.isFinished())
                reDraw();
            else
                finishScroll();
        }
    }

    private void addVelocityTracker(MotionEvent event) {
        if (velocityTracker == null)
            velocityTracker = VelocityTracker.obtain();

        velocityTracker.addMovement(event);
    }

    private int getScrollXVelocity() {
        velocityTracker.computeCurrentVelocity(1000, maximumVelocity);
        int velocity = (int) velocityTracker.getXVelocity();
        return velocity;
    }

    private void recycleVelocityTracker() {
        if (velocityTracker != null) {
            velocityTracker.recycle();
            velocityTracker = null;
        }
    }

    private void reDraw() {
        // curIndex需要偏移的量
        int i = (int) (offsetX / (textWidth + textPadding));
        if ( (curIndex - i >= 0 && curIndex - i < dataList.size())) {
            if (offsetIndex != i) {
                offsetIndex = i;
                if (null != onScrollChangedListener)
                    onScrollChangedListener.onScrollChanged(getNowIndex(-offsetIndex));
            }
            postInvalidate();
        } else {
            finishScroll();
        }
    }

    private void finishScroll() {
        // 判断结束滑动后应该停留在哪个位置
        int centerPadding = textHeight + textPadding;
        float v = offsetX % centerPadding;
        if (v > 0.5f * centerPadding)
            ++offsetIndex;
        else if (v < -0.5f * centerPadding)
            --offsetIndex;

        // 重置curIndex
        curIndex = getNowIndex(-offsetIndex);

        // 计算回弹的距离
        bounceDistance = offsetIndex * centerPadding - offsetX;
        offsetX += bounceDistance;

        // 更新
        if (null != onScrollChangedListener)
            onScrollChangedListener.onScrollFinished(curIndex);

        // 重绘
        reset();
        postInvalidate();
    }

    private void reset() {
        offsetX = 0;
        oldOffsetX = 0;
        offsetIndex = 0;
        bounceDistance = 0;
    }

    private int getNowIndex(int offsetIndex) {
        int index = curIndex + offsetIndex;
        if (false) {
            if (index < 0)
                index = (index + 1) % dataList.size() + dataList.size() - 1;
            else if (index > dataList.size() - 1)
                index = index % dataList.size();
        } else {
            if (index < 0)
                index = 0;
            else if (index > dataList.size() - 1)
                index = dataList.size() - 1;
        }
        return index;
    }

    public void setDataList(List<String> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        // 更新maxTextWidth
        if (null != dataList && dataList.size() > 0) {
            int size = dataList.size();
            for (int i = 0; i < size; i++) {
                int tempWidth = (int) textPaint.measureText(dataList.get(i));
                if (tempWidth > textWidth)
                    textWidth = tempWidth;
            }
            curIndex = 0;
        }
        requestLayout();
        invalidate();
    }

    /**
     * 滚动发生变化时的回调接口
     */
    public interface OnScrollChangedListener {
        public void onScrollChanged(int curIndex);

        public void onScrollFinished(int curIndex);
    }

    private OnScrollChangedListener onScrollChangedListener;

    public void setOnScrollChangedListener(OnScrollChangedListener onScrollChangedListener) {
        this.onScrollChangedListener = onScrollChangedListener;
    }
}
