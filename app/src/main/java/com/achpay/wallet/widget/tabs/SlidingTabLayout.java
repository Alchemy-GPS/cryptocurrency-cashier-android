package com.achpay.wallet.widget.tabs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.achpay.wallet.R;
import com.achpay.wallet.utils.AppUtil;
import com.achpay.wallet.utils.Log;

import java.lang.reflect.Field;

public class SlidingTabLayout extends TabLayout {
    /**
     * 每个tab的宽度
     */
    private int tabWidth;
    /**
     * 屏幕宽度
     */
    private int mScreenWidth;
    /**
     * 自定义指示器
     */
    private Bitmap mSlideIcon;
    /**
     * 滑动过程中指示器的水平偏移量
     */
    private int mTranslationX;
    /**
     * 指示器初始X偏移量
     */
    private int mInitTranslationX;
    /**
     * 指示器初始Y偏移量
     */
    private int mInitTranslationY;
    /**
     * 默认的页面可见的tab数量
     */
    private static final int COUNT_DEFAULT_VISIBLE_TAB = 2;
    /**
     * 默认最后一个tab露出百分比
     */
    private static final float RATIO_DEFAULT_LAST_VISIBLE_TAB = 0f;
    /**
     * 页面可见的tab数量，默认4个
     */
    private int mTabVisibleCount = COUNT_DEFAULT_VISIBLE_TAB;
    /**
     * 最后一个tab露出百分比
     */
    private float mLastTabVisibleRatio = RATIO_DEFAULT_LAST_VISIBLE_TAB;

    public SlidingTabLayout(Context context) {
        super(context);
    }

    public SlidingTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

//        指示器
//        this.mSlideIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.tab_indicator);
//        this.mSlideIcon = Bitmap.createScaledBitmap(mSlideIcon, mSlideIcon.getWidth(), (int) (mSlideIcon.getHeight() * 0.8), true);

        mSlideIcon = AppUtil.drawableToBitmap(context, R.drawable.tablayout_indicator);

        this.mScreenWidth = getResources().getDisplayMetrics().widthPixels;

        //方案1：反射修改Tab宽度
        reflectiveModifyTabWidth();

        //方案2：异步修改Tab宽度
        /*post(new Runnable() {
            @Override
            public void run() {
                resetTabParams();
            }
        });*/
    }

    /**
     * 重绘下标
     */
    public void redrawIndicator(int position, float positionOffset) {
        mTranslationX = (int) ((position + positionOffset) * tabWidth);

        invalidate();
    }

    public void setmSlideIcon(Bitmap mSlideIcon) {
        this.mSlideIcon = mSlideIcon;
    }

    /**
     * tab的父容器，注意空指针
     */
    @Nullable
    public LinearLayout getTabStrip() {
        Class<?> tabLayout = TabLayout.class;
        Field tabStrip;
        LinearLayout llTab = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");

            tabStrip.setAccessible(true);

            llTab = (LinearLayout) tabStrip.get(this);

            llTab.setClipChildren(false);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return llTab;
    }

    /**
     * 绘制指示器
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (mSlideIcon == null) {

            Log.i("mSlideIcon == null");

            return;
        }
        canvas.save();
        // 平移到正确的位置，修正tabs的平移量
        if (mInitTranslationY == 0 || mInitTranslationY < 0) {
            this.mInitTranslationY = (getHeight() - this.mSlideIcon.getHeight());
        }

        canvas.translate(mInitTranslationX + mTranslationX, this.mInitTranslationY);
        canvas.drawBitmap(this.mSlideIcon, 0, 0, null);
        canvas.restore();
        super.dispatchDraw(canvas);
    }

    private void reflectiveModifyTabWidth() {
        final Class<?> clz = TabLayout.class;
        try {
            final Field requestedTabMaxWidthField = clz.getDeclaredField("mRequestedTabMaxWidth");
            final Field requestedTabMinWidthField = clz.getDeclaredField("mRequestedTabMinWidth");

            tabWidth = (int) (mScreenWidth / (mTabVisibleCount + mLastTabVisibleRatio));

            requestedTabMaxWidthField.setAccessible(true);
            requestedTabMaxWidthField.set(this, (int) (mScreenWidth / (mTabVisibleCount + mLastTabVisibleRatio)));

            requestedTabMinWidthField.setAccessible(true);
            requestedTabMinWidthField.set(this, (int) (mScreenWidth / (mTabVisibleCount + mLastTabVisibleRatio)));

            this.mInitTranslationX = tabWidth / 2 - this.mSlideIcon.getWidth() / 2;

        } catch (final NoSuchFieldException e) {
            e.printStackTrace();
        } catch (final SecurityException e) {
            e.printStackTrace();
        } catch (final IllegalArgumentException e) {
            e.printStackTrace();
        } catch (final IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 重设tab宽度
     */
    private void resetTabParams() {
        LinearLayout tabStrip = getTabStrip();
        if (tabStrip == null) {

            Log.i("tabStrip == null");

            return;
        }
        for (int i = 0; i < tabStrip.getChildCount(); i++) {
            LinearLayout tabView = (LinearLayout) tabStrip.getChildAt(i);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) (mScreenWidth / (mTabVisibleCount + mLastTabVisibleRatio)), LinearLayout.LayoutParams.MATCH_PARENT);
            tabView.setLayoutParams(params);
            //tab中的图标可以超出父容器
            tabView.setClipChildren(false);
            tabView.setClipToPadding(false);

            tabView.setPadding(0, 0, 0, 0);
        }
        initTranslationParams(tabStrip, mScreenWidth);
    }

    /**
     * 初始化下标的坐标参数
     */
    private void initTranslationParams(LinearLayout llTab, int screenWidth) {
        if (mSlideIcon == null) {
            return;
        }

        tabWidth = (int) (screenWidth / (mTabVisibleCount + mLastTabVisibleRatio));
        View firstView = llTab.getChildAt(0);
        if (firstView != null) {
            this.mInitTranslationX = (firstView.getLeft() + tabWidth / 2 - this.mSlideIcon.getWidth() / 2);
            this.mInitTranslationY = (getBottom() - getTop() - this.mSlideIcon.getHeight());
        }
    }
}
