package com.defaultapps.producthuntviewer.ui.activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.defaultapps.producthuntviewer.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class WebViewActivity extends AppCompatActivity {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.webView)
    WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        getSupportActionBar().setTitle(intent.getStringExtra("Name"));
        loadWebPage(intent.getStringExtra("URL"));
    }

    private void loadWebPage(String url) {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressBar.setVisibility(View.VISIBLE);
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }
}
