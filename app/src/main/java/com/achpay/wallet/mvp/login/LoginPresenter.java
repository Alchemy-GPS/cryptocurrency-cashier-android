package com.achpay.wallet.mvp.login;

import android.content.Context;
import android.content.Intent;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Toast;

import com.achpay.wallet.R;
import com.achpay.wallet.base.presenter.BasePresenter;
import com.achpay.wallet.database.dbmodel.Cryptocurrency;
import com.achpay.wallet.database.dbmodel.CryptocurrencyManger;
import com.achpay.wallet.model.CoinTypeRequest;
import com.achpay.wallet.model.CoinTypeResponse;
import com.achpay.wallet.model.LoginQfpayResponse;
import com.achpay.wallet.model.LoginRequest;
import com.achpay.wallet.model.LoginResponse;
import com.achpay.wallet.model.WSClientMessage;
import com.achpay.wallet.model.params.ResponseParam;
import com.achpay.wallet.model.params.TransParams;
import com.achpay.wallet.model.params.User;
import com.achpay.wallet.network.BaseSubscriber;
import com.achpay.wallet.network.RetrofitUtil;
import com.achpay.wallet.network.UniteResponse;
import com.achpay.wallet.service.MessageService;
import com.achpay.wallet.service.ProtectService;
import com.achpay.wallet.utils.AES;
import com.achpay.wallet.utils.AppUtil;
import com.achpay.wallet.utils.CommonUtil;
import com.achpay.wallet.utils.EventBusUtil;
import com.achpay.wallet.utils.GsonUtil;
import com.achpay.wallet.utils.Log;
import com.achpay.wallet.utils.NativeMethods;
import com.achpay.wallet.utils.SharedPreferenceUtil;

import java.util.List;

import static com.achpay.wallet.model.params.User.ACH_SYS_ID;
import static com.achpay.wallet.model.params.User.QFPAY_SYS_ID;

public class LoginPresenter extends BasePresenter<LoginView> {
    public LoginPresenter(LoginView view) {
        super(view);
    }

