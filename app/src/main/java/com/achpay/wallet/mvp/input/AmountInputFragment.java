package com.achpay.wallet.mvp.input;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.achpay.wallet.R;
import com.achpay.wallet.base.presenter.IPresenter;
import com.achpay.wallet.base.view.BaseFragment;
import com.achpay.wallet.model.event.RateCheckEvent;
import com.achpay.wallet.model.event.RateEvent;
import com.achpay.wallet.model.params.TransParams;
import com.achpay.wallet.mvp.rate.CheckRateActivity;
import com.achpay.wallet.mvp.receipt.CoinReceiptActivity;
import com.achpay.wallet.utils.EventBusUtil;
import com.achpay.wallet.utils.NumberUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class AmountInputFragment extends BaseFragment implements TextWatcher, View.OnClickListener {
    private EditText amount;
    private TextView hint;
    private TextView buttonConfirm;

    private String currencyName;
    private String coinName;
    private TextView coinAmount;

    private double rate;

    public static AmountInputFragment newInstance(String currency, String coin) {
        Bundle bundle = new Bundle();
        bundle.putString(TransParams.CURRENCY_NAME, currency);
        bundle.putString(TransParams.COIN_NAME, coin);
        AmountInputFragment fragment = new AmountInputFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected IPresenter bindPresenter() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            currencyName = arguments.getString(TransParams.CURRENCY_NAME);
            coinName = arguments.getString(TransParams.COIN_NAME);
        }
        EventBusUtil.register(this);

        View mainView = inflater.inflate(R.layout.fragment_input_amount, container, false);
        initView(mainView);
        return mainView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusUtil.unregister(this);
    }

    private void initView(View mainView) {
        TextView button1 = mainView.findViewById(R.id.input_index_1);
        TextView button2 = mainView.findViewById(R.id.input_index_2);
        TextView button3 = mainView.findViewById(R.id.input_index_3);
        TextView button4 = mainView.findViewById(R.id.input_index_4);
        TextView button5 = mainView.findViewById(R.id.input_index_5);
        TextView button6 = mainView.findViewById(R.id.input_index_6);
        TextView button7 = mainView.findViewById(R.id.input_index_7);
        TextView button8 = mainView.findViewById(R.id.input_index_8);
        TextView button9 = mainView.findViewById(R.id.input_index_9);
        TextView button0 = mainView.findViewById(R.id.input_index_0);
        TextView button00 = mainView.findViewById(R.id.input_index_00);

        TextView buttonPoint = mainView.findViewById(R.id.input_index_point);
        ImageView buttonDelete = mainView.findViewById(R.id.input_index_delete);
        TextView currencyNameTv = mainView.findViewById(R.id.amount_input_currencyname);
        TextView coinNameTv = mainView.findViewById(R.id.amount_input_coinname);
        buttonConfirm = mainView.findViewById(R.id.input_index_confirm);
        amount = mainView.findViewById(R.id.amount_input_edittext);
        hint = mainView.findViewById(R.id.amount_input_hint);
        coinAmount = mainView.findViewById(R.id.amount_input_coinamount);

        button0.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        button00.setOnClickListener(this);
        buttonPoint.setOnClickListener(this);
        buttonDelete.setOnClickListener(this);
        buttonConfirm.setOnClickListener(this);

        amount.addTextChangedListener(this);

        //disableShowInput(amount);

        currencyNameTv.setText(currencyName);

        coinNameTv.setText(coinName);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.i("--------", s.toString());

        if (TextUtils.isEmpty(s)) {
            hint.setVisibility(View.VISIBLE);
        } else {
            hint.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(s)
                || "0.".equals(s.toString())
                || "0.0".equals(s.toString())
                || "0.00".equals(s.toString())
                || "0".equals(s.toString())) {

            coinAmount.setText(getString(R.string.amount_input_coinzero));
            buttonConfirm.setBackgroundColor(getResources().getColor(R.color.input_unconfirm));
            buttonConfirm.setTag(false);
        } else {
            buttonConfirm.setBackgroundColor(getResources().getColor(R.color.input_confirm));
            buttonConfirm.setTag(true);
            coinAmount.setText(NumberUtils.formatDouble8(Double.parseDouble(s.toString()) * rate));
        }
        amount.setSelection(s.length());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private void delete(String number) {
        if (!TextUtils.isEmpty(number)) {
            number = number.substring(0, number.length() - 1);
            amount.setText(number);
        }
    }

    private String getAmountBefore() {
        return amount.getText().toString().trim();
    }

    private void setAmount(String number) {
        String before = getAmountBefore();

        if (TextUtils.isEmpty(before)) {//输入框为空
            if (number.equals(".")) {
                amount.setText("0.");
                return;
            }
            if (number.equals("00")) {
                amount.setText("0");
                return;
            }
        } else {//输入框不为空
            if (before.equals("0")) {//输入框中为0的情况
                if (number.equals(".")) {
                    amount.setText("0.");
                } else if (number.equals("00") || number.equals("0")) {
                    amount.setText("0");
                } else {
                    amount.setText(number);
                }
                return;
            }
            if (NumberUtils.isDecimal(before)) {
                if (number.equals(".")) {//已经是小数,不能再输入点
                    return;
                }
                if (NumberUtils.isTwoDecimal(before)) {//两位小数
                    return;
                }
            }
        }
        amount.setText(before.concat(number));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.input_index_0:
                setAmount("0");
                break;
            case R.id.input_index_1:
                setAmount("1");
                break;
            case R.id.input_index_2:
                setAmount("2");
                break;
            case R.id.input_index_3:
                setAmount("3");
                break;
            case R.id.input_index_4:
                setAmount("4");
                break;
            case R.id.input_index_5:
                setAmount("5");
                break;
            case R.id.input_index_6:
                setAmount("6");
                break;
            case R.id.input_index_7:
                setAmount("7");
                break;
            case R.id.input_index_8:
                setAmount("8");
                break;
            case R.id.input_index_9:
                setAmount("9");
                break;
            case R.id.input_index_00:
                setAmount("00");
                break;
            case R.id.input_index_point:
                setAmount(".");
                break;
            case R.id.input_index_delete:
                String number = getAmountBefore();
                delete(number);
                break;
            case R.id.input_index_confirm:
                if ((boolean) buttonConfirm.getTag()) {
                    Intent intent = new Intent();
                    intent.setClass(getSelfActivity(), CoinReceiptActivity.class);

                    intent.putExtra(TransParams.TYPE, TransParams.CURRENCY);//类型为法币
                    intent.putExtra(TransParams.CURRENCY_NAME, currencyName);//法币名字
                    intent.putExtra(TransParams.AMOUNT, amount.getText().toString().trim());//法币金额
                    intent.putExtra(TransParams.COIN_NAME, coinName);//需要转换的虚拟币名字
                    startActivity(intent);
                    getSelfActivity().finish();
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRateReceived(RateEvent message) {
        if (message != null) {
            this.rate = message.getRate();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRateCheckReceived(RateCheckEvent event) {
        if (event != null && event.getPageName().equals("AmountInputFragment")) {
            Intent intent = new Intent(getSelfActivity(), CheckRateActivity.class);
            intent.putExtra(TransParams.COIN_NAME, coinName);
            intent.putExtra(TransParams.CURRENCY_NAME, currencyName);
            intent.putExtra(TransParams.CURRENCY_AMOUNT, amount.getText().toString().trim());
            startActivity(intent);
        }
    }
}
