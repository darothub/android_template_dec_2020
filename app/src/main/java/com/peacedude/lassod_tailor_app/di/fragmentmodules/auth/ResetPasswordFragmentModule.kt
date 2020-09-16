package com.peacedude.lassod_tailor_app.di.fragmentmodules.auth

import com.peacedude.lassod_tailor_app.ui.*
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ResetPasswordFragmentModule {
    /**
     * A abstract function inject Reset Password Fragment from DaggerGraph
     */
    @ContributesAndroidInjector()
    abstract fun provideResetPasswordFragment(): ResetPasswordFragment
}