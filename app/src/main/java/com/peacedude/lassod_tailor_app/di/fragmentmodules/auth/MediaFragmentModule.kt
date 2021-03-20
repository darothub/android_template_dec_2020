package com.peacedude.lassod_tailor_app.di.fragmentmodules.auth

import com.peacedude.lassod_tailor_app.ui.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MediaFragmentModule {
    /**
     * A abstract function inject Media Fragment from DaggerGraph
     */
    @ContributesAndroidInjector()
    abstract fun provideMediaFragment(): MediaFragment
}
