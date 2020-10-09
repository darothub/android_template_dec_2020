package com.peacedude.lassod_tailor_app.di


import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class BaseApplication : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }
}