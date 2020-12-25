package com.peacedude.lassod_tailor_app.di.activitymodules

import com.peacedude.lassod_tailor_app.di.fragmentmodules.auth.*
import com.peacedude.lassod_tailor_app.di.fragmentmodules.customer.FavouriteFragmentModule
import com.peacedude.lassod_tailor_app.di.networkmodules.auth.AuthRequestModule
import com.peacedude.lassod_tailor_app.di.networkmodules.user.UserRequestsModule
import com.peacedude.lassod_tailor_app.di.viewmodelmodules.auth.AuthViewModelModule
import com.peacedude.lassod_tailor_app.di.viewmodelmodules.user.UserViewModelModule
import com.peacedude.lassod_tailor_app.network.user.UserRequestInterface
import com.peacedude.lassod_tailor_app.ui.DashboardActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class DashboardActivityModule {
    /**
     * A abstract function to provide for DashboardActivity from DaggerGraph
     */

    @ContributesAndroidInjector(
        modules = [
            AuthRequestModule::class,
            UserAccountFragmentModule::class,
            SpecialtyFragmentModule::class,
            AuthViewModelModule::class,
            UserViewModelModule::class,
            UserRequestsModule::class,
            ProfileFragmentModule::class,
            MediaFragmentModule::class,
            SingleChatFragmentModule::class,
            MessageFragmentModule::class,
            FavouriteFragmentModule::class
        ]
    )
    abstract fun provideDashboardActivity(): DashboardActivity
}