package com.achpay.wallet.mvp.cointype.pages;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.achpay.wallet.R;
import com.achpay.wallet.base.view.BaseFragment;
import com.achpay.wallet.database.dbmodel.Cryptocurrency;
import com.achpay.wallet.database.dbmodel.CryptocurrencyManger;
import com.achpay.wallet.model.params.TransParams;
import com.achpay.wallet.mvp.qrcode.QrcodeScanActivity;
import com.achpay.wallet.mvp.qrcode.ScanQrcodeActivity;
import com.achpay.wallet.mvp.receipt.CoinReceiptActivity;
import com.achpay.wallet.utils.CommonUtil;
import com.achpay.wallet.view.CointypeMenuDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import org.greenrobot.greendao.annotation.NotNull;

import java.util.List;

public class CoinFragment extends BaseFragment<CoinFragmentPresenter> implements CoinFragmentView, OnRefreshListener, OnLoadMoreListener, CoinAdapter.OnItemClickLitener {
    private RecyclerView mRecyclerView;
    private SmartRefreshLayout mRefreshLayout;
    private List<Cryptocurrency> mCryptocurrencyList;
    private CoinAdapter mAdapter;

    private CointypeMenuDialog menuDialog;
    private String title;
    private String amount;
    private String currencyId;

    @Override
    protected CoinFragmentPresenter bindPresenter() {
        return new CoinFragmentPresenter(this);
    }

    public static CoinFragment newInstance(String title, String amount,String currencyId) {
        Bundle bundle = new Bundle();
        bundle.putString(TransParams.TITLE, title);
        bundle.putString(TransParams.AMOUNT, amount);
        bundle.putString(TransParams.CURRENCY_ID, currencyId);
        CoinFragment fragment = new CoinFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            title = arguments.getString(TransParams.TITLE);
            amount = arguments.getString(TransParams.AMOUNT);
            currencyId = arguments.getString(TransParams.CURRENCY_ID);
        }

        View mainView = inflater.inflate(R.layout.fragment_cointype_common, container, false);
        initView(mainView);
        return mainView;
    }

    private void initView(View mainView) {
        mRecyclerView = mainView.findViewById(R.id.cointype_rv);
        mRefreshLayout = mainView.findViewById(R.id.cointype_refresh);

        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadMoreListener(this);

        mRefreshLayout.setEnableLoadMore(false);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getSelfActivity()));

        mCryptocurrencyList = CryptocurrencyManger.getInstance().queryAllCryptocurrency();

        mAdapter = new CoinAdapter(getSelfActivity(), title);

        if (mCryptocurrencyList == null || mCryptocurrencyList.size() == 0) {
            mRefreshLayout.autoRefresh();
        } else {
            mAdapter.setDatas(mCryptocurrencyList);
        }

        mAdapter.setOnItemClickLitener(this);

        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        MvpPre.getCoinType(getSelfActivity());
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {

    }

    @Override
    public void stopLoading(boolean haveMore) {
        if (mRefreshLayout.isLoading()) {
            if (!haveMore) {
                mRefreshLayout.finishLoadMoreWithNoMoreData();
            } else {
                mRefreshLayout.finishLoadMore();
            }
        }
    }

    @Override
    public void stopRefresh() {
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.finishRefresh();
        }
    }

    @Override
    public void setDatas() {
        mCryptocurrencyList = CryptocurrencyManger.getInstance().queryAllCryptocurrency();

        mAdapter.setDatas(mCryptocurrencyList);
    }

    @Override
    public void onItemClick(View view, int position, final Cryptocurrency cryptocurrency) {
        //createQRCode(cryptocurrency);

        if (CommonUtil.supportRaidenNetwork(cryptocurrency.getCoinunique())) {

            if (menuDialog == null) {
                menuDialog = new CointypeMenuDialog(getSelfActivity());
            }

            menuDialog.setOnCreateQRListener(new CointypeMenuDialog.OnCreateQRListener() {
                @Override
                public void onCreateQR() {
                    createQRCode(cryptocurrency);
                }
            });
            menuDialog.setOnScanQRListener(new CointypeMenuDialog.OnScanQRListener() {
                @Override
                public void onScanQR() {
                    AndPermission.with(getSelfActivity())
                            .runtime()
                            .permission(Permission.CAMERA)
                            .onGranted(new Action<List<String>>() {
                                @Override
                                public void onAction(List<String> data) {
                                    if (AndPermission.hasPermissions(getSelfActivity(), Permission.CAMERA)) {
//                                        Intent intent = new Intent(getSelfActivity(), ScanQrcodeActivity.class);
                                        Intent intent = new Intent(getSelfActivity(), QrcodeScanActivity.class);

                                        intent.putExtra(TransParams.TYPE, TransParams.CURRENCY);//类型为法币
                                        intent.putExtra(TransParams.CURRENCY_ID, currencyId);//法币id
                                        intent.putExtra(TransParams.CURRENCY_NAME, CommonUtil.getCurrencyTypeById(getSelfActivity(), currencyId));//法币名字
                                        intent.putExtra(TransParams.AMOUNT, amount);//法币金额
                                        intent.putExtra(TransParams.COIN_NAME, cryptocurrency.getCryptoCurrency());//需要转换的虚拟币名字
                                        intent.putExtra(TransParams.COIN_ID, cryptocurrency.getCoinunique());//虚拟币唯一ID

                                        startActivity(intent);

                                        getSelfActivity().finish();

                                    } else {
                                        Toast.makeText(getSelfActivity(), getString(R.string.no_camera_permission), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .onDenied(new Action<List<String>>() {
                                @Override
                                public void onAction(List<String> data) {
                                    Toast.makeText(getSelfActivity(), getString(R.string.no_camera_permission), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .start();
                }
            });
            menuDialog.show();
        } else {
            createQRCode(cryptocurrency);
        }
    }

    private void createQRCode(@NotNull Cryptocurrency cryptocurrency) {
        Intent intent = new Intent();
        intent.setClass(getSelfActivity(), CoinReceiptActivity.class);

        intent.putExtra(TransParams.TYPE, TransParams.CURRENCY);//类型为法币
        intent.putExtra(TransParams.CURRENCY_ID, currencyId);//法币id
        intent.putExtra(TransParams.CURRENCY_NAME, CommonUtil.getCurrencyTypeById(getSelfActivity(), currencyId));//法币名字
        intent.putExtra(TransParams.AMOUNT, amount);//法币金额
        intent.putExtra(TransParams.COIN_NAME, cryptocurrency.getCryptoCurrency());//需要转换的虚拟币名字
        intent.putExtra(TransParams.COIN_ID, cryptocurrency.getCoinunique());//虚拟币唯一ID

        getSelfActivity().startActivity(intent);
        getSelfActivity().finish();
    }
}
