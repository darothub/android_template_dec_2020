package com.peacedude.lassod_tailor_app.di.fragmentmodules.auth

import com.peacedude.lassod_tailor_app.ui.*
import com.peacedude.lassod_tailor_app.ui.clientmanagement.ClientAccountFragment
import com.peacedude.lassod_tailor_app.ui.resources.AllVideoFragment
import com.peacedude.lassod_tailor_app.ui.resources.ResourcesFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ResourceFragmentModule {
    /**
     * A abstract function inject Resources Fragment from DaggerGraph
     */
    @ContributesAndroidInjector()
    abstract fun provideResourcesFragment(): ResourcesFragment

}