package com.czh.life_assistant;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class NewActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private WebView webView;

    private String title;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        showProgressDialog();
        webView = findViewById(R.id.webview);

        title = getIntent().getStringExtra("title");
        url = getIntent().getStringExtra("url");

        webView.getSettings().setSupportZoom(true);
        webView.getSettings().getBuiltInZoomControls();
        webView.setWebChromeClient(new WebViewClient());
        webView.loadUrl(url);
        webView.setWebViewClient(new android.webkit.WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

        });
    }

    public class WebViewClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress == 100) {
               closeProgressDialog();
            }
        }

    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(NewActivity.this);
            progressDialog.setMessage("正在查询...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
}
