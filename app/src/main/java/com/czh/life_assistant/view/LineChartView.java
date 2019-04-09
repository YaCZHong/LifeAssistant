package com.czh.life_assistant.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.czh.life_assistant.R;

import java.util.ArrayList;

public class LineChartView extends View {
    private int mBackgroundColor;
    private int mMaxCircleColor;
    private int mMinCircleColor;
    private int mLineWidth;
    private int mAverageCircleRadius;
    private int mLineColor;
    private int mCoordinatesTextSize;
    private int mCoordinatesTextColor;
    private int mCoordinatesLineWidth;
    private int mWidth;
    private int mHeight;

    private Paint mPaint;

    private Rect textBound;

    public void setxValues(ArrayList<String> xValues) {
        this.xValues = xValues;
    }

    public void setValues(ArrayList<Float> values) {
        this.values = values;
    }

    private ArrayList<String> xValues = new ArrayList<>();
    private ArrayList<Float> yValues = new ArrayList<>();

    private Paint textPaint;

    private ArrayList<Float> values = new ArrayList<>();

    private Paint linePaint;

    private Paint maxCirclePaint;

    private Paint minCirclePaint;

    private float XScale;
    private float YScale;

    private float XSection;

    private float YSection;
    private float YBlankSection;

    private float max_dv;

    public LineChartView(Context context) {
        this(context, null);
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LineChartView, defStyleAttr, 0);

        int count = array.getIndexCount();

