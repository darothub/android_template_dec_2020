package com.peacedude.lassod_tailor_app.di.fragmentmodules.auth

import com.peacedude.lassod_tailor_app.ui.*
import com.peacedude.lassod_tailor_app.ui.subscription.SubscriptionPaymentFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SubscriptionPaymentFragmentModule {
    /**
     * A abstract function inject Subscription Payment Fragment from DaggerGraph
     */
    @ContributesAndroidInjector()
    abstract fun provideSubscriptionPaymentFragment(): SubscriptionPaymentFragment
}
