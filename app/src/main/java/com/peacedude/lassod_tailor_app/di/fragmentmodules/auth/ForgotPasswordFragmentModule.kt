package com.peacedude.lassod_tailor_app.di.fragmentmodules.auth

import com.peacedude.lassod_tailor_app.ui.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ForgotPasswordFragmentModule {
    /**
     * A abstract function inject Forgot Password Fragment from DaggerGraph
     */
    @ContributesAndroidInjector()
    abstract fun provideForgotPasswordFragment(): ForgotPassword
}
