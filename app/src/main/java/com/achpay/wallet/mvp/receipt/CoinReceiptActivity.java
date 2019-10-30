package com.achpay.wallet.mvp.receipt;

import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.achpay.progresshub.KProgressHUD;
import com.achpay.wallet.R;
import com.achpay.wallet.base.view.BaseActivity;
import com.achpay.wallet.database.dbmodel.Cryptocurrency;
import com.achpay.wallet.database.dbmodel.CryptocurrencyManger;
import com.achpay.wallet.model.OrderStatusMessage;
import com.achpay.wallet.model.SettingExpenseResult;
import com.achpay.wallet.model.event.OrderReadEvent;
import com.achpay.wallet.model.params.ResponseParam;
import com.achpay.wallet.model.params.TransParams;
import com.achpay.wallet.model.params.User;
import com.achpay.wallet.mvp.order.OrderDetailActivity;
import com.achpay.wallet.mvp.setting.SettingExpenseActivity;
import com.achpay.wallet.utils.AppUtil;
import com.achpay.wallet.utils.CommonUtil;
import com.achpay.wallet.utils.EventBusUtil;
import com.achpay.wallet.utils.Log;
import com.achpay.wallet.utils.QRCode;
import com.achpay.wallet.utils.SharedPreferenceUtil;
import com.achpay.wallet.widget.MyRadioGroup;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.web3j.crypto.WalletUtils;

