package com.comp9323.qa;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.comp9323.main.R;

public class QAWebView extends Fragment {

    private WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qa, container, false);

        webView = view.findViewById(R.id.qa_web_view);

        //set starting url
        webView.loadUrl("http://52.65.129.3:8080/questions/");

        //set allow js in this web view
        webView.getSettings().setJavaScriptEnabled(true);

        //set load url content in this web view
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }
        });

        return view;
    }
}
