package com.peacedude.lassod_tailor_app.di.fragmentmodules.auth

import com.peacedude.lassod_tailor_app.ui.HomeFragment
import com.peacedude.lassod_tailor_app.ui.SignupChoicesFragment
import com.peacedude.lassod_tailor_app.ui.SpecialtyFragment
import com.peacedude.lassod_tailor_app.ui.UserAccountFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class SignupChoicesFragmentModule {
    /**
     * A abstract function inject Signup Choices Fragment from DaggerGraph
     */
    @ContributesAndroidInjector()
    abstract fun provideSignupChoiceFragment(): SignupChoicesFragment
}