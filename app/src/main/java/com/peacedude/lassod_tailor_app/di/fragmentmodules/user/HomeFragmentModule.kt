package com.peacedude.lassod_tailor_app.di.fragmentmodules.user

import com.peacedude.lassod_tailor_app.ui.HomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class HomeFragmentModule {
    /**
     * A abstract function inject HomeFragment from DaggerGraph
     */
    @ContributesAndroidInjector()
    abstract fun provideLoginFragment(): HomeFragment
}