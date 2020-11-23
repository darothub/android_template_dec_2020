package com.peacedude.lassod_tailor_app.di.activitymodules

import com.peacedude.lassod_tailor_app.di.fragmentmodules.auth.*
import com.peacedude.lassod_tailor_app.di.networkmodules.auth.AuthRequestModule
import com.peacedude.lassod_tailor_app.di.viewmodelmodules.auth.AuthViewModelModule
import com.peacedude.lassod_tailor_app.ui.clientmanagement.ClientActivity
import com.peacedude.lassod_tailor_app.ui.resources.ResourcesActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ResourceActivityModule {
    /**
     * A abstract function to provide for ProfileActivity from DaggerGraph
     */

    @ContributesAndroidInjector(
        modules = [
            AuthRequestModule::class,
            AuthViewModelModule::class,
            AllVideoFragmentModule::class,
            ResourceFragmentModule::class,
            SingleVideoFragmentModule::class
        ]
    )
    abstract fun provideResourceActivity(): ResourcesActivity
}