package com.peacedude.lassod_tailor_app.di.activitymodules

import com.peacedude.lassod_tailor_app.di.fragmentmodules.auth.*
import com.peacedude.lassod_tailor_app.di.fragmentmodules.customer.SearchFragmentModule
import com.peacedude.lassod_tailor_app.di.networkmodules.auth.AuthRequestModule
import com.peacedude.lassod_tailor_app.di.viewmodelmodules.auth.AuthViewModelModule
import com.peacedude.lassod_tailor_app.ui.clientmanagement.ClientActivity
import com.peacedude.lassod_tailor_app.ui.customer.CustomerActivity
import com.peacedude.lassod_tailor_app.ui.customer.SearchFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class CustomerActivityModule {
    /**
     * A abstract function to provide for ProfileActivity from DaggerGraph
     */

    @ContributesAndroidInjector(
        modules = [
            AuthRequestModule::class,
            AuthViewModelModule::class,
            SearchFragmentModule::class
        ]
    )
    abstract fun provideCustomerActivity(): CustomerActivity
}