package com.achpay.wallet.mvp.setting;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.achpay.progresshub.KProgressHUD;
import com.achpay.wallet.R;
import com.achpay.wallet.model.ZworkScore;
import com.achpay.wallet.utils.Log;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GrantPointsActivity extends Activity implements View.OnClickListener {

    private TextView mConfirm;
    private EditText mMemberId;
    private EditText mPointValue;
    private KProgressHUD progressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grant_points);

        RelativeLayout mTitleBack = findViewById(R.id.left_titlebar_back);
        TextView mTitle = findViewById(R.id.left_titlebar_text);
        mTitle.setText(getString(R.string.points_grant_title));
        mConfirm = findViewById(R.id.grant_points_confirm);
        mMemberId = findViewById(R.id.grant_points_member_id);
        mPointValue = findViewById(R.id.grant_points_value);

        mTitleBack.setOnClickListener(this);
        mConfirm.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.left_titlebar_back) {
            this.finish();
        } else if (v.getId() == R.id.grant_points_confirm) {
            String memberId = mMemberId.getText().toString().trim();

            String value = mPointValue.getText().toString().trim();

            if (TextUtils.isEmpty(memberId)) {
                Toast.makeText(GrantPointsActivity.this, "会员ID不能为空", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(value)) {
                Toast.makeText(GrantPointsActivity.this, "积分值不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            grantPoints(memberId, value);
        }
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
                            GrantPointsActivity.this.finish();
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

    private void grantPoints(final String memberId, final String value) {
        final Web3j web3j = Web3jFactory.build(new HttpService("http://47.104.90.114:8545"));

        final String CONTRACT_ADDRESS = "0xede80d8f73fb90e37780af2b1a1d072afba9c70a";

        final String privateKey = "1BC480FA6FAA4BFE91C72E05BEA43DD275267E772F9CF364093D131C109F43DE";

        final Credentials credentials = Credentials.create(privateKey);

        final BigInteger amount = new BigInteger(value).multiply(BigInteger.valueOf(100000000L));

        showProgress();

        web3j.ethGasPrice()
                .observable()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<EthGasPrice>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        dismissProgress();
                        Toast.makeText(GrantPointsActivity.this, "积分发放失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(EthGasPrice ethGasPrice) {
                        Log.i("gasPrice === " + ethGasPrice.getGasPrice().toString());

                        BigInteger gasLimit = BigInteger.valueOf(210000);
                        BigInteger gasPrice = ethGasPrice.getGasPrice();

                        ZworkScore zworkScore = ZworkScore.load(CONTRACT_ADDRESS, web3j, credentials, gasPrice, gasLimit);

                        zworkScore.grant(memberId, amount)
                                .observable()
                                .subscribeOn(Schedulers.io())
                                .unsubscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<TransactionReceipt>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        e.printStackTrace();
                                        Log.i("grantError");
                                        dismissProgress();
                                    }

                                    @Override
                                    public void onNext(TransactionReceipt transactionReceipt) {
                                        Toast.makeText(GrantPointsActivity.this, getString(R.string.points_grant_success), Toast.LENGTH_SHORT).show();
                                        dismissProgress();
                                    }
                                });
                    }
                });
    }
}
