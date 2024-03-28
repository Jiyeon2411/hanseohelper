package com.example.hanseohelper_muso

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hanseohelper_muso.R
import android.webkit.WebView
import com.example.hanseohelper_muso.searchaddress.BridgeInterface
import android.webkit.WebViewClient
import android.webkit.JavascriptInterface
import android.content.Intent
import android.app.Activity

class searchaddress : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchaddress)
        val webView = findViewById<WebView>(R.id.searchaddresswebview)
        webView.settings.javaScriptEnabled = true
        webView.addJavascriptInterface(BridgeInterface(), "Android")
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                //Android -> Javascript 함수 호출!
                webView.loadUrl("javascript:sample2_execDaumPostcode();")
            }
        }

        //최초 웹뷰 로드
        webView.loadUrl("https://hanseo-helper.web.app")
    }

    private inner class BridgeInterface {
        @JavascriptInterface
        fun processDATA(data: String?) {
            // 카카오 주소 검색 API의 결과 값이 브릿지 통로를 통해 전달 받는다.
            val intent = Intent()
            intent.putExtra("data", data)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}