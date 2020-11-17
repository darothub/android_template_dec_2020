package com.peacedude.lassod_tailor_app.di.fragmentmodules.auth

import com.peacedude.lassod_tailor_app.ui.*
import com.peacedude.lassod_tailor_app.ui.clientmanagement.ClientAccountFragment
import com.peacedude.lassod_tailor_app.ui.clientmanagement.MeasurementFragment
import com.peacedude.lassod_tailor_app.ui.clientmanagement.NativeMeasurementFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class MeasurementFragmentModule {
    /**
     * A abstract function inject  Measurement Fragment from DaggerGraph
     */
    @ContributesAndroidInjector()
    abstract fun provideMeasurementFragment(): MeasurementFragment
}