import java.io.IOException;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class CoinReceiptActivity extends BaseActivity<CoinReceiptPresenter> implements CoinReceiptView, View.OnClickListener, CompoundButton.OnCheckedChangeListener, MyRadioGroup.OnCheckedChangeListener, NfcAdapter.ReaderCallback {
    private String address;
    private String TYPE;
    private String currency_name;
    private String currency_amount;
    private String coin_name;
    private String coin_result;

    private ImageView mQRCode;
    private TextView mCoinType;
    private TextView mCoinAmount;
    private TextView mCurrencyType;
    private TextView mOrderFee;
    private TextView mCurrencyAmount;
    private TextView mDiscountOrExtraAmount;
    private TextView mSettingAmountType;
    private TextView mAddress;
    private CheckBox mCheckBox;

    private KProgressHUD progressHUD;
    private RelativeLayout mDiscountSetting;
    private String mOrderId;
    private String settingAmountType;
    private TextView mOrderCurrFee;
    private TextView mOrderCurrFeeType;
    private TextView mOrderFeePercentage;
    private TextView mOrderFeeUnit;
    private RadioButton mCommonAddress;
    private RadioButton mSegwitAddress;
    private TextView mOrderStatus;
    private MyRadioGroup mAddressGroup;
    private View mAddressGap;
    private RelativeLayout mOrderStatusLayout;
    private Cryptocurrency cryptocurrency;
    private String coin_id;
    private String currency_id;
    private NfcAdapter mNfcAdapter;
    private String userAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_receipt);
        AppUtil.keepScreenOn(this);

        AppUtil.closeActivityOnSchedule(this, 300L);

        mQRCode = findViewById(R.id.receipt_imageview_qrcode);

        RelativeLayout mTitleBack = findViewById(R.id.left_titlebar_back);
        TextView mTitle = findViewById(R.id.left_titlebar_text);

        mTitle.setText(getString(R.string.receipt_discount_title));
        mTitleBack.setOnClickListener(this);

        mCoinType = findViewById(R.id.receipt_coin_type);
        mCoinAmount = findViewById(R.id.receipt_coin_amount);
        mCurrencyType = findViewById(R.id.receipt_currency_type);
        mCurrencyAmount = findViewById(R.id.receipt_currency_amount);

        mAddressGap = findViewById(R.id.receipt_address_gap);
        mAddressGroup = findViewById(R.id.receipt_address_radiogroup);
        mCommonAddress = findViewById(R.id.receipt_address_common);
        mSegwitAddress = findViewById(R.id.receipt_address_segwit);

        mOrderFee = findViewById(R.id.receipt_recommand_fee);
        mOrderFeeUnit = findViewById(R.id.receipt_recommand_fee_unit);
        mOrderFeePercentage = findViewById(R.id.receipt_recommand_fee_pencentage);
        mOrderCurrFee = findViewById(R.id.receipt_recommand_fee_currency_amount);
        mOrderCurrFeeType = findViewById(R.id.receipt_recommand_fee_currency_type);

        mAddress = findViewById(R.id.receipt_address_tv);
        mCheckBox = findViewById(R.id.receipt_address_withamount);

        mOrderStatusLayout = findViewById(R.id.receipt_order_status_layout);
        mOrderStatus = findViewById(R.id.receipt_order_status);

        mSettingAmountType = findViewById(R.id.receipt_setting_amount_type);
        mDiscountOrExtraAmount = findViewById(R.id.receipt_setting_amount);

        mDiscountSetting = findViewById(R.id.receipt_discount_setting);

        RelativeLayout mCheckBoxLayout = findViewById(R.id.receipt_address_withamount_rl);
        RelativeLayout mAddressWithAmoutLayout = findViewById(R.id.receipt_address_withamount_layout);
        RelativeLayout mRecommamdFeeAllLayout = findViewById(R.id.receipt_recommand_fee_layout_all);

        mAddressGroup.setOnCheckedChangeListener(this);
        mCheckBox.setOnCheckedChangeListener(this);
        mDiscountSetting.setOnClickListener(this);
        mCheckBoxLayout.setOnClickListener(this);
        mOrderStatusLayout.setOnClickListener(this);

        EventBusUtil.register(this);

        TYPE = getIntent().getStringExtra(TransParams.TYPE);
        currency_name = getIntent().getStringExtra(TransParams.CURRENCY_NAME);
        currency_id = getIntent().getStringExtra(TransParams.CURRENCY_ID);
        coin_name = getIntent().getStringExtra(TransParams.COIN_NAME);
        coin_id = getIntent().getStringExtra(TransParams.COIN_ID);

        mCurrencyType.setText(currency_name);
        mCoinType.setText(coin_name);

        //此处以后可以优化
        if (CommonUtil.supportSegwit(coin_id)) {
            mAddressGroup.setVisibility(View.VISIBLE);
            mAddressGap.setVisibility(View.VISIBLE);
        }

        if (CommonUtil.supportLightningNetwork(coin_id) || CommonUtil.isVite(coin_id)) {
            mCheckBox.setChecked(false);
            mAddressWithAmoutLayout.setVisibility(View.GONE);
            mRecommamdFeeAllLayout.setVisibility(View.GONE);
        }

        if (CommonUtil.supportRaidenNetwork(coin_id)) {
            initNFC();
        }

        showProgress();

        cryptocurrency = CryptocurrencyManger.getInstance().queryCryptocurrencyById(coin_id);

        if (!TextUtils.isEmpty(TYPE) && TYPE.equals(TransParams.CURRENCY)) {//传过来的类型为法币
            currency_amount = getIntent().getStringExtra(TransParams.AMOUNT);

            settingAmountType = SharedPreferenceUtil.getPref(this).getString(User.SETTING_AMOUNT_TYPE, User.SETTING_AMOUNT_DISCOUNT);

            if (User.SETTING_AMOUNT_DISCOUNT.equals(settingAmountType)) {
                mSettingAmountType.setText(getString(R.string.receipt_discount_amount));
            } else if (User.SETTING_AMOUNT_EXTRA.equals(settingAmountType)) {
                mSettingAmountType.setText(getString(R.string.receipt_extra_amount));
            }

            MvpPre.calculateAmount(this);

            this.mOrderId = CommonUtil.createOrderId();
            MvpPre.createOrder(this, currency_id, coin_id, currency_amount, mOrderId, true);
        }
    }

    @Override
    protected CoinReceiptPresenter bindPresenter() {
        return new CoinReceiptPresenter(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.receipt_discount_setting) {
            Intent intent = new Intent(this, SettingExpenseActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.left_titlebar_back) {
            this.finish();
        } else if (v.getId() == R.id.receipt_order_status_layout) {
            Intent intent = new Intent(this, OrderDetailActivity.class);
            intent.putExtra(User.ORDER_ID, mOrderId);
            startActivity(intent);
        } else if (v.getId() == R.id.receipt_address_withamount_rl) {
            if (mCheckBox.isChecked()) {
                mCheckBox.setChecked(false);
            } else {
                mCheckBox.setChecked(true);
            }
        }
    }

    @Override
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
                            CoinReceiptActivity.this.finish();
                        }
                    });
        }

        if (!progressHUD.isShowing()) {
            progressHUD.show();
        }
    }

    @Override
    public void dismissProgress() {
        if (progressHUD != null && progressHUD.isShowing()) {
            progressHUD.dismiss();
        }
    }

    @Override
    public void showQRCode(String address) {
        if (TextUtils.isEmpty(address)) {
            mQRCode.setImageResource(R.mipmap.image_default_background);
        } else {
            mQRCode.setImageBitmap(QRCode.createQRCode(address, 600));
        }
    }

    @Override
    public void showQRCodeWithAmount(String address, String amount) {
        if (cryptocurrency == null) {
            cryptocurrency = CryptocurrencyManger.getInstance().queryCryptocurrencyById(coin_id);
        }

        String amountAddress;
        if (coin_id.equals("5")) {//bch不需要头信息
            amountAddress = address + "?" + cryptocurrency.getCryptoProtocol() + "=" + amount;
        } else {
            amountAddress = cryptocurrency.getCryptoCoin() + ":" + address + "?" + cryptocurrency.getCryptoProtocol() + "=" + amount;
        }

//        暂时不加订单信息,以免有些钱包不识别
//        if (!TextUtils.isEmpty(mOrderId)) {
//            AddressOrderInfo info = new AddressOrderInfo(CommonUtil.getMerchantId(this), mOrderId);
//            amountAddress = amountAddress.concat("&message=" + AES.encrypt(info.toString()));
//        }

        mQRCode.setImageBitmap(QRCode.createQRCode(amountAddress, 600));
    }

    @Override
    public void setCurrencyAmount(String amount) {
        mCurrencyAmount.setText(amount);
    }

    @Override
    public void setCoinAmount(String amount) {
        this.coin_result = amount;
        mCoinAmount.setText(amount);
    }

    @Override
    public void setDiscountOrExtraAmount(String amount) {
        mDiscountOrExtraAmount.setText(amount);
    }

    @Override
    public void setAmount(String coinAmount, String currencyAmount) {
        this.coin_result = coinAmount;

        mCoinAmount.setText(coinAmount);

        mCurrencyAmount.setText(currencyAmount);
    }

    /**
     * 设置地址(将地址转成二维码)
     *
     * @param address
     */
    @Override
    public void setAddress(String address) {
        this.address = address;

        mAddress.setText(address);

        if (mCheckBox.isChecked()) {
            showQRCodeWithAmount(address, coin_result);
        } else {
            showQRCode(address);
        }
    }

    /**
     * 设置手续费
     *
     * @param fee
     * @param unit
     * @param currFee
     * @param percentage
     */
    @Override
    public void setFee(String fee, String unit, String currFee, String percentage) {
        mOrderFee.setText(fee);
        mOrderFeeUnit.setText(unit);
        mOrderCurrFee.setText(currFee);
        mOrderCurrFeeType.setText(currency_name);
        mOrderFeePercentage.setText(percentage);
    }

    public void initNFC() {
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter == null) {
            //3.如果获取的mNfcAdapter=null，则说明该手机不支持nfc功能
            Log.i("NFC is not supported");
            return;
        } else {
            //4.如果手机有nfc功能，进一步判断nfc是否打开
            Log.i("NFC is enabled");
            if (!mNfcAdapter.isEnabled()) {
                //5.假如手机的nfc功能没有被打开。则跳到打开nfc功能的界面
                Intent setNfc = new Intent(Settings.ACTION_NFC_SETTINGS);
                startActivity(setNfc);
            }
        }
    }

    @Override
    public void onTagDiscovered(Tag tag) {
        IsoDep isoDep = IsoDep.get(tag);
        try {
            isoDep.connect();

            byte[] response = isoDep.transceive(CommonUtil.hexStringToByteArray(
                    "00A4040007A0000002471001"));

            userAddress = "0x" + CommonUtil.ByteArrayToHexString(response);

            boolean isValidAddress = WalletUtils.isValidAddress(userAddress);

            Log.i("Wallet Address == " + userAddress);

            if (isValidAddress) {
                Log.i("Wallet Address isValid");

                isoDep.close();
                if (mNfcAdapter != null) {
                    mNfcAdapter.disableReaderMode(this);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MvpPre.raidenPay(userAddress.toLowerCase());
                    }
                });
            } else {
                Log.i("Wallet Address isNotValid");
                isoDep.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mNfcAdapter != null) {
            mNfcAdapter.enableReaderMode(this, this,
                    NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK,
                    null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mNfcAdapter != null) {
            mNfcAdapter.disableReaderMode(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppUtil.closeKeepScreenOn(this);
        EventBusUtil.unregister(this);
    }

    /**
     * 推送收到的消息
     *
     * @param message
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOrderStatusMessageReceived(OrderStatusMessage message) {
        //收到最新的一笔订单推送
        if (this.mOrderId.equals(message.getOrderId())) {
            if (message.getResult().equals(ResponseParam.SUCCESS)
                    || message.getResult().equals(ResponseParam.MSUCCESS)
                    || message.getResult().equals(ResponseParam.CONFIRMING)
                    || message.getResult().equals(ResponseParam.LSUCCESS)) {

                //雷电或闪电币种收到确认中不做任何操作
                if (CryptocurrencyManger.getInstance().queryCryptocurrencyById(coin_id).getCoinType().equals(TransParams.LIGHTNING)
                        && message.getResult().equals(ResponseParam.CONFIRMING)) {
                    return;
                }

                if (AppUtil.isForeground(this, SettingExpenseActivity.class.getName())) {
                    SettingExpenseActivity.mInstance.finish();
                }

                mOrderStatusLayout.setVisibility(View.VISIBLE);
                mOrderStatus.setTextColor(getResources().getColor(R.color.input_confirm));
                mOrderStatus.setText(message.getResultMsg());

                AppUtil.vibrator(this, 350);

                EventBusUtil.post(new OrderReadEvent(true, mOrderId));

                Intent intent = new Intent(getSelfActivity(), OrderDetailActivity.class);
                intent.putExtra(User.ORDER_ID, mOrderId);
                startActivity(intent);

                this.finish();

            } else {
                Toast.makeText(this, CommonUtil.transOrderStatus(this, message.getResult()), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 优惠和附加费修改后收到消息
     *
     * @param result
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onExpenseSet(SettingExpenseResult result) {
        showQRCode("");
        if (result.isDiscount()) {//优惠
            mSettingAmountType.setText(getString(R.string.receipt_discount_amount));
            if (result.isCash()) {
                MvpPre.changeDiscountAmount(User.DISCOUNT_CASH, result.getAmount());
            } else {
                MvpPre.changeDiscountAmount(User.DISCOUNT_PERCENT, result.getAmount());
            }
        } else {//附加费
            mSettingAmountType.setText(getString(R.string.receipt_extra_amount));
            if (result.isCash()) {
                MvpPre.changeExtraAmount(User.EXTRA_CASH, result.getAmount());
            } else {
                MvpPre.changeExtraAmount(User.EXTRA_PERCENT, result.getAmount());
            }
        }

        mOrderId = CommonUtil.createOrderId();
        MvpPre.createOrder(this, currency_id, coin_id, currency_amount, mOrderId, true);
    }

    /**
     * 是否带金额的切换监听
     *
     * @param buttonView
     * @param isChecked
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.receipt_address_withamount) {
            if (!TextUtils.isEmpty(address) && !TextUtils.isEmpty(coin_result)) {
                if (isChecked) {
                    showQRCodeWithAmount(address, coin_result);
                } else {
                    showQRCode(address);
                }
            }
        }
    }

    /**
     * 正常地址和隔离见证地址切换的监听
     *
     * @param group     the group in which the checked radio button has changed
     * @param checkedId the unique identifier of the newly checked radio button
     */
    @Override
    public void onCheckedChanged(MyRadioGroup group, int checkedId) {
        this.mOrderId = CommonUtil.createOrderId();
        if (checkedId == R.id.receipt_address_common) {
            mCommonAddress.setTextColor(getResources().getColor(R.color.input_confirm));
            mSegwitAddress.setTextColor(getResources().getColor(R.color.input_unconfirm));
            showProgress();

            mOrderId = CommonUtil.createOrderId();
            MvpPre.createOrder(this, currency_id, coin_id, currency_amount, mOrderId, false);
        } else if (checkedId == R.id.receipt_address_segwit) {
            mCommonAddress.setTextColor(getResources().getColor(R.color.input_unconfirm));
            mSegwitAddress.setTextColor(getResources().getColor(R.color.input_confirm));
            showProgress();
            mOrderId = CommonUtil.createOrderId();
            MvpPre.createOrder(this, currency_id, coin_id, currency_amount, mOrderId, true);
        }
    }
}
