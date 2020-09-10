package com.peacedude.lassod_tailor_app.di.fragmentmodules.user

import com.peacedude.lassod_tailor_app.ui.*
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class EmailSignupFragmentModule {
    /**
     * A abstract function inject Email Signup Fragment from DaggerGraph
     */
    @ContributesAndroidInjector()
    abstract fun provideEmailSignupFragment(): EmailSignupFragment
}