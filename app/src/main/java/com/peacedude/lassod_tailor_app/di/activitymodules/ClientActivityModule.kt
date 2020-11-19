package com.peacedude.lassod_tailor_app.di.activitymodules

import com.peacedude.lassod_tailor_app.di.fragmentmodules.auth.*
import com.peacedude.lassod_tailor_app.di.networkmodules.auth.AuthRequestModule
import com.peacedude.lassod_tailor_app.di.viewmodelmodules.auth.AuthViewModelModule
import com.peacedude.lassod_tailor_app.ui.clientmanagement.ClientActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ClientActivityModule {
    /**
     * A abstract function to provide for ProfileActivity from DaggerGraph
     */

    @ContributesAndroidInjector(
        modules = [
            AuthRequestModule::class,
            AuthViewModelModule::class,
            NativeMeasurementFragmentModule::class,
            EnglishMeasurementFragmentModule::class,
            ClientAccountFragmentModule::class,
            ClientFragmentModule::class,
            MeasurementFragmentModule::class,
            DeliveryAddressFragmentModule::class
        ]
    )
    abstract fun provideClientActivity(): ClientActivity
}