        for (int index = 0; index < count; index++) {
            int attr = array.getIndex(index);

            switch (attr) {
                case R.styleable.LineChartView_coordinatesLineWidth:
                    mCoordinatesLineWidth = array.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 2, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.LineChartView_coordinatesTextColor:
                    mCoordinatesTextColor = array.getColor(attr, Color.parseColor("#607d8b"));
                    break;
                case R.styleable.LineChartView_coordinatesTextSize:
                    mCoordinatesTextSize = array.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 11, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.LineChartView_lineColor:
                    mLineColor = array.getColor(attr, Color.BLUE);
                    break;
                case R.styleable.LineChartView_lineWidth:
                    mLineWidth = array.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 11, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.LineChartView_averageCircleRadius:
                    mAverageCircleRadius = array.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.LineChartView_maxCircleColor:
                    mMaxCircleColor = array.getColor(attr, Color.GREEN);
                    break;
                case R.styleable.LineChartView_minCircleColor:
                    mMinCircleColor = array.getColor(attr, Color.WHITE);
                    break;
                case R.styleable.LineChartView_backgroundColor:
                    mBackgroundColor = array.getColor(attr, Color.parseColor("#ffffff"));
                    break;
            }
        }
        array.recycle();
        init();
    }

    private void init() {

        /*
        * 默认值
        * */
        xValues.add("01");
        xValues.add("02");
        xValues.add("03");
        xValues.add("04");
        xValues.add("05");
        xValues.add("06");
        xValues.add("07");
        xValues.add("08");
        xValues.add("09");
        xValues.add("10");
        xValues.add("11");
        xValues.add("12");
        xValues.add("13");
        xValues.add("14");
        xValues.add("15");
        xValues.add("16");
        xValues.add("17");
        xValues.add("18");
        xValues.add("19");
        xValues.add("20");
        xValues.add("21");
        xValues.add("22");
        xValues.add("23");
        xValues.add("24");

        values.add(0f);
        values.add(0f);
        values.add(0f);
        values.add(0f);
        values.add(0f);
        values.add(0f);
        values.add(0f);
        values.add(0f);
        values.add(0f);
        values.add(0f);
        values.add(0f);
        values.add(0f);
        values.add(0f);
        values.add(0f);
        values.add(0f);
        values.add(0f);
        values.add(0f);
        values.add(0f);
        values.add(0f);
        values.add(0f);
        values.add(0f);
        values.add(0f);
        values.add(0f);
        values.add(0f);


        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mCoordinatesTextColor);
        mPaint.setStrokeWidth(mCoordinatesLineWidth);
        mPaint.setStyle(Paint.Style.FILL);
        //mPaint.setShadowLayer(2,1,1,Color.parseColor("#424242"));

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(mCoordinatesTextColor);
        textPaint.setTextSize(mCoordinatesTextSize);
        textPaint.setStyle(Paint.Style.STROKE);
        textBound = new Rect();
        //textPaint.setShadowLayer(2,1,1,Color.parseColor("#424242"));

        minCirclePaint = new Paint();
        minCirclePaint.setAntiAlias(true);
        minCirclePaint.setColor(Color.WHITE);
        minCirclePaint.setStyle(Paint.Style.FILL);

        maxCirclePaint = new Paint();
        maxCirclePaint.setAntiAlias(true);
        maxCirclePaint.setColor(mMaxCircleColor);
        maxCirclePaint.setStyle(Paint.Style.FILL);

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(mLineColor);
        linePaint.setStrokeWidth(mLineWidth);
        linePaint.setStyle(Paint.Style.STROKE);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthSpecMode == MeasureSpec.EXACTLY) {
            mWidth = widthSpecSize;
        } else {
            mWidth = 300;
        }

        if (heightSpecMode == MeasureSpec.EXACTLY) {
            mHeight = heightSpecSize;
        } else {
            mHeight = (mWidth / 5) * 3;
        }

        //Log.d("onMeasure", "onMeasure: " + "mWidth:" + mWidth + "，mHeight:" + mHeight);
        setMeasuredDimension(mWidth, mHeight);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initchart();
        canvas.drawColor(mBackgroundColor);
        drawCoordinates(canvas);
        drawCoordinatesXValues(canvas);
        drawLine(canvas);
    }

    private void initchart() {

        /*
         * 折线图X轴的总区间
         * */
        XSection = (mWidth - getPaddingEnd() - getPaddingStart());


        /*
         * 单位区间的大小
         * */
        XScale = XSection / (xValues.size() - 1);

        /*
         * 折线图Y轴的区间，这样可以与顶部与底部有些距离，美观一些
         * */
        YSection = (mHeight - getPaddingBottom() - getPaddingTop()) / 6 * 4;
        YBlankSection = (mHeight - getPaddingBottom() - getPaddingTop()) / 6;

        /*
         * 待描绘的点的最大差值
         * */
        max_dv = getMax(values) - getMin(values);

        /*
         * 简陋的自适应调整
         * */
        if (max_dv >= 5) {

            /*
             * 将折线图的区间进行 max_dv 等分，YScale表示max_dv等分后每一个小区间的大小
             * */
            YScale = YSection / max_dv;

        } else {

            /*
             *把折线空间5等分，坐标值差为1对应一个YScale的高度，坐标值差为 K 对应一个 k * YScale 的高度这样，就能控制坐标点都在坐标系内
             * */
            YScale = YSection / 5;

        }
    }

    /**
     * 画上坐标
     *
     * @param canvas
     */
    private void drawCoordinates(Canvas canvas) {
        /*
         * x轴
         * */
        canvas.drawLine(getPaddingStart()-1, mHeight - getPaddingBottom(), mWidth - getPaddingEnd()+1, mHeight - getPaddingBottom(), mPaint);
    }

    /**
     * 绘制X轴上的刻度
     *
     * @param canvas
     */
    private void drawCoordinatesXValues(Canvas canvas) {

        for (int i = 0; i < xValues.size(); i++) {

            // 获取到文字边框 textBound
            textPaint.getTextBounds(xValues.get(i), 0, xValues.get(i).length(), textBound);

            // 画间断线
            canvas.drawLine(getPaddingStart() + (i * XScale),
                    mHeight - getPaddingBottom() - 10,
                    getPaddingStart() + (i * XScale),
                    mHeight - getPaddingBottom(), mPaint);

            // -textBound.width()/2 是为了让文字在间断线下方居中
            canvas.drawText(xValues.get(i),
                    getPaddingStart() + (i * XScale) - textBound.width() / 2,
                    mHeight - getPaddingBottom() + 48, textPaint);
        }
    }

    /**
     * @param canvas
     */
    private void drawLine(Canvas canvas) {

        int j;

        for (int i = 0; i < values.size(); i++) {

            float valueScale = (values.get(i) - getMin(values));

            /**
             * 画折线
             */
            if (i < values.size() - 1) {

                int nextValueScale = (int) (values.get(i + 1) - getMin(values));

                j = i + 1;

                canvas.drawLine(getPaddingStart() + (XScale * i),
                        mHeight - getPaddingBottom() - YBlankSection - (YScale * valueScale),
                        getPaddingStart() + (XScale * j),
                        mHeight - getPaddingBottom() - YBlankSection - (YScale * nextValueScale),
                        linePaint);
            }

            String text = String.valueOf(values.get(i)).replace(".0", "°");
            textPaint.getTextBounds(text, 0, text.length(), textBound);

            //画坐标点标签
            canvas.drawText(text,
                    getPaddingStart() + (XScale * i) - textBound.width() / 2,
                    mHeight - getPaddingBottom() - YBlankSection - (YScale * valueScale) - 45,
                    textPaint);

            /**
             * 两个小圆点
             */
            canvas.drawCircle(getPaddingStart() + (XScale * i),
                    mHeight - getPaddingBottom() - YBlankSection - (YScale * valueScale),  dp2px(4),
                    maxCirclePaint);
            canvas.drawCircle(getPaddingStart() + (XScale * i),
                    mHeight - getPaddingBottom() - YBlankSection - (YScale * valueScale),  dp2px(3),
                    minCirclePaint);

        }

    }

    private float getMax(ArrayList<Float> data) {
        float max = data.get(0);
        for (int i = 1; i < data.size(); i++) {
            if (max < data.get(i)) {
                max = data.get(i);
            }
        }
        return max;
    }

    private float getMin(ArrayList<Float> data) {
        float min = data.get(0);
        for (int i = 1; i < data.size(); i++) {
            if (min > data.get(i)) {
                min = data.get(i);
            }
        }
        return min;
    }

    public void setChange() {
        this.invalidate();
    }
    /**
     * dp 2 px
     *
     * @param dpVal
     */
    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }
}
