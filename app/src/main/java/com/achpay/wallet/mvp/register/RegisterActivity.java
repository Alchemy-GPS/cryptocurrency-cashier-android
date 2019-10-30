package com.achpay.wallet.mvp.register;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.achpay.progresshub.KProgressHUD;
import com.achpay.wallet.R;
import com.achpay.wallet.model.RegisterRequest;
import com.achpay.wallet.model.RegisterResponse;
import com.achpay.wallet.model.params.ResponseParam;
import com.achpay.wallet.model.params.User;
import com.achpay.wallet.mvp.login.LoginActivity;
import com.achpay.wallet.network.BaseSubscriber;
import com.achpay.wallet.network.RetrofitUtil;
import com.achpay.wallet.network.UniteResponse;
import com.achpay.wallet.utils.AppUtil;
import com.achpay.wallet.utils.GsonUtil;
import com.achpay.wallet.utils.Log;
import com.achpay.wallet.utils.ToastUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.achpay.wallet.model.params.User.ACH_SYS_ID;

public class RegisterActivity extends Activity implements View.OnClickListener, View.OnFocusChangeListener {
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    private EditText mAccount;
    private EditText mShopName;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mVerifyPassword;
    private TextView mRegister;
    private int inputBottomHeight;
    private LinearLayout rootView;
    private KProgressHUD progressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        rootView = findViewById(R.id.register_root_view);

        keepLoginBtnNotOver(rootView);

        RelativeLayout mTitleBack = findViewById(R.id.left_titlebar_back);
        mAccount = findViewById(R.id.register_account_et);
        mShopName = findViewById(R.id.register_shop_name_et);
        mEmail = findViewById(R.id.register_email_et);
        mPassword = findViewById(R.id.register_password_et);
        mVerifyPassword = findViewById(R.id.register_verify_password_et);
        mRegister = findViewById(R.id.register_button);

        mTitleBack.setOnClickListener(this);
        mRegister.setOnClickListener(this);

