package com.zhulf.www.tabswitch;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by Administrator on 2018/3/27.
 */

public class CustomView extends View {
    private static final float DEFAULT_TEXT_SIZE = 12;//标签字体大小
    private final int DEFAULT_TEXT_COLOR = 0x9A9A9A;//灰色
    private final int DEFAULT_IMAGE_COLOR = 0x3CAF36;//绿色


    /**
     * 标签文字
     */
    private String tabTitle;

    /**
     * 图标默认颜色
     */
    private int targetColor;

    /**
     * 标签文字的画笔
     */
    private Paint tabTextPaint;

    /**
     * 标签文字最小矩形边界
     */
    private Rect tabTextBounds;

    /**
     * 标签图标
     */
    private Bitmap tabBitmap;

    /**
     * 标签图标的矩形边界
     */
    private Rect tabImageRect;

    /**
     * 改变图标,文字的透明度值,取值范围:[0.0f,1.0f]
     */
    private float mAlpha;

    /**
     * 用于图片混合模式处理(PorterDuff)的一个位图对象
     */
    private Bitmap targetBitmap;

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

        //图标的颜色，如果没有指定，默认为绿色
        targetColor = typedArray.getColor(R.styleable.CustomTab_color, DEFAULT_IMAGE_COLOR);

        //标签标题大小，如果没有指定，默认12像素
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
        //获取标题的矩形边界，保存到tabTextBounds中
        tabTextPaint.getTextBounds(tabTitle, 0, tabTitle.length(), tabTextBounds);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //比较每个标签(总共4个自定义标签CustomView)的宽度和高度，用最小的值作为图标的正方形边长
        int tabImageMeasuredLength = Math.min(getMeasuredWidth() - getPaddingLeft()
                - getPaddingRight(), getMeasuredHeight() - tabTextBounds.height() -
                getPaddingTop() - getPaddingBottom());
        //计算图标正方形的坐标,left,top是左上角,right,bottom是右下角
        int left = (getMeasuredWidth() - tabImageMeasuredLength) / 2;
        int top = (getMeasuredHeight() - tabTextBounds.height() - tabImageMeasuredLength) / 2;
        int right = left + tabImageMeasuredLength;
        int bottom = top + tabImageMeasuredLength;
        //创建正方形对象
        tabImageRect = new Rect(left, top, right, bottom);

    }

    /**
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        int alphaValue = getIntAlphaValue(mAlpha);
        //画出原始图标
        canvas.drawBitmap(tabBitmap, null, tabImageRect, null);
        //画出混合模式下的图标
        drawXfermodeTabImage(alphaValue);
        //画出初始状态下的文字
        drawInitialTabText(canvas, alphaValue);
        //画出滑动后状态下的文字
        drawFinalTabText(canvas, alphaValue);
        //把创建的图片targetBitmap画到画板上
        canvas.drawBitmap(targetBitmap, 0, 0, null);
    }

    /**
     * 绘制图片混合模式
     * 方法：
     * 1. 创建一个指定bitmap大小的画布，此处不能用onDraw的参数canvas
     * 2. 先在画布上画出一个矩形背景
     * 3. 给paint设置图片混合模式，用该画笔画出混合后的图标
     * 注意：这三步虽然画出了混合模式下的图标，但并没有显示出来，要让图标显示出来
     * 必须要把图标所在的bitmap对象再画到自定义CustomView的画板上才能显示，这一步在
     * onDraw方法的最后一句：
     * canvas.drawBitmap(targetBitmap, 0, 0, null);
     *
     * @param alphaValue
     */
    private void drawXfermodeTabImage(int alphaValue) {
        targetBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Config.ARGB_8888);
        Canvas targetCanvas = new Canvas(targetBitmap);
        Paint paint = new Paint();
        paint.setColor(targetColor);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setAlpha(alphaValue);
        //先画出一个DST目标图形，即一个不透明的矩形，颜色为绿色
        targetCanvas.drawRect(tabImageRect, paint);
        //再设置画笔为混合模式DST_IN
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        paint.setAlpha(255);
        //在新创建的画布上画出具体的图标tabBitmap，位置以上面的矩形为参考，此时
        //通过混合模式画出来的图形是矩形和图标的交集并且填充颜色为绿色
        //虽然画出了混合模式的图标，但是还不能显示出来，因为新创建的图片targetBitmap尚未
        //添加到CustomView中，显示不出来。
        targetCanvas.drawBitmap(tabBitmap, null, tabImageRect, paint);
    }

    /**
     * 绘制初始情况下的文字
     *
     * @param canvas
     * @param alphaValue
     */
    private void drawInitialTabText(Canvas canvas, int alphaValue) {
        tabTextPaint.setColor(DEFAULT_TEXT_COLOR);
        tabTextPaint.setAlpha(255 - alphaValue);
        canvas.drawText(tabTitle, tabImageRect.left + tabImageRect.width() / 2 - tabTextBounds.width() / 2,
                tabImageRect.bottom + tabTextBounds.height(), tabTextPaint);
    }

    /**
     * 滑动到某一页时，绘制该页对应标签上的文字
     *
     * @param canvas
     * @param alphaValue
     */
    private void drawFinalTabText(Canvas canvas, int alphaValue) {
        tabTextPaint.setColor(targetColor);
        tabTextPaint.setAlpha(alphaValue);
        canvas.drawText(tabTitle, tabImageRect.left + tabImageRect.width() / 2 - tabTextBounds.width() / 2,
                tabImageRect.bottom + tabTextBounds.height(), tabTextPaint);
    }

    /**
     * 替换透明度并重画view
     *
     * @param alpha
     */
    public void setTabAlpha(float alpha) {
        if (mAlpha != alpha) {
            mAlpha = alpha;
            invalidate();
        }
    }

    private void invalidateView() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }

    /**
     * 把[0.0f,1.0f]区间的透明度转化成[0,255]之间的整形值
     *
     * @param alpha
     * @return
     */
    private int getIntAlphaValue(float alpha) {
        return (int) Math.ceil(255 * alpha);
    }

    /**
     * 根据drawableId获得Bitmap对象并替换相应的图标并重画view
     *
     * @param context
     * @param drawableId
     */
    public void setTabImage(Context context, int drawableId) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) context.getDrawable(drawableId);
        if (bitmapDrawable == null) {
            throw new NullPointerException("No Image, Image is not null");
        }
        if (!bitmapDrawable.getBitmap().equals(tabBitmap)) {
            tabBitmap = bitmapDrawable.getBitmap();
            invalidate();
        }
    }

}