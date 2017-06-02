package com.administrator.chifanbao.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class HistogramView extends View{


    public HistogramView(Context context) {
        super(context);

        init(context, null);
    }

    public HistogramView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }
    private int step = 2;
    private Paint axisLinePaint;
    private Paint hLinePaint;
    private Paint titlePaint;
    private Paint recPaint;
    private void init(Context context, AttributeSet attrs){
        axisLinePaint = new Paint();
        hLinePaint = new Paint();
        titlePaint = new Paint();
        recPaint = new Paint();
        axisLinePaint.setColor(Color.DKGRAY);
        hLinePaint.setColor(Color.LTGRAY);
        titlePaint.setColor(Color.BLACK);

    }


    public void updateThisData(int[] thisData){
        thisYear = thisData;
        this.postInvalidate();  //可以子线程 更新视图的方法调用。
    }

    public void updateLastData(int[] lastData)
    {
        lastYear = lastData;
        this.postInvalidate();  //可以子线程 更新视图的方法调用。
    }


    private String[] yTitleDataStrings =
            new String[]{"10000","20000","30000","50000","0"};

    private String[] xTitleDataStrings =
            new String[]{"1","2","3","4","5","6","7"};

    private int[] thisYear = new int[xTitleDataStrings.length];
    private int[] lastYear = new int[xTitleDataStrings.length];
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        canvas.drawLine(100, 10, 100, 320, axisLinePaint);

        canvas.drawLine(100, 320, width-10 , 320, axisLinePaint);


        int leftHeight = 300;
        int hPerHeight = leftHeight/4;

        hLinePaint.setTextAlign(Align.CENTER);
        for(int i=0;i<4;i++){
            canvas.drawLine(100, 20+i*hPerHeight, width-10, 20+i*hPerHeight, hLinePaint);
        }
        FontMetrics metrics =titlePaint.getFontMetrics();
        int descent = (int)metrics.descent;
        titlePaint.setTextAlign(Align.RIGHT);
        for(int i=0;i<yTitleDataStrings.length;i++){
            canvas.drawText(yTitleDataStrings[i], 80, 20+i*hPerHeight+descent, titlePaint);
        }

 		//No.1
 		//开始写代码，请根据上下文代码，在这里实现绘制X轴坐标。
// 		int xAxisLength =
        for(int i=0;i<xTitleDataStrings.length;i++){
            canvas.drawText(xTitleDataStrings[i], 180+i*hPerHeight+descent, 340, titlePaint);
            step++;
            thisYear[i] = i;
            lastYear[i] = i;
            updateThisData(thisYear);
        }
//        updateLastData(lastYear);










        //end_code

        if(thisYear != null && thisYear.length >0){
            int thisCount = thisYear.length;

            for(int i=0;i<thisCount;i++){
                int value = thisYear[i];
                int num = 8 - value / 10000 ;
                recPaint.setColor(0xFF1078CF);
                Rect rect = new Rect();
                rect.left  = 100 + step * (i+1)  - 10;
                rect.right = 100 + step * (i+1)  + 10;
                int rh = (leftHeight * num) / 8 ;
                rect.top = rh + 20;
                rect.bottom = 320 ;
                canvas.drawRect(rect, recPaint);
            }
        }


        if(lastYear != null && lastYear.length >0) {
            int thisCount = lastYear.length;
            for(int i=0;i<thisCount;i++){
                int value = lastYear[i];
                int num = 8 - value / 10000 ;
                recPaint.setColor(0xFFAA1122);
                Rect rect = new Rect();
                rect.left  = 100 + step * (i+1)  - 10;
                rect.right = 100 + step * (i+1)  + 10;
                int rh = (leftHeight * num) / 8 ;
                rect.top = rh + 20;
                rect.bottom = 320 ;
                canvas.drawRect(rect, recPaint);
            }
        }
    }
}