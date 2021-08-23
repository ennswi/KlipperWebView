package de.ennswi.klipperwebview

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient


class KlipperWebViewClient(pActivity: Activity) : WebViewClient() {
    private lateinit var webView: WebView
    private var activity = pActivity
    private var isLockEnabled: Boolean = false

    private val TAG = "KlipperWebViewClient"

    fun injectCSS() = try {
        val inputStream = this.activity.assets.open("style.css")
        val buffer = ByteArray(inputStream.available())
        inputStream.read(buffer)
        inputStream.close()
        val encoded = Base64.encodeToString(buffer, Base64.NO_WRAP)
        webView.loadUrl(
            "javascript:(function() {" +
                    "var parent = document.getElementsByTagName('head').item(0);" +
                    "var style = document.createElement('style');" +
                    "style.type = 'text/css';" +
                    // Tell the browser to BASE64-decode the string into your script !!!
                    "style.innerHTML = window.atob('" + encoded + "');" +
                    "parent.appendChild(style)" +
                    "})()")
    } catch (e: Exception) {
        Log.e("KlipperWebViewClient","cannot Load CS File",e)
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {

        if (isLockEnabled) {
            injectCSS();
        }

        webView = view!!
        super.onPageStarted(view, url, favicon)
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        if (request?.url.toString().indexOf("journaldev.com") > -1) return false

        val intent = Intent(Intent.ACTION_VIEW, request?.url)
        activity.startActivity(intent)
        return true
    }

    fun removeCSSInjection() {
        isLockEnabled = false;
        webView?.reload();
    }

    fun enableCSSInjection() {
        isLockEnabled = true;
        webView?.reload();
    }
    fun setLockEnabled(lock: Boolean){
        isLockEnabled=lock;
    }
}