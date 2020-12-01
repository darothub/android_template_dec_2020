package com.peacedude.lassod_tailor_app.di


import androidx.lifecycle.MutableLiveData
import co.paystack.android.PaystackSdk
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.facebook.FacebookEmojiProvider
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication


class BaseApplication : DaggerApplication() {
    val netWorkLiveData = MutableLiveData<Boolean>()
    override fun onCreate() {
        super.onCreate()
//        PaystackSdk.initialize(applicationContext);
        EmojiManager.install(FacebookEmojiProvider())
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {

        return DaggerAppComponent.builder().application(this).build()
    }

}