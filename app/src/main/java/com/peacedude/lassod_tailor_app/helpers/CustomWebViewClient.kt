package com.peacedude.lassod_tailor_app.helpers

import android.util.Log
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView

class CustomWebViewClient:WebChromeClient() {
    val title = getName()
    val murl = "https://obioma-staging.herokuapp.com/api/v1/auth/google"
    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
        var prog = newProgress
        var url = view?.url
        Log.i(title, "url $newProgress")
        Log.i(title, "url $url")

        if (newProgress > 10){
            Log.i(title, "new $newProgress")
            Log.i(title, "url $url")
        }

    }

    override fun onReceivedTitle(view: WebView?, title: String?) {
        super.onReceivedTitle(view, title)
        Log.i(title, "new Title $title")
    }


}