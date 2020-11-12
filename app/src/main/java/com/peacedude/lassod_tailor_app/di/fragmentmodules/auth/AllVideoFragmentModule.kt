package com.peacedude.lassod_tailor_app.di.fragmentmodules.auth

import com.peacedude.lassod_tailor_app.ui.*
import com.peacedude.lassod_tailor_app.ui.clientmanagement.ClientAccountFragment
import com.peacedude.lassod_tailor_app.ui.resources.AllVideoFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class AllVideoFragmentModule {
    /**
     * A abstract function inject All Video Fragment from DaggerGraph
     */
    @ContributesAndroidInjector()
    abstract fun provideAllVideoFragment(): AllVideoFragment

}