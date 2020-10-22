package com.peacedude.lassod_tailor_app.di.fragmentmodules.auth

import com.peacedude.lassod_tailor_app.ui.*
import com.peacedude.lassod_tailor_app.ui.clientmanagement.ClientAccountFragment
import com.peacedude.lassod_tailor_app.ui.profile.ProfileFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ProfileFragmentModule {
    /**
     * A abstract function inject Client Account Fragment from DaggerGraph
     */
    @ContributesAndroidInjector()
    abstract fun provideProfileFragment(): ProfileFragment
}