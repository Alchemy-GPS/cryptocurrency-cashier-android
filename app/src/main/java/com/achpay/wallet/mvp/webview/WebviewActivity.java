package com.achpay.wallet.mvp.webview;

import android.app.Activity;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.achpay.wallet.Constants;
import com.achpay.wallet.R;
import com.achpay.wallet.model.params.TransParams;
import com.achpay.wallet.utils.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class WebviewActivity extends Activity {
    private static final String TAG = "WebviewActivity";

    private WebSettings settings;
    private WebView webView;
    private String url;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        webView = findViewById(R.id.web_webview);
        mProgressBar = findViewById(R.id.web_progressbar);

        String address = getIntent().getStringExtra(TransParams.WEB_ADDRESS);
        String coinId = getIntent().getStringExtra(TransParams.COIN_ID);

        try {

            if (coinId.equals("1")) {//BTC
                url = URLDecoder.decode(Constants.BITCOIN_EXPLORE_URL + address, "UTF-8");
            } else if (coinId.equals("2")) {//ETH
                url = URLDecoder.decode(Constants.ETHEREUM_EXPLORE_URL + address, "UTF-8");
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        settings = webView.getSettings();

        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        webView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                Log.i("WEBVIEW_URL == " + url);
                webView.loadUrl(url);
                return Build.VERSION.SDK_INT < 26;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
                super.onReceivedSslError(view, handler, error);
            }

        });

        webView.setWebChromeClient(new WebChromeClient(){



            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                mProgressBar.setProgress(newProgress);//设置进度值
                if(newProgress==100){
                    mProgressBar.setVisibility(View.GONE);//加载完网页进度条消失
                } else{
                    mProgressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                }
            }
        });

        webView.loadUrl(url);
    }
}
