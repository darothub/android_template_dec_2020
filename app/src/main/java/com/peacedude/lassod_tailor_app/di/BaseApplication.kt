package com.peacedude.lassod_tailor_app.di

import com.cloudinary.android.MediaManager
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class BaseApplication : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
//        MediaManager.init(this)
        return DaggerAppComponent.builder().application(this).build()
    }
}