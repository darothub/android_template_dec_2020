package com.peacedude.lassod_tailor_app.di.fragmentmodules.auth

import com.peacedude.lassod_tailor_app.ui.*
import com.peacedude.lassod_tailor_app.ui.clientmanagement.ClientAccountFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ClientAccountFragmentModule {
    /**
     * A abstract function inject Client Account Fragment from DaggerGraph
     */
    @ContributesAndroidInjector()
    abstract fun provideClientAccountFragment(): ClientAccountFragment

}