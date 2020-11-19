package com.peacedude.lassod_tailor_app.di.fragmentmodules.auth

import com.peacedude.lassod_tailor_app.ui.*
import com.peacedude.lassod_tailor_app.ui.clientmanagement.ClientAccountFragment
import com.peacedude.lassod_tailor_app.ui.clientmanagement.DeliveryAddressFragment
import com.peacedude.lassod_tailor_app.ui.clientmanagement.MeasurementFragment
import com.peacedude.lassod_tailor_app.ui.clientmanagement.NativeMeasurementFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class DeliveryAddressFragmentModule {
    /**
     * A abstract function inject  Delivery address Fragment from DaggerGraph
     */
    @ContributesAndroidInjector()
    abstract fun provideDeliveryAddressFragment(): DeliveryAddressFragment
}