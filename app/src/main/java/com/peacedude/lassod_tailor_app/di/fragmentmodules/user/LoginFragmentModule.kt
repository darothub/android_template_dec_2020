package com.peacedude.lassod_tailor_app.di.fragmentmodules.user

import com.peacedude.lassod_tailor_app.ui.LoginFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class LoginFragmentModule {
    /**
     * A abstract function inject LoginFragment from DaggerGraph
     */

    @ContributesAndroidInjector()
    abstract fun provideLoginFragment(): LoginFragment
}