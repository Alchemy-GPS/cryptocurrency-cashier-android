package com.achpay.wallet.mvp.mine;

import android.text.TextUtils;

import com.achpay.wallet.base.presenter.BasePresenter;
import com.achpay.wallet.model.UserInfoRequest;
import com.achpay.wallet.model.UserInfoResponse;
import com.achpay.wallet.model.params.ResponseParam;
import com.achpay.wallet.network.BaseSubscriber;
import com.achpay.wallet.network.RetrofitUtil;
import com.achpay.wallet.network.UniteResponse;
import com.achpay.wallet.utils.GsonUtil;
import com.achpay.wallet.utils.Log;

import java.util.List;

public class MinePresenter extends BasePresenter<MineView> {

    public MinePresenter(MineView view) {
        super(view);
    }

    public void getUserInfo() {

        UserInfoRequest request = new UserInfoRequest();
        request.setSign("ACHPAY");

        RetrofitUtil.getInstance().getUserInfo(request, new BaseSubscriber<UniteResponse<UserInfoResponse>>() {
            @Override
            public void onResponse(UniteResponse<UserInfoResponse> response) {
                Log.i(GsonUtil.objectToJson(response));
                if (MvpRef != null && TextUtils.equals(response.getReturnCode(), ResponseParam.SUCCESS_CODE)) {
                    UserInfoResponse userInfoResponse = response.getData();
                    MvpRef.get().setUserInfo(userInfoResponse);
                    List<UserInfoResponse.AccountInfos> accountInfos = userInfoResponse.getAccountInfos();
                    if (accountInfos != null && accountInfos.size() != 0) {
                        if (accountInfos.size() > 11) {
                            accountInfos = accountInfos.subList(0, 10);
                        }
                        MvpRef.get().setDatas(accountInfos);
                    }
                    MvpRef.get().stopRefresh();
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (MvpRef != null) {
                    MvpRef.get().stopRefresh();
                }
            }
        });
    }
}
