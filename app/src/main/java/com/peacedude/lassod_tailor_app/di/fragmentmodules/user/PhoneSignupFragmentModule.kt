package com.peacedude.lassod_tailor_app.di.fragmentmodules.user

import com.peacedude.lassod_tailor_app.ui.*
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class PhoneSignupFragmentModule {
    /**
     * A abstract function inject Phone Signup Fragment from DaggerGraph
     */
    @ContributesAndroidInjector()
    abstract fun providePhoneSignupFragment(): PhoneSignupFragment
}