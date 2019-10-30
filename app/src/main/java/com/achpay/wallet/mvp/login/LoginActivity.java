package com.achpay.wallet.mvp.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.achpay.progresshub.KProgressHUD;
import com.achpay.wallet.R;
import com.achpay.wallet.base.view.BaseActivity;
import com.achpay.wallet.model.params.User;
import com.achpay.wallet.mvp.main.MainActivity;
import com.achpay.wallet.mvp.register.RegisterActivity;
import com.achpay.wallet.mvp.setting.SettingLanguageActivity;
import com.achpay.wallet.utils.CommonUtil;
import com.achpay.wallet.utils.SharedPreferenceUtil;
import com.achpay.wallet.widget.Rotate3dAnimation;

import static com.achpay.wallet.model.params.User.ACHPAY_LOGIN;

public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginView, View.OnClickListener {
    private EditText mAccountNumber;
    private EditText mPassword;
    private TextView mLoginButton;
    private TextView mGoToRegister;
    private KProgressHUD progressHUD;
    private TextView mLoginEntry;
    private int mLoginSourceClickTimes;
    private long mLoginSourceFirstClickTime;

    private int centerX;
    private int centerY;
    private int duration = 500;
    private Rotate3dAnimation firstPartAnimation;
    private Rotate3dAnimation secondPartAnimation;
    private boolean isACHLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LinearLayout rootView = findViewById(R.id.login_root_view);
        RelativeLayout contentView = findViewById(R.id.login_root_content);

        mLoginButton = findViewById(R.id.login_button_tv);
        mGoToRegister = findViewById(R.id.login_go_to_register);
        mAccountNumber = findViewById(R.id.login_account_number_et);
        mPassword = findViewById(R.id.login_merchant_password_et);
        mLoginEntry = findViewById(R.id.qfpay_login_source);
        RelativeLayout mSettingLanguage = findViewById(R.id.login_language_setting);

        keepLoginBtnNotOver(rootView, contentView);

        mLoginButton.setOnClickListener(this);
        mGoToRegister.setOnClickListener(this);
        mLoginEntry.setOnClickListener(this);
        mSettingLanguage.setOnClickListener(this);

        isACHLogin = SharedPreferenceUtil.getPref(this).getBoolean(ACHPAY_LOGIN, true);
        if (isACHLogin) {
            mLoginEntry.setText(getString(R.string.login_alchemy_account));
            mGoToRegister.setVisibility(View.VISIBLE);
        } else {
            mGoToRegister.setVisibility(View.GONE);
        }
    }

    private void keepLoginBtnNotOver(final View root, final View subView) {
        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                // 获取root在窗体的可视区域
                root.getWindowVisibleDisplayFrame(rect);
                // 获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
                int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;

                //减去mLoginButton的高度(除以2)是为了让布局再提高一块(高度等于mLoginButton的高度(除以2))
                int loginButtonMarginBottom = root.getHeight() - mLoginButton.getBottom() - mLoginButton.getHeight() / 2;

                if (loginButtonMarginBottom < rootInvisibleHeight) {
                    root.scrollTo(0, rootInvisibleHeight - loginButtonMarginBottom);
                } else {
                    root.scrollTo(0, 0);
                }

                // 若不可视区域高度大于200，则键盘显示,其实相当于键盘的高度
                /*if (rootInvisibleHeight > 200) {
                    // 显示键盘时
                    int srollHeight = rootInvisibleHeight - (root.getHeight() - subView.getHeight()) - AppUtil.getNavigationBarHeight(root.getContext());
                    if (srollHeight > 0) {
                        //当键盘高度覆盖按钮时
                        root.scrollTo(0, srollHeight);
                    }
                } else {
                    // 隐藏键盘时
                    root.scrollTo(0, 0);
                }*/
            }
        });
    }


    @Override
    protected LoginPresenter bindPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login_button_tv) {
            login();
        } else if (v.getId() == R.id.login_language_setting) {
            //设置语言
            Intent intent = new Intent(this, SettingLanguageActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.login_go_to_register) {
            //前往注册
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.qfpay_login_source) {
            if (firstPartAnimation != null) {
                if (firstPartAnimation.hasStarted() && !firstPartAnimation.hasEnded()) {
                    return;
                }
            }

            if (secondPartAnimation != null) {
                if (secondPartAnimation.hasStarted() && !secondPartAnimation.hasEnded()) {
                    return;
                }
            }

            mLoginSourceClickTimes++;
            if (mLoginSourceClickTimes == 1) {
                mLoginSourceFirstClickTime = System.currentTimeMillis();
            }
            if (System.currentTimeMillis() - mLoginSourceFirstClickTime < 1500) {
                if (mLoginSourceClickTimes == 4) {

                    centerX = mLoginEntry.getWidth() / 2;
                    centerY = mLoginEntry.getHeight() / 2;

                    if (firstPartAnimation == null) {
                        initOpenAnim();
                    }

                    mLoginEntry.startAnimation(firstPartAnimation);
                }
            } else {
                mLoginSourceClickTimes = 0;
            }
        }
    }

    private void initOpenAnim() {
        //从0到90度，顺时针旋转视图，此时reverse参数为true，达到90度时动画结束时视图变得不可见，
        firstPartAnimation = new Rotate3dAnimation(0, 90, centerX, centerY, centerX, true);
        firstPartAnimation.setDuration(duration);
        firstPartAnimation.setFillAfter(true);
        firstPartAnimation.setInterpolator(new AccelerateInterpolator());
        firstPartAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (isACHLogin) {
                    SharedPreferenceUtil.getPref(LoginActivity.this).putBooleanValue(ACHPAY_LOGIN, false);
                    isACHLogin = false;
                    mLoginEntry.setText(getString(R.string.login_qfpay_account));
                    mGoToRegister.setVisibility(View.GONE);
                } else {//QFPAY登录
                    SharedPreferenceUtil.getPref(LoginActivity.this).putBooleanValue(ACHPAY_LOGIN, true);
                    isACHLogin = true;
                    mLoginEntry.setText(getString(R.string.login_alchemy_account));
                    mGoToRegister.setVisibility(View.VISIBLE);
                }
                startSecondPartAnimation();
            }
        });
    }

    private void startSecondPartAnimation() {
        //从270到360度，顺时针旋转视图，此时reverse参数为false，达到360度动画结束时视图变得可见
        if (secondPartAnimation == null) {
            secondPartAnimation = new Rotate3dAnimation(270, 360, centerX, centerY, centerX, false);
        }
        secondPartAnimation.setDuration(duration);
        secondPartAnimation.setFillAfter(true);
        secondPartAnimation.setInterpolator(new DecelerateInterpolator());
        secondPartAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mLoginSourceClickTimes = 0;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mLoginEntry.startAnimation(secondPartAnimation);
    }

    private void login() {
        String accountNumber = mAccountNumber.getText().toString().trim();
        String password = mPassword.getText().toString().trim();

        if (TextUtils.isEmpty(accountNumber)) {
            Toast.makeText(this, getString(R.string.login_empty_phone), Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, getString(R.string.login_empty_password), Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading();

        if (isACHLogin) {
            MvpPre.AchpayLogin(this, accountNumber, password);
        } else {
            MvpPre.QfpayLogin(this, accountNumber, password);
        }
    }

    @Override
    public void showLoading() {
        progressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setSize(125, 125)
                .setLabel(getString(R.string.progress_logining))
                .setCancellable(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        progressHUD.dismiss();
                        LoginActivity.this.finish();
                    }
                });
        progressHUD.show();
    }

    @Override
    public void dismiss() {
        if (progressHUD != null && progressHUD.isShowing()) {
            progressHUD.dismiss();
        }
    }

    @Override
    public void loginSuccess() {
        dismiss();
        Intent intent = new Intent(this, MainActivity.class);

        intent.putExtra(User.MERCHANT_ID, CommonUtil.getMerchantId(this));
        intent.putExtra(User.MERCHANT_NAME, CommonUtil.getMerchantName(this));

        startActivity(intent);

        this.finish();
    }

    @Override
    public void loginFail() {
        dismiss();
        Toast.makeText(this, getString(R.string.login_login_fail), Toast.LENGTH_SHORT).show();
    }
}
