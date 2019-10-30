package com.achpay.wallet.mvp.lightningnetwork;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.achpay.progresshub.KProgressHUD;
import com.achpay.wallet.Constants;
import com.achpay.wallet.R;
import com.achpay.wallet.model.LightningAddressResponse;
import com.achpay.wallet.model.LightningUriResponse;
import com.achpay.wallet.mvp.receipt.CoinReceiptActivity;
import com.achpay.wallet.network.ApiService;
import com.achpay.wallet.network.BaseSubscriber;
import com.achpay.wallet.network.RetrofitUtil;
import com.achpay.wallet.utils.CommonUtil;
import com.achpay.wallet.utils.Log;
import com.achpay.wallet.utils.QRCode;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class LightningNetworkActivity extends Activity implements View.OnClickListener {

    private KProgressHUD progressHUD;
    private ImageView mLightningUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lightning_network);
        RelativeLayout mTitleBack = findViewById(R.id.left_titlebar_back);
        TextView mTitle = findViewById(R.id.left_titlebar_text);
        mTitle.setText(getString(R.string.lightning_network_title));

        mLightningUri = findViewById(R.id.lightning_network_uri_iv);

        mTitleBack.setOnClickListener(this);

        String eclairUri = "03864ef025fde8fb587d989186ce6a4a186895ee44a926bfc370e2c366597a3f8f@node.acinq.co:9735";

//        mLightningUri.setImageBitmap(QRCode.createQRCode(eclairUri, 600));
        getUri();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.left_titlebar_back) {
            this.finish();
        }
    }

    public void showProgress() {
        if (progressHUD == null) {
            progressHUD = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setSize(100, 100)
                    .setCancellable(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            progressHUD.dismiss();
                            LightningNetworkActivity.this.finish();
                        }
                    });
        }

        if (!progressHUD.isShowing()) {
            progressHUD.show();
        }
    }

    public void dismissProgress() {
        progressHUD.dismiss();
    }

    private void getUri() {
        showProgress();

        RetrofitUtil.getInstance().getLightningUri(Constants.LIGHTNING_ADDRESS_URL, new BaseSubscriber<LightningUriResponse>() {
            @Override
            public void onResponse(LightningUriResponse lightningUriResponse) {
                String uri = lightningUriResponse.getUris();
                if (!TextUtils.isEmpty(uri)) {
                    mLightningUri.setImageBitmap(QRCode.createQRCode(uri, 600));
                }
                dismissProgress();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                dismissProgress();
            }
        });
    }
}
