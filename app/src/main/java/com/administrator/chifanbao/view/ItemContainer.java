package com.administrator.chifanbao.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.administrator.chifanbao.utils.DensityUtils;

/**
 * Created by Administrator on 2017/3/6.
 */

public class ItemContainer extends LinearLayout {
    private int width;//组件宽
    private int height;//组件高
    private int childCount;
    private int childMarginLeft = DensityUtils.dp2px(getContext(),8);//子控件相对左边控件的距离
    private int childMarginHorizonTal = DensityUtils.dp2px(getContext(),10);//子控件相对最左、最右的距离
    private int childMarginTop = DensityUtils.dp2px(getContext(),8);//子控件相对顶部控件的距离
    private int childWidth;//子控件宽
    private int childHeight;//子控件高
    private static final int CHILD_NUM = 3;   //每行子控件的数量

    public ItemContainer(Context context) {
        super(context);
    }

    public ItemContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        childCount = getChildCount();//得到子控件数量
        if(childCount>0) {
            childWidth = (width - childMarginLeft * CHILD_NUM) / CHILD_NUM;
            childHeight = DensityUtils.dp2px(getContext(),25);//给子控件的高度一个定值

            //根据子控件的高和子控件数目得到自身的高
            height = childHeight * ((childCount-1)/ CHILD_NUM+1) + childMarginHorizonTal * 2 + childMarginTop*((childCount-1)/CHILD_NUM);
        }else {
            //如果木有子控件，自身高度为0，即不显示
            height = 0;
        }
        width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        //根据自身的宽度约束子控件宽度
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        //设置自身宽度
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        /**
         * 遍历所有子控件，并设置它们的位置和大小
         * 每行只能有三个子控件，且高度固定，宽度相同，且每行正好布满
         */
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);//得到当前子控件
            childView.layout((i%CHILD_NUM) * childWidth + (i%CHILD_NUM+1)*childMarginLeft
                    , (i / CHILD_NUM)*childHeight + childMarginHorizonTal + (i / CHILD_NUM)*childMarginTop
                    , (i%CHILD_NUM+1) * childWidth + (i%CHILD_NUM+1)*childMarginLeft
                    , (i / CHILD_NUM+1)*childHeight + childMarginHorizonTal + (i / CHILD_NUM)*childMarginTop);
        }
    }

}
