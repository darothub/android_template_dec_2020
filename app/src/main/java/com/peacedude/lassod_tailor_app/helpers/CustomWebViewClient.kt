package com.peacedude.lassod_tailor_app.helpers

import android.os.Message
import android.util.Log
import android.webkit.*
import android.widget.Toast

class CustomWebViewClient(val action:()->Unit): WebViewClient() {
    val title = getName()

    override fun shouldInterceptRequest(
        view: WebView?,
        request: WebResourceRequest?
    ): WebResourceResponse? {

        val reqUrlPattern = Regex("""^https://standard.paystack.co/charge/""")
        if(reqUrlPattern.matches(request?.url.toString())){
            i(title, "Override yes ${request?.url}")
            action()

        }
        else{
            i(title, "Override no ${request?.url}")
        }

        return super.shouldInterceptRequest(view, request)
    }

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        i(title, "Should overrideUrlone $url")
        return true
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        val url = request?.url
        i(title, "Should overrideUrl $url")
        return super.shouldOverrideUrlLoading(view, request)
    }


}