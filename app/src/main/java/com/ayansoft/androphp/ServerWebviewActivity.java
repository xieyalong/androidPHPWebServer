package com.ayansoft.androphp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ServerWebviewActivity extends Activity{
    WebView web;
    String url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_webview);
        url=getIntent().getStringExtra("url");
        System.out.println(">]webview url="+url);
        web = this.findViewById(R.id.web);
        web.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);// 非缓存方式
        web.clearCache(true);
        web.setVerticalScrollBarEnabled(false);
        web.setHorizontalScrollBarEnabled(false);
        WebSettings settings = web.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBlockNetworkImage(false);
        settings.setSupportZoom(true);
        // 设置允许JS弹窗
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDefaultTextEncodingName("utf-8");

        loadPage(url);

    }



    @SuppressLint("JavascriptInterface")
    void loadPage(String url) {
        web.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

        });

        web.setWebViewClient(new EmbeddedWebViewClient());
        web.addJavascriptInterface(this, "appjs");
        web.loadUrl(url);
    }


    private class EmbeddedWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        }

        @Override
        public void onPageFinished(WebView wv, String url) {
            super.onPageFinished(wv, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // view.getSettings().setDefaultTextEncodingName("gbk");
            super.onPageStarted(view, url, favicon);
        }
    }



}
