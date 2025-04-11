package vn.iotstart.bttuan10;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import vn.iotstart.bttuan10.databinding.ActivityMainBinding;

public class MainWebViewActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @SuppressLint({"SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Ẩn ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Cấu hình WebView
        WebSettings webSettings = binding.webview.getSettings();
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        binding.webview.setWebViewClient(new WebViewClient());
        binding.webview.setWebChromeClient(new WebChromeClient());

        // Load trang web
        binding.webview.loadUrl("http://iotstar.vn");
    }
}
