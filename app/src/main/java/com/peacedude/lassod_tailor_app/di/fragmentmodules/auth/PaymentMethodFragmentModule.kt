package com.peacedude.lassod_tailor_app.di.fragmentmodules.auth

import com.peacedude.lassod_tailor_app.ui.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class PaymentMethodFragmentModule {
    /**
     * A abstract function inject Payment Method Fragment from DaggerGraph
     */
    @ContributesAndroidInjector()
    abstract fun providePaymentMethodFragment(): PaymentMethodFragment
}