    public void QfpayLogin(final Context mContext, String accountNumber, String password) {
        RetrofitUtil.getInstance().qfpayLogin(accountNumber, password, new BaseSubscriber<LoginQfpayResponse>() {
            @Override
            public void onResponse(LoginQfpayResponse response) {
                Log.i(GsonUtil.objectToJson(response));
                if (MvpRef != null) {
                    if (response.getRespcd().equals(ResponseParam.SUCCESS_CODE) && response.getUserData() != null) {
                        //钱方登录成功
                        SharedPreferenceUtil.getPref(mContext).putStringValue(User.SYS_ID, QFPAY_SYS_ID);
                        SharedPreferenceUtil.getPref(mContext).putStringValue(User.QFPAY_SESSION_ID, AES.encrypt(response.getUserData().getSessionid()));

                        LoginRequest request = new LoginRequest(
                                String.valueOf(response.getUserData().getUserid()),
                                response.getUserData().getShopname(),
                                response.getUserData().getSessionid(),
                                QFPAY_SYS_ID);
                        AchLogin(mContext, request);
                    } else {
                        //登录失败
                        loginError();
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                loginError();
            }
        });
    }

    public void AchLogin(final Context mContext, LoginRequest request) {

        RetrofitUtil.getInstance().userLogin(request, new BaseSubscriber<UniteResponse<LoginResponse>>() {
            @Override
            public void onResponse(UniteResponse<LoginResponse> response) {

                Log.i(GsonUtil.objectToJson(response));

                //    2002 无操作权限
                //    3012 登录信息不符
                if (response.getReturnCode().equals(ResponseParam.SUCCESS_CODE)) {
                    LoginResponse loginResponse = response.getData();

                    String currencyId = String.valueOf(loginResponse.getSettleCurrencyId());

                    CommonUtil.setCurrencyUnitById(mContext, currencyId);

                    SharedPreferenceUtil
                            .getPref(mContext)
                            .putStringValue(User.MERCHANT_ID, AES.encrypt(loginResponse.getMerchantId()));

                    SharedPreferenceUtil
                            .getPref(mContext)
                            .putStringValue(User.MERCHANT_NAME, loginResponse.getMerchantName());

                    SharedPreferenceUtil
                            .getPref(mContext)
                            .putStringValue(User.SESSION_ID, AES.encrypt(loginResponse.getSessionId()));

                    SharedPreferenceUtil
                            .getPref(mContext)
                            .putStringValue(User.QFPAY_MERCHANT_ID, AES.encrypt(loginResponse.getQianfId()));

                    SharedPreferenceUtil
                            .getPref(mContext)
                            .putStringValue(User.SETTING_CURRENCY_ID, String.valueOf(loginResponse.getSettleCurrencyId()));

                    SharedPreferenceUtil
                            .getPref(mContext)
                            .putStringValue(User.SETTING_CURRENCY, loginResponse.getSettltCurrency());

                    SharedPreferenceUtil
                            .getPref(mContext)
                            .putStringValue(User.SETTING_CRYPTOCURRENCY_ID, String.valueOf(loginResponse.getSettleCryptocurrencyId()));

                    SharedPreferenceUtil
                            .getPref(mContext)
                            .putStringValue(User.SETTING_CRYPTOCURRENCY, loginResponse.getSettleCryptocurrency());

                    if (!AppUtil.isServiceRunning(mContext, "com.achpay.wallet.service.MessageService")) {
                        Intent intent = new Intent(mContext, MessageService.class);
                        mContext.startService(intent);
                    }

                    if (!AppUtil.isServiceRunning(mContext, "com.achpay.wallet.service.ProtectService")) {
                        Intent intent = new Intent(mContext, ProtectService.class);
                        mContext.startService(intent);
                    }

                    WSClientMessage message = new WSClientMessage();
                    message.setMessage(TransParams.CHECK_WEBSOCKET);
                    EventBusUtil.post(message);

                    List<Cryptocurrency> mCryptocurrencyList = CryptocurrencyManger.getInstance().queryAllCryptocurrency();

                    if (mCryptocurrencyList == null || mCryptocurrencyList.size() == 0) {
                        initCryptocurrency();
                    } else {
                        if (MvpRef != null) {
                            MvpRef.get().loginSuccess();
                        }
                    }
                } else {
                    //登录失败
                    loginError();
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                loginError();
            }
        });
    }

    public void AchpayLogin(final Context mContext, String accountNumber, String password) {
        String encryptPassword = CommonUtil.Base64(CommonUtil.MD5(NativeMethods.getPWDSaltStart() + password + NativeMethods.getPWDSaltEnd()));

        LoginRequest request = new LoginRequest();
        request.setLoginId(accountNumber);
        request.setLoginPassword(encryptPassword);
        request.setSysId(ACH_SYS_ID);

        SharedPreferenceUtil.getPref(mContext).putStringValue(User.SYS_ID, ACH_SYS_ID);

        RetrofitUtil.getInstance().userLogin(request, new BaseSubscriber<UniteResponse<LoginResponse>>() {
            @Override
            public void onResponse(UniteResponse<LoginResponse> response) {

                Log.i(GsonUtil.objectToJson(response));

                //    2002 无操作权限
                //    3012 登录信息不符
                if (response.getReturnCode().equals(ResponseParam.SUCCESS_CODE)) {
                    LoginResponse loginResponse = response.getData();

                    String currencyId = String.valueOf(loginResponse.getSettleCurrencyId());

                    CommonUtil.setCurrencyUnitById(mContext, currencyId);

                    SharedPreferenceUtil
                            .getPref(mContext)
                            .putStringValue(User.MERCHANT_ID, AES.encrypt(loginResponse.getMerchantId()));

                    SharedPreferenceUtil
                            .getPref(mContext)
                            .putStringValue(User.MERCHANT_NAME, loginResponse.getMerchantName());

                    SharedPreferenceUtil
                            .getPref(mContext)
                            .putStringValue(User.SESSION_ID, AES.encrypt(loginResponse.getSessionId()));

                    SharedPreferenceUtil
                            .getPref(mContext)
                            .putStringValue(User.SETTING_CURRENCY_ID, String.valueOf(loginResponse.getSettleCurrencyId()));

                    SharedPreferenceUtil
                            .getPref(mContext)
                            .putStringValue(User.SETTING_CRYPTOCURRENCY_ID, String.valueOf(loginResponse.getSettleCryptocurrencyId()));

                    SharedPreferenceUtil
                            .getPref(mContext)
                            .putStringValue(User.SETTING_CURRENCY, loginResponse.getSettltCurrency());

                    SharedPreferenceUtil
                            .getPref(mContext)
                            .putStringValue(User.SETTING_CRYPTOCURRENCY, loginResponse.getSettleCryptocurrency());

                    if (!AppUtil.isServiceRunning(mContext, "com.achpay.wallet.service.MessageService")) {
                        Intent intent = new Intent(mContext, MessageService.class);
                        mContext.startService(intent);
                    }

                    if (!AppUtil.isServiceRunning(mContext, "com.achpay.wallet.service.ProtectService")) {
                        Intent intent = new Intent(mContext, ProtectService.class);
                        mContext.startService(intent);
                    }

                    WSClientMessage message = new WSClientMessage();
                    message.setMessage(TransParams.CHECK_WEBSOCKET);
                    EventBusUtil.post(message);

                    List<Cryptocurrency> mCryptocurrencyList = CryptocurrencyManger.getInstance().queryAllCryptocurrency();

                    if (mCryptocurrencyList == null || mCryptocurrencyList.size() == 0) {
                        initCryptocurrency();
                    } else {
                        if (MvpRef != null) {
                            MvpRef.get().loginSuccess();
                        }
                    }
                } else {
                    //登录失败
                    loginError();
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                loginError();
            }
        });
    }

    private void initCryptocurrency() {
        CoinTypeRequest request = new CoinTypeRequest(1);

        RetrofitUtil.getInstance().getCoinType(request, new BaseSubscriber<UniteResponse<CoinTypeResponse>>() {
            @Override
            public void onResponse(UniteResponse<CoinTypeResponse> response) {
                Log.i(GsonUtil.objectToJson(response));
                if (response.getReturnCode().equals(ResponseParam.SUCCESS_CODE)) {
                    List<Cryptocurrency> mList = response.getData().getSupportCryptosList();
                    if (mList != null && mList.size() != 0) {
                        CryptocurrencyManger.getInstance().insertCryptocurrencys(mList);
                        if (MvpRef != null) {
                            MvpRef.get().loginSuccess();
                        }
                    }
                } else {
                    loginError();
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                loginError();
            }
        });
    }

    private void loginError() {
        if (MvpRef != null) {
            MvpRef.get().loginFail();
        }
    }
}
