package com.peacedude.lassod_tailor_app.di.fragmentmodules.user

import com.peacedude.lassod_tailor_app.ui.SignupFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SignupFragmentModule {
    /**
     * A abstract function inject RegisterFragment from DaggerGraph
     */

    @ContributesAndroidInjector()
    abstract fun provideSignupFragment(): SignupFragment
}