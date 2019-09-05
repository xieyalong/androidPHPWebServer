package com.ayansoft.androphp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.just.agentweb.AgentWeb;
import com.just.agentweb.DefaultWebClient;
import com.just.agentweb.LogUtils;

public class ServerWebviewActivity extends Activity{
    String url;
    LinearLayout mLinearLayout;
    AgentWeb mAgentWeb;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_webview);
        url=getIntent().getStringExtra("url");
        System.out.println(">]webview url="+url);
        mLinearLayout =findViewById(R.id.ll_html_body);
        initWebView();

    }

    public  void initWebView(){
        try {
            mAgentWeb = AgentWeb.with(this)//
                    .setAgentWebParent((LinearLayout) mLinearLayout, -1, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))//传入AgentWeb的父控件。
                    .useDefaultIndicator(-1, 3)//设置进度条颜色与高度，-1为默认值，高度为2，单位为dp。
                    .setWebChromeClient(mWebChromeClient)
                    .setWebViewClient(mWebViewClient)
                    //                .setAgentWebWebSettings(getSettings())//设置 IAgentWebSettings。
                    .setWebViewClient(mWebViewClient)//WebViewClient ， 与 WebView 使用一致 ，但是请勿获取WebView调用setWebViewClient(xx)方法了,会覆盖AgentWeb DefaultWebClient,同时相应的中间件也会失效。
                    //                .setWebChromeClient(new CommonWebChromeClient()) //WebChromeClient
                    //                .setPermissionInterceptor(mPermissionInterceptor) //权限拦截 2.0.0 加入。
                    .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK) //严格模式 Android 4.2.2 以下会放弃注入对象 ，使用AgentWebView没影响。
                    //                .setAgentWebUIController(new UIController(getActivity())) //自定义UI  AgentWeb3.0.0 加入。
                    .setMainFrameErrorView(R.layout.agentweb_error_page, -1) //参数1是错误显示的布局，参数2点击刷新控件ID -1表示点击整个布局都刷新， AgentWeb 3.0.0 加入。
                    //                .useMiddlewareWebChrome(getMiddlewareWebChrome()) //设置WebChromeClient中间件，支持多个WebChromeClient，AgentWeb 3.0.0 加入。
                    //                .additionalHttpHeader(getUrl(),"cookie","41bc7ddf04a26b91803f6b11817a5a1c")
                    //                .useMiddlewareWebClient(getMiddlewareWebClient()) //设置WebViewClient中间件，支持多个WebViewClient， AgentWeb 3.0.0 加入。
                    .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他页面时，弹窗质询用户前往其他应用 AgentWeb 3.0.0 加入。
                    .interceptUnkownUrl() //拦截找不到相关页面的Url AgentWeb 3.0.0 加入。
                    .createAgentWeb()//创建AgentWeb。
                    .ready()//设置 WebSettings。
                    .go(url); //WebV
            mAgentWeb.clearWebCache();
//            mAgentWeb.getJsInterfaceHolder().addJavaObject("APP",new AndroidInterface());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * WebChromeClient
     */
    private com.just.agentweb.WebChromeClient mWebChromeClient = new com.just.agentweb.WebChromeClient() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            System.out.println(">] WebChromeClient");
        }
    };
    /**
     * WebViewClient
     */
    private com.just.agentweb.WebViewClient mWebViewClient = new com.just.agentweb.WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            System.out.println(">] WebViewClient");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                }
            },2000);
        }
    };


}
