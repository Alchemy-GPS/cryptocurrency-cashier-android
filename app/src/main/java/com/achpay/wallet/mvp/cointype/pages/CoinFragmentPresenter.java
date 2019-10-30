package com.achpay.wallet.mvp.cointype.pages;

import android.content.Context;

import com.achpay.wallet.base.presenter.BasePresenter;
import com.achpay.wallet.database.dbmodel.Cryptocurrency;
import com.achpay.wallet.database.dbmodel.CryptocurrencyManger;
import com.achpay.wallet.model.CoinTypeRequest;
import com.achpay.wallet.model.CoinTypeResponse;
import com.achpay.wallet.model.params.ResponseParam;
import com.achpay.wallet.network.BaseSubscriber;
import com.achpay.wallet.network.RetrofitUtil;
import com.achpay.wallet.network.UniteResponse;
import com.achpay.wallet.utils.GsonUtil;
import com.achpay.wallet.utils.Log;

import java.util.List;

public class CoinFragmentPresenter extends BasePresenter<CoinFragmentView> {

    public CoinFragmentPresenter(CoinFragmentView view) {
        super(view);
    }

    public void getCoinType(final Context mContext) {

        CoinTypeRequest request = new CoinTypeRequest(1);

        RetrofitUtil.getInstance().getCoinType(request, new BaseSubscriber<UniteResponse<CoinTypeResponse>>() {

            @Override
            public void onResponse(UniteResponse<CoinTypeResponse> response) {
                Log.i("COINTYPE == " + GsonUtil.objectToJson(response));
                if (MvpRef != null) {
                    if (response.getReturnCode().equals(ResponseParam.SUCCESS_CODE)) {
                        List<Cryptocurrency> mList = response.getData().getSupportCryptosList();
                        if (mList != null && mList.size() != 0) {
                            CryptocurrencyManger.getInstance().insertCryptocurrencys(mList);
                            MvpRef.get().setDatas();
                            MvpRef.get().stopRefresh();
                        }
                    } else {
                        MvpRef.get().stopRefresh();
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.i("COINTYPE == " + "获取币种信息失败" + "onError");
                e.printStackTrace();
                if (MvpRef != null) {
                    MvpRef.get().stopRefresh();
                }
            }
        });
    }
}
