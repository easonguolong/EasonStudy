package com.images.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/22.
 */

public class nineImageView extends ViewGroup {

    /**
     *储存所有的view，按行记录
     */
    private List<List<View>> mAllViews = new ArrayList<>();

    /**
     * mLineHeight  记录每一行的最大高度
     */
    private List<Integer> mLineHeight = new ArrayList<>();

    public nineImageView(Context context) {
        super(context);
    }

    public nineImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int totalWidth =0;
        int totalHeight =0;
        for (int i = 0; i <getChildCount() ; i++) {
            View child = getChildAt(i);
            measureChild(child,widthMeasureSpec,heightMeasureSpec);
            //得到child的lp
            MarginLayoutParams lp = (MarginLayoutParams)child.getLayoutParams();
            int childWidth = child.getWidth()+lp.leftMargin+lp.rightMargin;
            int childHeight = child.getHeight()+lp.topMargin+lp.bottomMargin;
            if (getChildCount()<=3){
                totalWidth +=childWidth;
                totalHeight+=childHeight;
            }else if (getChildCount()<=6){
                if (getChildCount()==4){
                    totalHeight = childHeight*2;
                    totalWidth = childWidth*2;
                }else{
                    totalHeight = childHeight*2;
                    totalWidth = childWidth*3;
                }
            }else if (getChildCount()<=9){
                totalHeight = childHeight*3;
                totalWidth = childWidth*3;
            }
        }
        setMeasuredDimension(totalWidth+getPaddingLeft()+getPaddingRight(),totalHeight+getPaddingBottom()+getPaddingTop());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
         //onLayout 会被调用多次，预防重叠
        mAllViews.clear();
        mLineHeight.clear();
        //获取总宽度
        int Width = getWidth();
        //单行宽度和当行高度
        int lineWidth =0;
        int lineHeight =0;
        //储存每一行所有的childView
        List<View> childViews = new ArrayList<>();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child  = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams)child.getLayoutParams();
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            //如果已经需要换行
            if (childWidth+lp.leftMargin+lp.rightMargin+lineWidth > Width){
                //记录这一行所有的View以及最大高度
                mLineHeight.add(lineHeight);
                //将当前行的childView保存，然后开启新的ArrayList保存下一行的childView
                mAllViews.add(childViews);
                 lineWidth = 0;
                childViews = new ArrayList<>();
            }
            //如果不需要换行，则累计
            lineWidth += childWidth + lp.rightMargin+lp.leftMargin;
            lineHeight = Math.max(lineHeight,childHeight+lp.topMargin+lp.bottomMargin);
            childViews.add(child);
        }
        mLineHeight.add(lineHeight);
        mAllViews.add(childViews);

        int left = getPaddingLeft();
        int top = getPaddingTop();

        int lineNum = mAllViews.size();

        for (int i = 0; i < lineNum ; i++) {
            //每一行的所有的views
            childViews = mAllViews.get(i);
            //当前行的最大高度
            lineHeight = mLineHeight.get(i);
            //遍历当前行所有的子view
            for (int j = 0; j < childViews.size(); j++) {
                View child  = childViews.get(j);
                if (child.getVisibility()!= View.GONE){
                    MarginLayoutParams lp = (MarginLayoutParams)child.getLayoutParams();
                    //计算childview的left等
                    int childLeft = left + lp.leftMargin ;
                    int childTop = top + lp.topMargin;
                    int childRight = childLeft + child.getMeasuredWidth() ;
                    int childBottom = childTop + child.getMeasuredHeight() ;
                    child.layout(childLeft,childTop,childRight,childBottom);

                    left+=child.getMeasuredWidth() + lp.rightMargin + lp.leftMargin;
                }
            }
            //换行后，重新从第一个开始，高度累加
            left = getPaddingTop();
            top += lineHeight;
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }
}
