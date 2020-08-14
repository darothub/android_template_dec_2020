package com.peacedude.lassod_tailor_app.helpers

import android.app.Activity
import android.os.Build


inline fun Any.buildVersion(forSdkGreaterThankM:()->Unit, forSdkLesserThanM:()->Unit){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        forSdkGreaterThankM()
    }else{
        forSdkLesserThanM()
    }
}