package com.peacedude.lassod_tailor_app.di.activitymodules

import com.peacedude.lassod_tailor_app.di.fragmentmodules.auth.MediaFragmentModule
import com.peacedude.lassod_tailor_app.di.fragmentmodules.auth.ProfileFragmentModule
import com.peacedude.lassod_tailor_app.di.fragmentmodules.auth.SpecialtyFragmentModule
import com.peacedude.lassod_tailor_app.di.fragmentmodules.auth.UserAccountFragmentModule
import com.peacedude.lassod_tailor_app.di.networkmodules.auth.AuthRequestModule
import com.peacedude.lassod_tailor_app.di.viewmodelmodules.auth.AuthViewModelModule
import com.peacedude.lassod_tailor_app.ui.DashboardActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class DashboardActivityModule {
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
            MediaFragmentModule::class
        ]
    )
    abstract fun provideDashboardActivity(): DashboardActivity
}