package com.peacedude.lassod_tailor_app.di.fragmentmodules.auth

import com.peacedude.lassod_tailor_app.ui.*
import com.peacedude.lassod_tailor_app.ui.resources.SingleVideoFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SingleVideoFragmentModule {
    /**
     * A abstract function inject Add Card Fragment from DaggerGraph
     */
    @ContributesAndroidInjector()
    abstract fun provideSingleFragment(): SingleVideoFragment
}
