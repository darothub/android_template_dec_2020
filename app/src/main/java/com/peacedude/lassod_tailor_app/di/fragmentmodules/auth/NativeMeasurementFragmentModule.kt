package com.peacedude.lassod_tailor_app.di.fragmentmodules.auth

import com.peacedude.lassod_tailor_app.ui.*
import com.peacedude.lassod_tailor_app.ui.clientmanagement.ClientAccountFragment
import com.peacedude.lassod_tailor_app.ui.clientmanagement.NativeMeasurementFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class NativeMeasurementFragmentModule {
    /**
     * A abstract function inject Native Measurement Fragment from DaggerGraph
     */
    @ContributesAndroidInjector()
    abstract fun provideNativeMeasurementFragment(): NativeMeasurementFragment
}