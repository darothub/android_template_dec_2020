package com.peacedude.lassod_tailor_app.di.fragmentmodules.user

import com.peacedude.lassod_tailor_app.ui.DrawerFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class DrawerFragmentModule {
    /**
     * A abstract function inject DrawerFragment from DaggerGraph
     */

    @ContributesAndroidInjector()
    abstract fun provideDrawerFragment(): DrawerFragment
}
