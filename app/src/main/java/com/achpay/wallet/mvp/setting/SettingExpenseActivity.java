package com.achpay.wallet.mvp.setting;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.achpay.wallet.R;
import com.achpay.wallet.model.SettingExpenseInfo;
import com.crazysunj.cardslideview.CardViewPager;

import java.util.ArrayList;
import java.util.List;

public class SettingExpenseActivity extends FragmentActivity implements SettingExpenseHandler.OnCancelListener, SettingExpenseHandler.OnConfirmListener {
    private CardViewPager viewPager;
    public static SettingExpenseActivity mInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//设置竖屏模式
        overridePendingTransition(R.anim.expense_activity_open, R.anim.expense_activity_close);

        setContentView(R.layout.activity_setting_expense);

        mInstance = this;

        viewPager = findViewById(R.id.expense_setting_viewpager);

        List<SettingExpenseInfo> mList = new ArrayList<>();

        SettingExpenseInfo mDiscountInfo = new SettingExpenseInfo();
        mDiscountInfo.setCashExpense(true);
        mDiscountInfo.setPercentExpense(false);
        mDiscountInfo.setTitle(getString(R.string.dialog_discount_setting));
        mDiscountInfo.setCashExpenseDes(getString(R.string.discount_setting_cash));
        mDiscountInfo.setCashExpenseHint(getString(R.string.discount_setting_cashamount));
        mDiscountInfo.setPercentExpenseDes(getString(R.string.discount_setting_percent));
        mDiscountInfo.setPercentExpenseHint(getString(R.string.discount_setting_percentamount));


        SettingExpenseInfo mExtraInfo = new SettingExpenseInfo();
        mExtraInfo.setCashExpense(true);
        mExtraInfo.setPercentExpense(false);
        mExtraInfo.setTitle(getString(R.string.dialog_extra_setting));
        mExtraInfo.setCashExpenseDes(getString(R.string.extra_setting_cash));
        mExtraInfo.setCashExpenseHint(getString(R.string.extra_setting_cashamount));
        mExtraInfo.setPercentExpenseDes(getString(R.string.extra_setting_percent));
        mExtraInfo.setPercentExpenseHint(getString(R.string.extra_setting_percentamount));


        mList.add(mDiscountInfo);
        mList.add(mExtraInfo);

        SettingExpenseHandler mExpenseHandler = new SettingExpenseHandler(this);
        mExpenseHandler.setOnCancelListener(this);
        mExpenseHandler.setOnConfirmListener(this);

        viewPager.bind(getSupportFragmentManager(), mExpenseHandler, mList);

        viewPager.setCardTransformer(180, 0.38f);
        viewPager.setCardPadding(40);
        viewPager.setCardMargin(20);
        viewPager.notifyUI(CardViewPager.MODE_CARD);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.expense_activity_open, R.anim.expense_activity_close);
    }

    @Override
    public void onCancel() {
        this.finish();
    }

    @Override
    public void onConfirm() {
        this.finish();
    }
}
