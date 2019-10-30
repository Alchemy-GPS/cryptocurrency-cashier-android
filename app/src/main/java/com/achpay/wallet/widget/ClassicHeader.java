package com.achpay.wallet.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.internal.ProgressDrawable;
import com.scwang.smartrefresh.layout.internal.pathview.PathsDrawable;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class ClassicHeader extends RelativeLayout implements RefreshHeader {

    public static String REFRESH_HEADER_PULLDOWN = "下拉刷新";
    public static String REFRESH_HEADER_REFRESHING = "正在刷新...";
    public static String REFRESH_HEADER_LOADING = "正在加载...";
    public static String REFRESH_HEADER_RELEASE = "松开刷新";
    public static String REFRESH_HEADER_FINISH = "完成";
    public static String REFRESH_HEADER_FAILED = "失败";
    public static String REFRESH_HEADER_LASTTIME = "上次更新 M-d HH:mm";
    public static String REFRESH_HEADER_SECOND_FLOOR = "释放进入二楼";
//    public static String REFRESH_HEADER_LASTTIME = "'Last update' M-d HH:mm";

    protected String KEY_LAST_UPDATE_TIME = "LAST_UPDATE_TIME";

    protected Date mLastTime;
    protected TextView mTitleText;
    protected TextView mLastUpdateText;
    protected ImageView mArrowView;
    protected ImageView mProgressView;
    protected SharedPreferences mShared;
    protected RefreshKernel mRefreshKernel;
    protected PathsDrawable mArrowDrawable;
    protected ProgressDrawable mProgressDrawable;
    protected SpinnerStyle mSpinnerStyle = SpinnerStyle.Translate;
    protected DateFormat mFormat = new SimpleDateFormat(REFRESH_HEADER_LASTTIME, Locale.getDefault());
    protected Integer mAccentColor;
    protected Integer mPrimaryColor;
    protected int mBackgroundColor;
    protected int mFinishDuration = 500;
    protected int mPaddingTop = 20;
    protected int mPaddingBottom = 20;
    protected boolean mEnableLastTime = true;

    //<editor-fold desc="RelativeLayout">
    public ClassicHeader(Context context) {
        super(context);
        this.initView(context, null);
    }

    public ClassicHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context, attrs);
    }

    public ClassicHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initView(context, attrs);
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public ClassicHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        DensityUtil density = new DensityUtil();

        LinearLayout layout = new LinearLayout(context);
        layout.setId(android.R.id.widget_frame);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        layout.setOrientation(LinearLayout.VERTICAL);
        mTitleText = new TextView(context);
        mTitleText.setText(REFRESH_HEADER_PULLDOWN);
        mTitleText.setTextColor(0xff666666);

        mLastUpdateText = new TextView(context);
        mLastUpdateText.setTextColor(0xff7c7c7c);
        LinearLayout.LayoutParams lpHeaderText = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        layout.addView(mTitleText, lpHeaderText);
        LinearLayout.LayoutParams lpUpdateText = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        layout.addView(mLastUpdateText, lpUpdateText);

        RelativeLayout.LayoutParams lpHeaderLayout = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lpHeaderLayout.addRule(CENTER_IN_PARENT);
        addView(layout,lpHeaderLayout);

        RelativeLayout.LayoutParams lpArrow = new RelativeLayout.LayoutParams(density.dip2px(20), density.dip2px(20));
        lpArrow.addRule(CENTER_VERTICAL);
        lpArrow.addRule(LEFT_OF, android.R.id.widget_frame);
        mArrowView = new ImageView(context);
        addView(mArrowView, lpArrow);

        RelativeLayout.LayoutParams lpProgress = new RelativeLayout.LayoutParams((ViewGroup.LayoutParams)lpArrow);
        lpProgress.addRule(CENTER_VERTICAL);
        lpProgress.addRule(LEFT_OF, android.R.id.widget_frame);
        mProgressView = new ImageView(context);
        mProgressView.animate().setInterpolator(new LinearInterpolator());
        addView(mProgressView, lpProgress);

        if (isInEditMode()) {
            mArrowView.setVisibility(GONE);
            mTitleText.setText(REFRESH_HEADER_REFRESHING);
        } else {
            mProgressView.setVisibility(GONE);
        }

        TypedArray ta = context.obtainStyledAttributes(attrs, com.scwang.smartrefresh.layout.R.styleable.ClassicsHeader);

        lpUpdateText.topMargin = ta.getDimensionPixelSize(com.scwang.smartrefresh.layout.R.styleable.ClassicsHeader_srlTextTimeMarginTop, density.dip2px(0));
        lpProgress.rightMargin = ta.getDimensionPixelSize(com.scwang.smartrefresh.layout.R.styleable.ClassicsFooter_srlDrawableMarginRight, density.dip2px(20));
        lpArrow.rightMargin = lpProgress.rightMargin;

        lpArrow.width = ta.getLayoutDimension(com.scwang.smartrefresh.layout.R.styleable.ClassicsHeader_srlDrawableArrowSize, lpArrow.width);
        lpArrow.height = ta.getLayoutDimension(com.scwang.smartrefresh.layout.R.styleable.ClassicsHeader_srlDrawableArrowSize, lpArrow.height);
        lpProgress.width = ta.getLayoutDimension(com.scwang.smartrefresh.layout.R.styleable.ClassicsHeader_srlDrawableProgressSize, lpProgress.width);
        lpProgress.height = ta.getLayoutDimension(com.scwang.smartrefresh.layout.R.styleable.ClassicsHeader_srlDrawableProgressSize, lpProgress.height);

        lpArrow.width = ta.getLayoutDimension(com.scwang.smartrefresh.layout.R.styleable.ClassicsHeader_srlDrawableSize, lpArrow.width);
        lpArrow.height = ta.getLayoutDimension(com.scwang.smartrefresh.layout.R.styleable.ClassicsHeader_srlDrawableSize, lpArrow.height);
        lpProgress.width = ta.getLayoutDimension(com.scwang.smartrefresh.layout.R.styleable.ClassicsHeader_srlDrawableSize, lpProgress.width);
        lpProgress.height = ta.getLayoutDimension(com.scwang.smartrefresh.layout.R.styleable.ClassicsHeader_srlDrawableSize, lpProgress.height);

        mFinishDuration = ta.getInt(com.scwang.smartrefresh.layout.R.styleable.ClassicsHeader_srlFinishDuration, mFinishDuration);
        mEnableLastTime = ta.getBoolean(com.scwang.smartrefresh.layout.R.styleable.ClassicsHeader_srlEnableLastTime, mEnableLastTime);
        mSpinnerStyle = SpinnerStyle.values()[ta.getInt(com.scwang.smartrefresh.layout.R.styleable.ClassicsHeader_srlClassicsSpinnerStyle,mSpinnerStyle.ordinal())];

        mLastUpdateText.setVisibility(mEnableLastTime ? VISIBLE : GONE);

        if (ta.hasValue(com.scwang.smartrefresh.layout.R.styleable.ClassicsHeader_srlDrawableArrow)) {
            mArrowView.setImageDrawable(ta.getDrawable(com.scwang.smartrefresh.layout.R.styleable.ClassicsHeader_srlDrawableArrow));
        } else {
            mArrowDrawable = new PathsDrawable();
            mArrowDrawable.parserColors(0xff666666);
            mArrowDrawable.parserPaths("M20,12l-1.41,-1.41L13,16.17V4h-2v12.17l-5.58,-5.59L4,12l8,8 8,-8z");
            mArrowView.setImageDrawable(mArrowDrawable);
        }

        if (ta.hasValue(com.scwang.smartrefresh.layout.R.styleable.ClassicsHeader_srlDrawableProgress)) {
            mProgressView.setImageDrawable(ta.getDrawable(com.scwang.smartrefresh.layout.R.styleable.ClassicsHeader_srlDrawableProgress));
        } else {
            mProgressDrawable = new ProgressDrawable();
            mProgressDrawable.setColor(0xff666666);
            mProgressView.setImageDrawable(mProgressDrawable);
        }

        if (ta.hasValue(com.scwang.smartrefresh.layout.R.styleable.ClassicsHeader_srlTextSizeTitle)) {
            mTitleText.setTextSize(TypedValue.COMPLEX_UNIT_PX, ta.getDimensionPixelSize(com.scwang.smartrefresh.layout.R.styleable.ClassicsHeader_srlTextSizeTitle, DensityUtil.dp2px(16)));
        } else {
            mTitleText.setTextSize(16);
        }

        if (ta.hasValue(com.scwang.smartrefresh.layout.R.styleable.ClassicsHeader_srlTextSizeTime)) {
            mLastUpdateText.setTextSize(TypedValue.COMPLEX_UNIT_PX, ta.getDimensionPixelSize(com.scwang.smartrefresh.layout.R.styleable.ClassicsHeader_srlTextSizeTime, DensityUtil.dp2px(12)));
        } else {
            mLastUpdateText.setTextSize(12);
        }

        if (ta.hasValue(com.scwang.smartrefresh.layout.R.styleable.ClassicsHeader_srlPrimaryColor)) {
            setPrimaryColor(ta.getColor(com.scwang.smartrefresh.layout.R.styleable.ClassicsHeader_srlPrimaryColor, 0));
        }
        if (ta.hasValue(com.scwang.smartrefresh.layout.R.styleable.ClassicsHeader_srlAccentColor)) {
            setAccentColor(ta.getColor(com.scwang.smartrefresh.layout.R.styleable.ClassicsHeader_srlAccentColor, 0));
        }

        ta.recycle();

        if (getPaddingTop() == 0) {
            if (getPaddingBottom() == 0) {
                setPadding(getPaddingLeft(), mPaddingTop = density.dip2px(20), getPaddingRight(), mPaddingBottom = density.dip2px(20));
            } else {
                setPadding(getPaddingLeft(), mPaddingTop = density.dip2px(20), getPaddingRight(), mPaddingBottom = getPaddingBottom());
            }
        } else {
            if (getPaddingBottom() == 0) {
                setPadding(getPaddingLeft(), mPaddingTop = getPaddingTop(), getPaddingRight(), mPaddingBottom = density.dip2px(20));
            } else {
                mPaddingTop = getPaddingTop();
                mPaddingBottom = getPaddingBottom();
            }
        }

        try {//try 不能删除-否则会出现兼容性问题
            if (context instanceof FragmentActivity) {
                FragmentManager manager = ((FragmentActivity) context).getSupportFragmentManager();
                if (manager != null) {
                    @SuppressLint("RestrictedApi")
                    List<Fragment> fragments = manager.getFragments();
                    if (fragments != null && fragments.size() > 0) {
                        setLastUpdateTime(new Date());
                        return;
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        KEY_LAST_UPDATE_TIME += context.getClass().getName();
        mShared = context.getSharedPreferences("ClassicHeader", Context.MODE_PRIVATE);
        setLastUpdateTime(new Date(mShared.getLong(KEY_LAST_UPDATE_TIME, System.currentTimeMillis())));

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (View.MeasureSpec.getMode(heightMeasureSpec) == View.MeasureSpec.EXACTLY) {
            setPadding(getPaddingLeft(), 0, getPaddingRight(), 0);
        } else {
            setPadding(getPaddingLeft(), mPaddingTop, getPaddingRight(), mPaddingBottom);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    //</editor-fold>

    //<editor-fold desc="RefreshHeader">
    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int extendHeight) {
        mRefreshKernel = kernel;
        mRefreshKernel.requestDrawBackgroundForHeader(mBackgroundColor);
    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {
    }

    @Override
    public void onPulling(float percent, int offset, int height, int extendHeight) {
    }

    @Override
    public void onReleasing(float percent, int offset, int height, int extendHeight) {

    }

    @Override
    public void onReleased(RefreshLayout layout, int height, int extendHeight) {
        if (mProgressDrawable != null) {
            mProgressDrawable.start();
        } else {
            Drawable drawable = mProgressView.getDrawable();
            if (drawable instanceof Animatable) {
                ((Animatable) drawable).start();
            } else {
                mProgressView.animate().rotation(36000).setDuration(100000);
            }
        }
    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout layout, int height, int extendHeight) {

    }

    @Override
    public int onFinish(@NonNull RefreshLayout layout, boolean success) {
        if (mProgressDrawable != null) {
            mProgressDrawable.stop();
        } else {
            Drawable drawable = mProgressView.getDrawable();
            if (drawable instanceof Animatable) {
                ((Animatable) drawable).stop();
            } else {
                mProgressView.animate().rotation(0).setDuration(300);
            }
        }
        mProgressView.setVisibility(GONE);
        if (success) {
            mTitleText.setText(REFRESH_HEADER_FINISH);
            if (mLastTime != null) {
                setLastUpdateTime(new Date());
            }
        } else {
            mTitleText.setText(REFRESH_HEADER_FAILED);
        }
        return mFinishDuration;//延迟500毫秒之后再弹回
    }

    @Override@Deprecated
    public void setPrimaryColors(@ColorInt int ... colors) {
        if (colors.length > 0) {
            if (!(getBackground() instanceof BitmapDrawable) && mPrimaryColor == null) {
                setPrimaryColor(colors[0]);
                mPrimaryColor = null;
            }
            if (mAccentColor == null) {
                if (colors.length > 1) {
                    setAccentColor(colors[1]);
                } else {
                    setAccentColor(colors[0] == 0xffffffff ? 0xff666666 : 0xffffffff);
                }
                mAccentColor = null;
            }
        }
    }

    @NonNull
    public View getView() {
        return this;
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return mSpinnerStyle;
    }

    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
        switch (newState) {
            case None:
                mLastUpdateText.setVisibility(mEnableLastTime ? VISIBLE : GONE);
            case PullDownToRefresh:
                mTitleText.setText(REFRESH_HEADER_PULLDOWN);
                mArrowView.setVisibility(VISIBLE);
                mProgressView.setVisibility(GONE);
                mArrowView.animate().rotation(0);
                break;
            case Refreshing:
            case RefreshReleased:
                mTitleText.setText(REFRESH_HEADER_REFRESHING);
                mProgressView.setVisibility(VISIBLE);
                mArrowView.setVisibility(GONE);
                break;
            case ReleaseToRefresh:
                mTitleText.setText(REFRESH_HEADER_RELEASE);
                mArrowView.animate().rotation(180);
                break;
            case ReleaseToTwoLevel:
                mTitleText.setText(REFRESH_HEADER_SECOND_FLOOR);
                mArrowView.animate().rotation(0);
                break;
            case Loading:
                mArrowView.setVisibility(GONE);
                mProgressView.setVisibility(GONE);
                mLastUpdateText.setVisibility(mEnableLastTime ? INVISIBLE : GONE);
                mTitleText.setText(REFRESH_HEADER_LOADING);
                break;
        }
    }
    //</editor-fold>

    //<editor-fold desc="API">
    public ClassicHeader setProgressBitmap(Bitmap bitmap) {
        mProgressDrawable = null;
        mProgressView.setImageBitmap(bitmap);
        return this;
    }
    public ClassicHeader setProgressDrawable(Drawable drawable) {
        mProgressDrawable = null;
        mProgressView.setImageDrawable(drawable);
        return this;
    }
    public ClassicHeader setProgressResource(@DrawableRes int resId) {
        mProgressDrawable = null;
        mProgressView.setImageResource(resId);
        return this;
    }
    public ClassicHeader setArrowBitmap(Bitmap bitmap) {
        mArrowDrawable = null;
        mArrowView.setImageBitmap(bitmap);
        return this;
    }
    public ClassicHeader setArrowDrawable(Drawable drawable) {
        mArrowDrawable = null;
        mArrowView.setImageDrawable(drawable);
        return this;
    }
    public ClassicHeader setArrowResource(@DrawableRes int resId) {
        mArrowDrawable = null;
        mArrowView.setImageResource(resId);
        return this;
    }

    public ClassicHeader setLastUpdateTime(Date time) {
        mLastTime = time;
        mLastUpdateText.setText(mFormat.format(time));
        if (mShared != null && !isInEditMode()) {
            mShared.edit().putLong(KEY_LAST_UPDATE_TIME, time.getTime()).apply();
        }
        return this;
    }

    public ClassicHeader setLastUpdateText(CharSequence text) {
        mLastTime = null;
        mLastUpdateText.setText(text);
        return this;
    }

    public ClassicHeader setTimeFormat(DateFormat format) {
        mFormat = format;
        if (mLastTime != null) {
            mLastUpdateText.setText(mFormat.format(mLastTime));
        }
        return this;
    }

    public ClassicHeader setSpinnerStyle(SpinnerStyle style) {
        this.mSpinnerStyle = style;
        return this;
    }

    public ClassicHeader setPrimaryColor(@ColorInt int primaryColor) {
        mBackgroundColor = mPrimaryColor = primaryColor;
        if (mRefreshKernel != null) {
            mRefreshKernel.requestDrawBackgroundForHeader(mPrimaryColor);
        }
        return this;
    }

    public ClassicHeader setAccentColor(@ColorInt int accentColor) {
        mAccentColor = accentColor;
        if (mArrowDrawable != null) {
            mArrowDrawable.parserColors(accentColor);
        }
        if (mProgressDrawable != null) {
            mProgressDrawable.setColor(accentColor);
        }
        mTitleText.setTextColor(accentColor);
        mLastUpdateText.setTextColor(accentColor&0x00ffffff|0xcc000000);
        return this;
    }

    public ClassicHeader setPrimaryColorId(@ColorRes int colorId) {
        setPrimaryColor(ContextCompat.getColor(getContext(), colorId));
        return this;
    }

    public ClassicHeader setAccentColorId(@ColorRes int colorId) {
        setAccentColor(ContextCompat.getColor(getContext(), colorId));
        return this;
    }

    public ClassicHeader setFinishDuration(int delay) {
        mFinishDuration = delay;
        return this;
    }

    public ClassicHeader setEnableLastTime(boolean enable) {
        mEnableLastTime = enable;
        mLastUpdateText.setVisibility(enable ? VISIBLE : GONE);
        if (mRefreshKernel != null) {
            mRefreshKernel.requestRemeasureHeightForHeader();
        }
        return this;
    }

    public ClassicHeader setTextSizeTitle(float size) {
        mTitleText.setTextSize(size);
        if (mRefreshKernel != null) {
            mRefreshKernel.requestRemeasureHeightForHeader();
        }
        return this;
    }

    public ClassicHeader setTextSizeTitle(int unit, float size) {
        mTitleText.setTextSize(unit, size);
        if (mRefreshKernel != null) {
            mRefreshKernel.requestRemeasureHeightForHeader();
        }
        return this;
    }

    public ClassicHeader setTextSizeTime(float size) {
        mLastUpdateText.setTextSize(size);
        if (mRefreshKernel != null) {
            mRefreshKernel.requestRemeasureHeightForHeader();
        }
        return this;
    }

    public ClassicHeader setTextSizeTime(int unit, float size) {
        mLastUpdateText.setTextSize(unit, size);
        if (mRefreshKernel != null) {
            mRefreshKernel.requestRemeasureHeightForHeader();
        }
        return this;
    }

    public ClassicHeader setTextTimeMarginTop(float dp) {
        return setTextTimeMarginTopPx(DensityUtil.dp2px(dp));
    }

    public ClassicHeader setTextTimeMarginTopPx(int px) {
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams)mLastUpdateText.getLayoutParams();
        lp.topMargin = px;
        mLastUpdateText.setLayoutParams(lp);
        return this;
    }

    public ClassicHeader setDrawableMarginRight(float dp) {
        return setDrawableMarginRightPx(DensityUtil.dp2px(dp));
    }

    public ClassicHeader setDrawableMarginRightPx(int px) {
        ViewGroup.MarginLayoutParams lpArrow = (ViewGroup.MarginLayoutParams)mArrowView.getLayoutParams();
        ViewGroup.MarginLayoutParams lpProgress = (ViewGroup.MarginLayoutParams)mProgressView.getLayoutParams();
        lpArrow.rightMargin = lpProgress.rightMargin = px;
        mArrowView.setLayoutParams(lpArrow);
        mProgressView.setLayoutParams(lpProgress);
        return this;
    }

    public ClassicHeader setDrawableSize(float dp) {
        return setDrawableSizePx(DensityUtil.dp2px(dp));
    }

    public ClassicHeader setDrawableSizePx(int px) {
        ViewGroup.LayoutParams lpArrow = mArrowView.getLayoutParams();
        ViewGroup.LayoutParams lpProgress = mProgressView.getLayoutParams();
        lpArrow.width = lpProgress.width = px;
        lpArrow.height = lpProgress.height = px;
        mArrowView.setLayoutParams(lpArrow);
        mProgressView.setLayoutParams(lpProgress);
        return this;
    }

    public ClassicHeader setDrawableArrowSize(float dp) {
        return setDrawableArrowSizePx(DensityUtil.dp2px(dp));
    }

    public ClassicHeader setDrawableArrowSizePx(int px) {
        ViewGroup.LayoutParams lpArrow = mArrowView.getLayoutParams();
        lpArrow.width = px;
        lpArrow.height = px;
        mArrowView.setLayoutParams(lpArrow);
        return this;
    }

    public ClassicHeader setDrawableProgressSize(float dp) {
        return setDrawableProgressSizePx(DensityUtil.dp2px(dp));
    }

    public ClassicHeader setDrawableProgressSizePx(int px) {
        ViewGroup.LayoutParams lpProgress = mProgressView.getLayoutParams();
        lpProgress.width = px;
        lpProgress.height = px;
        mProgressView.setLayoutParams(lpProgress);
        return this;
    }

    public ImageView getArrowView() {
        return mArrowView;
    }

    public ImageView getProgressView() {
        return mProgressView;
    }

    public TextView getTitleText() {
        return mTitleText;
    }

    public TextView getLastUpdateText() {
        return mLastUpdateText;
    }

    //</editor-fold>
}
