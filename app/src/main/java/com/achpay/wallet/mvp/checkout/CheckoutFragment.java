package com.achpay.wallet.mvp.checkout;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.achpay.wallet.R;
import com.achpay.wallet.base.view.BaseFragment;
import com.achpay.wallet.database.dbmodel.RecentOrder;
import com.achpay.wallet.database.dbmodel.RecentOrderDaoManager;
import com.achpay.wallet.model.OrderStatusMessage;
import com.achpay.wallet.model.event.OrderReadEvent;
import com.achpay.wallet.model.event.SettingCurrencyEvent;
import com.achpay.wallet.model.params.ResponseParam;
import com.achpay.wallet.model.params.TransParams;
import com.achpay.wallet.model.params.User;
import com.achpay.wallet.mvp.cointype.CoinTypeActivity;
import com.achpay.wallet.mvp.order.OrderDetailActivity;
import com.achpay.wallet.mvp.receipt.CoinReceiptActivity;
import com.achpay.wallet.utils.CommonUtil;
import com.achpay.wallet.utils.EventBusUtil;
import com.achpay.wallet.utils.GsonUtil;
import com.achpay.wallet.utils.Log;
import com.achpay.wallet.utils.NumberUtils;
import com.achpay.wallet.utils.SharedPreferenceUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CheckoutFragment extends BaseFragment<CheckoutPresenter> implements CheckoutView, View.OnClickListener, TextWatcher, View.OnTouchListener {

    private final String TAG = "CheckoutFragment";

    private OrderStatusMessage mOrderStatusMessage;
    private TextView incomeTime;
    private TextView incomeAmount;
    private TextView incomeStatus;
    private ImageView incomePoint;
    private RelativeLayout mFirstItem;
    private TextView incomeType;
    private LinearLayout incomeAmountLayout;
    private TextView currencyUnit;
    private EditText amount;
    private TextView checkoutConfirm;
    private ImageView buttonDelete;
    private GestureDetector mGestureDetector;
    private Disposable mDisposable;
    private RecentOrder mRecentOrder;

    public static CheckoutFragment newInstance(String title) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TITLE, title);
        CheckoutFragment fragment = new CheckoutFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected CheckoutPresenter bindPresenter() {
        return new CheckoutPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_main_checkout, container, false);
        initView(mainView);
        return mainView;
    }

    private void initView(View mainView) {
        EventBusUtil.register(this);

        initFirstItem(mainView);

        initAmountLayout(mainView);

        initKeyBoard(mainView);
    }

    private void initFirstItem(View mainView) {
        mFirstItem = mainView.findViewById(R.id.checkout_income_item);

        incomeTime = mainView.findViewById(R.id.fragment_checkout_incometime);

        incomeAmount = mainView.findViewById(R.id.fragment_checkout_incomeamount);

        incomeType = mainView.findViewById(R.id.fragment_checkout_incometype);

        incomeAmountLayout = mainView.findViewById(R.id.fragment_checkout_income_amount_ll);

        incomeStatus = mainView.findViewById(R.id.fragment_checkout_incomestatus);

        incomePoint = mainView.findViewById(R.id.fragment_checkout_redpoint);

        mFirstItem.setOnClickListener(this);

        //检查上一笔订单信息,如果有则显示
        String orderDetail = CommonUtil.getLastOrderDetail(getSelfActivity());

        Log.i("LAST_ORDER_DETAIL == " + orderDetail);

        if (TextUtils.isEmpty(orderDetail)) {//首次进入App
            incomeTime.setText(getString(R.string.checkout_no_order));

            incomePoint.setVisibility(View.INVISIBLE);

            incomeAmountLayout.setVisibility(View.INVISIBLE);
        } else {
            mOrderStatusMessage = (OrderStatusMessage) GsonUtil.jsonToBean(orderDetail, OrderStatusMessage.class);

            if (mOrderStatusMessage != null) {

                incomePoint.setVisibility(getOrderIsRead() ? View.INVISIBLE : View.VISIBLE);

                incomeTime.setText(mOrderStatusMessage.getReceiveTime());

                incomeAmount.setText(mOrderStatusMessage.getReceiveQuantity() + " ");

                incomeType.setText(mOrderStatusMessage.getCryptocurrency());

                incomeStatus.setText(CommonUtil.transOrderStatus(getSelfActivity(), mOrderStatusMessage.getResult()));
            }
        }
    }

    private void initAmountLayout(View mainView) {
        currencyUnit = mainView.findViewById(R.id.checkout_receipt_amount_unit);
        amount = mainView.findViewById(R.id.checkout_receipt_input);
        checkoutConfirm = mainView.findViewById(R.id.checkout_receipt_confirm);

        currencyUnit.setText(CommonUtil.getCurrencyUnit(getSelfActivity()));
        checkoutConfirm.setOnClickListener(this);
        checkoutConfirm.setBackgroundColor(getResources().getColor(R.color.input_unconfirm));
        checkoutConfirm.setTag(false);
        amount.addTextChangedListener(this);
    }

    private void initKeyBoard(View mainView) {
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
        buttonDelete = mainView.findViewById(R.id.input_index_delete);

        button0.setOnClickListener(this);
        button00.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        buttonPoint.setOnClickListener(this);
        buttonDelete.setOnClickListener(this);
        buttonDelete.setOnTouchListener(this);

        mGestureDetector = new GestureDetector(getSelfActivity(),new GestureDetector.SimpleOnGestureListener(){
            @Override
            public void onLongPress(MotionEvent e) {
               Observable.interval(100, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Long>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                mDisposable = d;
                            }

                            @Override
                            public void onNext(Long aLong) {
                                delete();
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }

            @Override
            public boolean onDown(MotionEvent e) {
                delete();
                return super.onDown(e);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.checkout_income_item:
                String orderDetail = CommonUtil.getLastOrderDetail(getSelfActivity());
                if (!TextUtils.isEmpty(orderDetail)) {
                    Intent intent = new Intent(getSelfActivity(), OrderDetailActivity.class);
                    intent.putExtra(User.ORDER_ID, mOrderStatusMessage.getOrderId());
                    startActivity(intent);
                    setOrderIsRead(mOrderStatusMessage.getOrderId());

                } else {
                    Toast.makeText(getSelfActivity(), getResources().getString(R.string.checkout_no_detail), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.input_index_0:
                setAmount("0");
                break;
            case R.id.input_index_00:
                setAmount("00");
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
            case R.id.input_index_point:
                setAmount(".");
                break;
            case R.id.checkout_receipt_confirm:
                if ((boolean) checkoutConfirm.getTag()) {
                    Intent intent = new Intent(getSelfActivity(), CoinTypeActivity.class);
                    intent.putExtra(TransParams.AMOUNT, amount.getText().toString().trim());
                    startActivity(intent);

                    Observable.timer(200, TimeUnit.MILLISECONDS)
                            .subscribeOn(Schedulers.io())
                            .unsubscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<Long>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onNext(Long aLong) {
                                    amount.setText("");
                                }

                                @Override
                                public void onError(Throwable e) {
                                    e.printStackTrace();
                                    amount.setText("");
                                }

                                @Override
                                public void onComplete() {

                                }
                            });
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusUtil.unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onOrderMessageReceived(OrderStatusMessage message) {
        this.mOrderStatusMessage = message;
        //收到最新的一笔订单推送

        incomePoint.setVisibility(getOrderIsRead() ? View.INVISIBLE : View.VISIBLE);

        incomeAmountLayout.setVisibility(View.VISIBLE);

        incomeTime.setText(mOrderStatusMessage.getReceiveTime());

        incomeAmount.setText(mOrderStatusMessage.getReceiveQuantity() + " ");

        incomeType.setText(mOrderStatusMessage.getCryptocurrency());

        incomeStatus.setText(CommonUtil.transOrderStatus(getSelfActivity(), mOrderStatusMessage.getResult()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSettingCurrencyEvent(SettingCurrencyEvent event) {
        if (event != null && event.getMessage().equals(ResponseParam.SUCCESS)) {
            currencyUnit.setText(CommonUtil.getCurrencyUnit(getSelfActivity()));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOrderReadEvent(OrderReadEvent event) {
        if (event != null && event.isRead()) {
            setOrderIsRead(event.getOrderId());
        }
    }

    private void setOrderIsRead(String orderId) {
        mRecentOrder = RecentOrderDaoManager.getInstance().queryRecentOrderById(orderId);
        mRecentOrder.setIsRead(true);
        incomePoint.setVisibility(View.INVISIBLE);
        RecentOrderDaoManager.getInstance().insertRecentOrder(mRecentOrder);
    }

    private boolean getOrderIsRead() {
        mRecentOrder = RecentOrderDaoManager.getInstance().queryRecentOrderById(mOrderStatusMessage.getOrderId());
        if (mRecentOrder == null) {
            Log.i("mRecentOrder == null");
            return false;
        }
        return mRecentOrder.getIsRead();
    }

    private void delete() {
        String number = getAmountBefore();
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

                if (NumberUtils.isOneDecimal(before) && number.equals("00")) {//输入两个0
                    number = "0";
                }
            }
        }
        amount.setText(before.concat(number));
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (TextUtils.isEmpty(s)) {
            amount.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);//没有值显示为hint
        } else {
            amount.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);//有值显示为数字
        }

        if (TextUtils.isEmpty(s)
                || "0.".equals(s.toString())
                || "0.0".equals(s.toString())
                || "0.00".equals(s.toString())
                || "0".equals(s.toString())) {
            checkoutConfirm.setBackgroundColor(getResources().getColor(R.color.input_unconfirm));
            checkoutConfirm.setTag(false);
        } else {
            checkoutConfirm.setBackgroundColor(getResources().getColor(R.color.input_confirm));
            checkoutConfirm.setTag(true);
        }
        amount.setSelection(s.length());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.input_index_delete) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if(mDisposable!=null&&!mDisposable.isDisposed()){
                    mDisposable.dispose();
                }
                return mGestureDetector.onTouchEvent(event);
            } else {
                return mGestureDetector.onTouchEvent(event);
            }
        }
        return false;
    }
}
