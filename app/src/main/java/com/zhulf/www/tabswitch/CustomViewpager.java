package com.zhulf.www.tabswitch;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;

/**
 * Created by Administrator on 2018/3/27.
 */

public class CustomViewpager extends ViewPager {
    private static final float DEFAULT_TEXT_SIZE = 12;
    private Bitmap bitmap;
    private String tabTitle;

    public CustomViewpager(Context context) {
        super(context);
    }

    public CustomViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.CustomTab);

        //标签标题
        tabTitle = typedArray.getString(R.styleable.CustomTab_text);

        //标签图片的bitmap类型
        BitmapDrawable bitmapDrawable = (BitmapDrawable) typedArray.getDrawable(R.styleable.CustomTab_image);
        if(bitmapDrawable != null) {
            bitmap = bitmapDrawable.getBitmap();
        }

        //标签标题大小
        int textSize = (int) typedArray.getDimension(R.styleable.CustomTab_textSize,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,DEFAULT_TEXT_SIZE,getResources().getDisplayMetrics()));

        //回收typedArray便于将来使用
        typedArray.recycle();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
