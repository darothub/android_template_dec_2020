package com.peacedude.lassod_tailor_app.di.fragmentmodules.auth

import com.peacedude.lassod_tailor_app.ui.*
import com.peacedude.lassod_tailor_app.ui.clientmanagement.ClientFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ClientFragmentModule {
    /**
     * A abstract function inject Client Fragment from DaggerGraph
     */
    @ContributesAndroidInjector()
    abstract fun provideClientFragment(): ClientFragment
}