        mAccount.setOnFocusChangeListener(this);
        mShopName.setOnFocusChangeListener(this);
        mEmail.setOnFocusChangeListener(this);
        mPassword.setOnFocusChangeListener(this);
        mVerifyPassword.setOnFocusChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.left_titlebar_back) {
            this.finish();
        } else if (v.getId() == R.id.register_button) {
            //注册
            register();
        }
    }

    private void register() {
        String account = mAccount.getText().toString().trim();
        String shopname = mShopName.getText().toString().trim();
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        String verifyPassword = mVerifyPassword.getText().toString().trim();

        if (TextUtils.isEmpty(account)) {
            ToastUtil.showInCenter(this, getString(R.string.register_account_empty_tip), Toast.LENGTH_SHORT);
            return;
        }
        if (TextUtils.isEmpty(shopname)) {
            ToastUtil.showInCenter(this, getString(R.string.register_shop_name_empty_tip), Toast.LENGTH_SHORT);
            return;
        }
        if (TextUtils.isEmpty(email)) {
            ToastUtil.showInCenter(this, getString(R.string.register_email_empty_tip), Toast.LENGTH_SHORT);
            return;
        }
        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(verifyPassword)) {
            ToastUtil.showInCenter(this, getString(R.string.register_password_empty_tip), Toast.LENGTH_SHORT);
            return;
        }

        if (!TextUtils.equals(password, verifyPassword)) {
            //两次密码不同
            ToastUtil.showInCenter(this, getString(R.string.register_password_not_verified), Toast.LENGTH_SHORT);
            return;
        }

        Pattern p = Pattern.compile(REGEX_EMAIL);
        Matcher m = p.matcher(email);
        if (!m.matches()) {
            //邮箱不匹配
            ToastUtil.showInCenter(this, getString(R.string.register_email_regex_not_match), Toast.LENGTH_SHORT);
            return;
        }

        registerOnline(account, shopname, email, password);
    }

    private void registerOnline(String account, String shopname, String email, String password) {
        showLoading();
        RegisterRequest request = new RegisterRequest();
        request.setLoginId(account);
        request.setMerchantName(shopname);
        request.setRegistMail(email);
        request.setLoginPassword(password);
        request.setAgainLoginPassword(password);
        request.setSysId(ACH_SYS_ID);
        request.setSign("sign");

        RetrofitUtil.getInstance().register(request, new BaseSubscriber<UniteResponse<RegisterResponse>>() {
            @Override
            public void onResponse(UniteResponse<RegisterResponse> response) {
                Log.e(GsonUtil.objectToJson(response));
                dismiss();
                if (response.getReturnCode().equals(ResponseParam.SUCCESS_CODE)) {
                    ToastUtil.showInCenter(RegisterActivity.this, getString(R.string.register_success), Toast.LENGTH_SHORT);
                    RegisterActivity.this.finish();
                } else {
                    ToastUtil.showInCenter(RegisterActivity.this, response.returnMsg, Toast.LENGTH_SHORT);
                } 
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                dismiss();
                ToastUtil.showInCenter(RegisterActivity.this, getString(R.string.register_error), Toast.LENGTH_SHORT);
            }
        });
    }
    
    public void showLoading() {
        progressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setSize(110, 110)
                .setLabelSize(15)
                .setCancellable(false)
                .setLabel(getString(R.string.register_registering))
                .setCancellable(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        progressHUD.dismiss();
                        RegisterActivity.this.finish();
                    }
                });
        progressHUD.show();
    }
    
    public void dismiss() {
        if (progressHUD != null && progressHUD.isShowing()) {
            progressHUD.dismiss();
        }
    }

    private void keepLoginBtnNotOver(final View root) {
        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                // 获取root在窗体的可视区域
                root.getWindowVisibleDisplayFrame(rect);
                // 获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
                int rootInvisibleHeight = root.getHeight() - rect.bottom + AppUtil.getStatusBarHeight(RegisterActivity.this);

                //减去mLoginButton的高度(除以2)是为了让布局再提高一块(高度等于mLoginButton的高度(除以2))
                int loginButtonMarginBottom = root.getHeight() - inputBottomHeight;

                if (loginButtonMarginBottom < rootInvisibleHeight) {
                    root.scrollTo(0, rootInvisibleHeight - loginButtonMarginBottom);
                } else {
                    root.scrollTo(0, 0);
                }
            }
        });
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v.getId() == R.id.register_account_et) {
            if (hasFocus) {
                inputBottomHeight = mAccount.getBottom();
            }
        } else if (v.getId() == R.id.register_shop_name_et) {
            if (hasFocus) {
                inputBottomHeight = mShopName.getBottom();
            }
        } else if (v.getId() == R.id.register_email_et) {
            if (hasFocus) {
                inputBottomHeight = mEmail.getBottom();
            }
        } else if (v.getId() == R.id.register_password_et) {
            if (hasFocus) {
                inputBottomHeight = mRegister.getBottom() + mRegister.getHeight() / 3;
                Rect rect = new Rect();
                // 获取root在窗体的可视区域
                rootView.getWindowVisibleDisplayFrame(rect);
                // 获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
                int rootInvisibleHeight = rootView.getHeight() - rect.bottom + AppUtil.getStatusBarHeight(RegisterActivity.this);

                int registerButtonMarginBottom = rootView.getHeight() - inputBottomHeight;

                if (registerButtonMarginBottom < rootInvisibleHeight) {
                    rootView.scrollTo(0, rootInvisibleHeight - registerButtonMarginBottom);
                } else {
                    rootView.scrollTo(0, 0);
                }
            }
        } else if (v.getId() == R.id.register_verify_password_et) {
            if (hasFocus) {
                inputBottomHeight = mRegister.getBottom() + mRegister.getHeight() / 3;

                Rect rect = new Rect();
                // 获取root在窗体的可视区域
                rootView.getWindowVisibleDisplayFrame(rect);
                // 获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
                int rootInvisibleHeight = rootView.getHeight() - rect.bottom + AppUtil.getStatusBarHeight(RegisterActivity.this);

                int registerButtonMarginBottom = rootView.getHeight() - inputBottomHeight;

                if (registerButtonMarginBottom < rootInvisibleHeight) {
                    rootView.scrollTo(0, rootInvisibleHeight - registerButtonMarginBottom);
                } else {
                    rootView.scrollTo(0, 0);
                }
            }
        }
    }
}
