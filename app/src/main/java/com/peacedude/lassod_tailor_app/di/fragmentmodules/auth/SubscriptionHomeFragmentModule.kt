package com.peacedude.lassod_tailor_app.di.fragmentmodules.auth

import com.peacedude.lassod_tailor_app.ui.*
import com.peacedude.lassod_tailor_app.ui.subscription.SubscriptionHomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SubscriptionHomeFragmentModule {
    /**
     * A abstract function inject Subscription Home Fragment from DaggerGraph
     */
    @ContributesAndroidInjector()
    abstract fun provideSubscriptionHomeFragment(): SubscriptionHomeFragment
}
