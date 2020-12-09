package com.peacedude.lassod_tailor_app.di.activitymodules

import com.peacedude.lassod_tailor_app.di.fragmentmodules.auth.*
import com.peacedude.lassod_tailor_app.di.fragmentmodules.user.ProfileManagementFragmentModule
import com.peacedude.lassod_tailor_app.di.networkmodules.auth.AuthRequestModule
import com.peacedude.lassod_tailor_app.di.viewmodelmodules.auth.AuthViewModelModule
import com.peacedude.lassod_tailor_app.ui.clientmanagement.ClientActivity
import com.peacedude.lassod_tailor_app.ui.profile.ProfileActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ProfileActivityModule {
    /**
     * A abstract function to provide for ProfileActivity from DaggerGraph
     */

    @ContributesAndroidInjector(
        modules = [
            AuthRequestModule::class,
            UserAccountFragmentModule::class,
            SpecialtyFragmentModule::class,
            AuthViewModelModule::class,
            ProfileFragmentModule::class,
            PaymentMethodFragmentModule::class,
            SecurityFragmentModule::class,
            ProfileManagementFragmentModule::class
        ]
    )
    abstract fun provideProfileActivity(): ProfileActivity
}