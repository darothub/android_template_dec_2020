package com.peacedude.lassod_tailor_app.helpers

import android.os.Message
import android.util.Log
import android.webkit.*
import android.widget.Toast

class CustomWebViewClient: WebViewClient() {
    val title = getName()
    override fun onPageFinished(view: WebView?, url: String?) {
        i(title, "On page finished $url")
    }

    override fun shouldInterceptRequest(view: WebView?, url: String?): WebResourceResponse? {

        i(title, "Intercept url $url")
        return super.shouldInterceptRequest(view, url)
    }

    override fun shouldInterceptRequest(
        view: WebView?,
        request: WebResourceRequest?
    ): WebResourceResponse? {
        val reqUrlPattern = Regex("""^https://checkout.paystack.com/\w+""")
        if(reqUrlPattern.matches(request?.url.toString())){
            i(title, "Override yes ${request?.url}")
        }
        else{
            i(title, "Override no ${request?.url}")
        }
        i(title, "Intercept request $request")
        return super.shouldInterceptRequest(view, request)
    }

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        i(title, "Override url $url")
        return super.shouldOverrideUrlLoading(view, url)
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        i(title, "Override url $request")
        return super.shouldOverrideUrlLoading(view, request)
    }



}