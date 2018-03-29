package com.zhulf.www.tabswitch;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by Administrator on 2018/3/27.
 */

public class CustomView extends View {
    private static final float DEFAULT_TEXT_SIZE = 12;
    private final int DEFAULT_TEXT_COLOR = 0x2B2B2B;
    private final int DEFAULT_IMAGE_COLOR = 0x3CAF36;
    private int targetColor;
    private Paint targetPaint;
    private Paint tabTextPaint;
    private Rect tabTextBounds;
    private Bitmap tabBitmap;
    private String tabTitle;
    private Rect tabImageRect;
    private float mAlpha = 1.0f;

    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomTab);

        //标签标题
        tabTitle = typedArray.getString(R.styleable.CustomTab_text);

        //标签图片的bitmap类型
        BitmapDrawable bitmapDrawable = (BitmapDrawable) typedArray.getDrawable(R.styleable.CustomTab_image);
        if (bitmapDrawable != null) {
            tabBitmap = bitmapDrawable.getBitmap();
        }

        //图标绿色
        targetColor = typedArray.getColor(R.styleable.CustomTab_color,DEFAULT_IMAGE_COLOR);

        //标签标题大小
        int textSize = (int) typedArray.getDimension(R.styleable.CustomTab_size,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, DEFAULT_TEXT_SIZE, getResources().getDisplayMetrics()));

        //回收typedArray便于将来使用
        typedArray.recycle();


        tabTextPaint = new Paint();
        tabTextPaint.setTextSize(textSize);
        tabTextPaint.setColor(DEFAULT_TEXT_COLOR);
        tabTextPaint.setDither(true);
        tabTextPaint.setAntiAlias(true);
        tabTextBounds = new Rect();
        tabTextPaint.getTextBounds(tabTitle, 0, tabTitle.length(), tabTextBounds);

        targetPaint = new Paint();
        targetPaint.setColor(targetColor);
        targetPaint.setAntiAlias(true);
        targetPaint.setDither(true);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int tabImageMeasuredLength = Math.min(getMeasuredWidth() - getPaddingLeft()
                - getPaddingRight(), getMeasuredHeight() - tabTextBounds.height() -
                getPaddingTop() - getPaddingBottom());
        int left = (getMeasuredWidth() - tabImageMeasuredLength) / 2;
        int top = (getMeasuredHeight() - tabTextBounds.height() - tabImageMeasuredLength) / 2;
        tabImageRect = new Rect(left, top, left + tabImageMeasuredLength,
                top + tabImageMeasuredLength);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        int alphaValue = getIntAlphaValue(mAlpha);
        drawTabImage(canvas,alphaValue);
        drawTabText(canvas,alphaValue);
    }

    private void drawTabImage(Canvas canvas, int alphaValue) {
        canvas.drawBitmap(tabBitmap, null, tabImageRect, null);

        Bitmap targetBitmap = Bitmap.createBitmap(getMeasuredWidth(),getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas targetCanvas = new Canvas(targetBitmap);

        targetPaint.setAlpha(alphaValue);
        targetCanvas.drawRect(tabImageRect,targetPaint);

        targetPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        targetPaint.setAlpha(255);
        targetCanvas.drawBitmap(tabBitmap,null,tabImageRect,targetPaint);

        canvas.drawBitmap(targetBitmap,0,0,null);

    }

    private void drawTabText(Canvas canvas, int alphaValue) {

    }

    public void setTabImageAlpha(float alpha) {
        if(mAlpha != alpha) {
            mAlpha = alpha;
        }
    }

    private int getIntAlphaValue(float alpha) {
        return (int) Math.ceil(255 * alpha);
    }



}
