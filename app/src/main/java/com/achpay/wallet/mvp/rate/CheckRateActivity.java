package com.achpay.wallet.mvp.rate;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.achpay.progresshub.KProgressHUD;
import com.achpay.wallet.R;
import com.achpay.wallet.database.dbmodel.CryptocurrencyManger;
import com.achpay.wallet.model.CurrencyToCoinRequest;
import com.achpay.wallet.model.CurrencyToCoinResponse;
import com.achpay.wallet.model.params.ResponseParam;
import com.achpay.wallet.model.params.TransParams;
import com.achpay.wallet.network.RetrofitUtil;
import com.achpay.wallet.utils.CommonUtil;
import com.achpay.wallet.utils.GsonUtil;
import com.achpay.wallet.utils.Log;
import com.achpay.wallet.utils.NumberUtils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class CheckRateActivity extends Activity implements View.OnClickListener {

    private String coinType;
    private String currencyType;
    private String currencyAmount;
    private String coinAmount;
    private double rate;
    private CurrencyToCoinRequest currencyToCoinModel;
    private TextView mOrderCurrencyAmount;
    private TextView mOrderCoinAmount;
    private TextView mCurrencytoCoinAmount;
    private TextView mCointoCurrencyAmount;
    private KProgressHUD progressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_rate);

        RelativeLayout mTitleBack = findViewById(R.id.left_titlebar_back);
        TextView mTitle = findViewById(R.id.left_titlebar_text);
        mTitle.setText(getString(R.string.rate_title));
        mTitleBack.setOnClickListener(this);

        TextView mOrderCurrencyType = findViewById(R.id.rate_order_currencytype);
        TextView mOneCurrencyType = findViewById(R.id.rate_currency_one_type);
        TextView mOneCointoCurrencyType = findViewById(R.id.rate_coin_to_currency_type);

        TextView mOrderCoinType = findViewById(R.id.rate_order_cointype);
        TextView mOneCurrencytoCoinType = findViewById(R.id.rate_currency_to_coin_type);
        TextView mOneCoinType = findViewById(R.id.rate_coin_one_type);

        TextView mCheckOnWeb = findViewById(R.id.rate_check_on_website);
        mCheckOnWeb.setOnClickListener(this);

        mOrderCurrencyAmount = findViewById(R.id.rate_order_currency_amount);
        mOrderCoinAmount = findViewById(R.id.rate_order_coin_amount);

        mCurrencytoCoinAmount = findViewById(R.id.rate_currency_to_coin);
        mCointoCurrencyAmount = findViewById(R.id.rate_coin_to_currency);

        coinType = getIntent().getStringExtra(TransParams.COIN_NAME);

        currencyType = getIntent().getStringExtra(TransParams.CURRENCY_NAME);

        currencyAmount = getIntent().getStringExtra(TransParams.CURRENCY_AMOUNT);

        coinAmount = getIntent().getStringExtra(TransParams.COIN_AMOUNT);

        mOrderCurrencyType.setText(currencyType);
        mOneCurrencyType.setText(currencyType);
        mOneCointoCurrencyType.setText(currencyType);

        mOrderCoinType.setText(coinType);
        mOneCoinType.setText(coinType);
        mOneCurrencytoCoinType.setText(coinType);

        checkRate();
    }

    private void setRate() {
        if (TextUtils.isEmpty(currencyAmount) && TextUtils.isEmpty(coinAmount)) {
            //二者皆为空值
            mOrderCurrencyAmount.setText("0.00");
            mOrderCoinAmount.setText("0.00");

        } else if (!TextUtils.isEmpty(currencyAmount) && TextUtils.isEmpty(coinAmount)) {
            //携带金额为法币
            mOrderCurrencyAmount.setText(currencyAmount);
            coinAmount = NumberUtils.formatDouble8(Double.parseDouble(currencyAmount) * rate);
            mOrderCoinAmount.setText(coinAmount);
        } else if (!TextUtils.isEmpty(coinAmount) && TextUtils.isEmpty(currencyAmount)) {
            //携带金额为虚拟币
            mOrderCoinAmount.setText(coinAmount);

            currencyAmount = NumberUtils.formatDouble2(Double.parseDouble(coinAmount) / rate);

            mOrderCurrencyAmount.setText(currencyAmount);
        }

        mCurrencytoCoinAmount.setText(NumberUtils.formatDouble8(1 * rate));
        mCointoCurrencyAmount.setText(NumberUtils.formatDouble2(1 / rate));
    }

    private void checkRate() {
        showProgress();

    }

    public void showProgress() {
        if (progressHUD == null) {
            progressHUD = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setSize(125, 125)
                    .setLabel(getString(R.string.progress_wait))
                    .setCancellable(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            progressHUD.dismiss();
                            CheckRateActivity.this.finish();
                        }
                    });
        }

        if (!progressHUD.isShowing()) {
            progressHUD.show();
        }
    }

    public void dismissProgress() {
        if (progressHUD != null) {
            progressHUD.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.left_titlebar_back) {
            CheckRateActivity.this.finish();
        } else if (v.getId() == R.id.rate_check_on_website) {
            Intent intent= new Intent();
            intent.setAction("android.intent.action.VIEW");

            String name = CryptocurrencyManger.getInstance().queryCryptocurrencyByName(coinType).getCryptoName();

            String coinmarketcapUrl = "https://coinmarketcap.com/currencies/" + name + "/";
            Uri content_url = Uri.parse(coinmarketcapUrl);

            intent.setData(content_url);
            startActivity(intent);
        }
    }
}
