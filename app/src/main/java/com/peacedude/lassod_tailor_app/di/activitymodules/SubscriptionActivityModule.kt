package com.peacedude.lassod_tailor_app.di.activitymodules

import com.peacedude.lassod_tailor_app.di.fragmentmodules.auth.*
import com.peacedude.lassod_tailor_app.di.networkmodules.auth.AuthRequestModule
import com.peacedude.lassod_tailor_app.di.viewmodelmodules.auth.AuthViewModelModule
import com.peacedude.lassod_tailor_app.ui.clientmanagement.ClientActivity
import com.peacedude.lassod_tailor_app.ui.subscription.SubscriptionActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SubscriptionActivityModule {
    /**
     * A abstract function to provide for Subscription Activity from DaggerGraph
     */

    @ContributesAndroidInjector(
        modules = [
            AddCardFragmentModule::class,
            AuthRequestModule::class,
            AuthViewModelModule::class

        ]
    )
    abstract fun provideSubscriptionActivity(): SubscriptionActivity
}