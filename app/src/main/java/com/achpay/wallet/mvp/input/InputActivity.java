package com.achpay.wallet.mvp.input;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.achpay.wallet.R;
import com.achpay.wallet.model.CurrencyToCoinRequest;
import com.achpay.wallet.model.CurrencyToCoinResponse;
import com.achpay.wallet.model.event.RateCheckEvent;
import com.achpay.wallet.model.event.RateEvent;
import com.achpay.wallet.model.params.ResponseParam;
import com.achpay.wallet.model.params.TransParams;
import com.achpay.wallet.network.RetrofitUtil;
import com.achpay.wallet.utils.CommonUtil;
import com.achpay.wallet.utils.EventBusUtil;
import com.achpay.wallet.utils.GsonUtil;
import com.achpay.wallet.utils.Log;
import com.achpay.wallet.utils.SharedPreferenceUtil;
import com.achpay.wallet.view.TestEnvironmentTips;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 1,显示收款地址,点击设置金额时,弹出设置金额的Dialog,动态转换金额
 * 2,设置好以后,将法币和虚拟货币的交易金额和相应的手续费显示在界面上
 * 3,通知后台轮询查看该商户的交易状态
 */
public class InputActivity extends FragmentActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private String[] mTitles = new String[2];
    private String coinType;
    private String currencyType;
    private TextView mCurrencyType;
    private View mCurrencyTab;
    private TextView mCoinType;
    private View mCoinTab;
    private RelativeLayout mBack;
    private TextView mTitle;
    private ViewPager mViewPager;
    private CurrencyToCoinRequest currencyToCoinModel;
    private double rate;
    private RelativeLayout mRightLayout;
    private TextView mRightText;
    private LinearLayout mCurrencyLayout;
    private LinearLayout mCoinLayout;
    private String currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        coinType = getIntent().getStringExtra(TransParams.COIN_NAME);

        currencyType = CommonUtil.getCurrencyType(this);

        mTitles[0] = currencyType;

        mTitles[1] = coinType;

        mViewPager = findViewById(R.id.checkout_sliding_viewpager);

        mCurrencyType = findViewById(R.id.input_currencyname);
        mCurrencyTab = findViewById(R.id.input_currencytab);

        mCoinType = findViewById(R.id.input_coinname);
        mCoinTab = findViewById(R.id.input_cointab);

        mBack = findViewById(R.id.left_titlebar_back);

        mRightLayout = findViewById(R.id.titlebar_right_layout);

        mRightLayout.setVisibility(View.VISIBLE);

        mTitle = findViewById(R.id.left_titlebar_text);

        mRightText = findViewById(R.id.titlebar_right_text);

        mTitle.setText(getString(R.string.input_title));

        mRightText.setText(R.string.input_title_rate);

        mCurrencyLayout = findViewById(R.id.input_currency_layout);

        mCoinLayout = findViewById(R.id.input_coin_layout);

        mRightText.setTextColor(getResources().getColor(R.color.receipt_address));

        mCurrencyType.setText(currencyType);

        mCoinType.setText(coinType);

        InputAdapter adapter = new InputAdapter(getSupportFragmentManager(), mTitles);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(this);
        mRightLayout.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mCurrencyLayout.setOnClickListener(this);
        mCoinLayout.setOnClickListener(this);

        currentPage = "AmountInputFragment";
//        currencyToCoinModel = new CurrencyToCoinRequest();

        // TODO: 2018/7/9 只有演示的时候显示
        showTestTips();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.left_titlebar_back) {
            InputActivity.this.finish();
        } else if (v.getId() == R.id.titlebar_right_layout) {
            EventBusUtil.post(new RateCheckEvent(currentPage));
        } else if (v.getId() == R.id.input_currency_layout) {
            mViewPager.setCurrentItem(0);
        } else if (v.getId() == R.id.input_coin_layout) {
            mViewPager.setCurrentItem(1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
     /*   currencyToCoinModel.setAmount("1");

        RetrofitUtil.getInstance().transCurrencyToCoin(currencyToCoinModel, new Observer<CurrencyToCoinResponse>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(CurrencyToCoinResponse currencyToCoinResponse) {
                Log.i(GsonUtil.objectToJson(currencyToCoinResponse));

                if (currencyToCoinResponse != null && currencyToCoinResponse.getReturnMsg().equals(ResponseParam.SUCCESS)) {

                    String coinAmount = currencyToCoinResponse.getQuantity();

                    rate = Double.parseDouble(coinAmount);

                    EventBusUtil.post(new RateEvent(rate));

                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    */
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            currentPage = "AmountInputFragment";

            mCurrencyType.setTextColor(getResources().getColor(R.color.input_confirm));
            mCurrencyTab.setBackgroundColor(getResources().getColor(R.color.input_confirm));

            mCoinType.setTextColor(getResources().getColor(R.color.input_unconfirm));
            mCoinTab.setBackgroundColor(getResources().getColor(R.color.input_unconfirm));

        } else if (position == 1) {
            currentPage = "CoinInputFragment";

            mCoinType.setTextColor(getResources().getColor(R.color.input_confirm));
            mCoinTab.setBackgroundColor(getResources().getColor(R.color.input_confirm));

            mCurrencyType.setTextColor(getResources().getColor(R.color.input_unconfirm));
            mCurrencyTab.setBackgroundColor(getResources().getColor(R.color.input_unconfirm));

        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void showTestTips() {
        boolean showTestNetTips = SharedPreferenceUtil.getPref(this).getBoolean("SHOW_TESTNET_TIPS", true);
        if (showTestNetTips) {
            TestEnvironmentTips tips = new TestEnvironmentTips(this);
            tips.show();
        }
    }
}
