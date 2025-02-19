package com.peacedude.lassod_tailor_app.di.fragmentmodules.auth

import com.peacedude.lassod_tailor_app.ui.*
import com.peacedude.lassod_tailor_app.ui.clientmanagement.ClientAccountFragment
import com.peacedude.lassod_tailor_app.ui.resources.AllVideoFragment
import com.peacedude.lassod_tailor_app.ui.subscription.AddCardFragment
import com.peacedude.lassod_tailor_app.ui.subscription.SubscriptionPaymentFragment
import com.peacedude.lassod_tailor_app.ui.subscription.SubscriptionPlansFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class SubscriptionPlansFragmentModule {
    /**
     * A abstract function inject Subscription Plans Fragment from DaggerGraph
     */
    @ContributesAndroidInjector()
    abstract fun provideSubscriptionPlansFragment(): SubscriptionPlansFragment

}