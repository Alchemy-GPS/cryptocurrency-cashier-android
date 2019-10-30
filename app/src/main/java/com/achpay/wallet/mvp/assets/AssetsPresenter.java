package com.achpay.wallet.mvp.assets;

import android.content.Context;

import com.achpay.wallet.R;
import com.achpay.wallet.base.presenter.BasePresenter;
import com.achpay.wallet.model.UserInfoRequest;
import com.achpay.wallet.model.UserInfoResponse;
import com.achpay.wallet.model.params.ResponseParam;
import com.achpay.wallet.network.BaseSubscriber;
import com.achpay.wallet.network.RetrofitUtil;
import com.achpay.wallet.network.UniteResponse;
import com.achpay.wallet.utils.CommonUtil;
import com.achpay.wallet.utils.GsonUtil;
import com.achpay.wallet.utils.Log;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class AssetsPresenter extends BasePresenter<AssetsView> {
    public AssetsPresenter(AssetsView view) {
        super(view);
    }

    public void getUserInfo(Context mContext) {

        UserInfoRequest request = new UserInfoRequest();
        request.setSign("ACHPAY");

        RetrofitUtil.getInstance().getUserInfo(request, new BaseSubscriber<UniteResponse<UserInfoResponse>>() {

            @Override
            public void onResponse(UniteResponse<UserInfoResponse> response) {
                Log.i("获取账户信息 == " + GsonUtil.objectToJson(response));
                if (MvpRef != null) {
                    UserInfoResponse userInfoResponse = response.getData();
                    List<UserInfoResponse.AccountInfos> accountInfos = userInfoResponse.getAccountInfos();
                    if (accountInfos != null && accountInfos.size() != 0) {
                        MvpRef.get().setDatas(accountInfos);
                    }

                    MvpRef.get().dismissProgress();
                    MvpRef.get().stopRefresh();
                }
            }

            @Override
            public void onError(Throwable e) {
                if (MvpRef != null) {
                    MvpRef.get().dismissProgress();
                    MvpRef.get().stopRefresh();
                }
                Log.i("获取用户信息失败");
            }
        });
    }
}
