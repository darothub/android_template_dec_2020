package com.peacedude.lassod_tailor_app.di.fragmentmodules.auth

import com.peacedude.lassod_tailor_app.ui.SpecialtyFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SpecialtyFragmentModule {
    /**
     * A abstract function inject Specialty Fragment from DaggerGraph
     */
    @ContributesAndroidInjector()
    abstract fun provideSpecialtyFragment(): SpecialtyFragment
}
