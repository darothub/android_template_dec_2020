package com.peacedude.lassod_tailor_app.di.fragmentmodules.user

import com.peacedude.lassod_tailor_app.ui.LoginFragment
import com.peacedude.lassod_tailor_app.ui.VerificationFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector



@Module
abstract class VerificationFragmentModule {
    /**
     * A abstract function inject VerificationFragment from DaggerGraph
     */

    @ContributesAndroidInjector()
    abstract fun provideVerificationFragment(): VerificationFragment
}