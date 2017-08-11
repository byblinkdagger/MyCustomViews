package dagger.blink.com.customviewlibrary.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import dagger.blink.com.customviewlibrary.util.DensityUtil;


/**
 * Created by lucky on 2017/8/11.
 */
public class ScanningView extends View {

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    String colorValue = "#135a5e";
    int radius = 0;
    int minRadius = 0;
    int paintWidth;
    int duration = 5000;

    ValueAnimator valueAnimator;

    public ScanningView(Context context) {
        this(context,null);
    }

    public ScanningView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        paint.setColor(Color.parseColor(colorValue));
        paint.setStyle(Paint.Style.STROKE);
        paintWidth = DensityUtil.dip2px(getContext(),2);
        paint.setStrokeWidth(paintWidth);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        minRadius = Math.min(w,h)/10;
        startAnim();
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        int count = radius / minRadius;
        for (int i = 0; i < count+1; i++){
            canvas.drawCircle(getWidth()/2,getHeight()/2,radius-i*minRadius,paint);
        }
    }

    public void startAnim(){
        valueAnimator = ValueAnimator.ofInt(0,5*minRadius);
        valueAnimator.setDuration(duration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                radius= (int) valueAnimator.getAnimatedValue();
                if (radius == 5 * minRadius){
                    valueAnimator.cancel();
                    startAnim();
                }
                invalidate();
            }
        });
        valueAnimator.start();
    }

    public void stopAnim(){
        if (valueAnimator != null){
            valueAnimator.cancel();
            radius = 0;
        }
    }
}
