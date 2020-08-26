package com.peacedude.lassod_tailor_app.di.activitymodules

import com.peacedude.lassod_tailor_app.di.fragmentmodules.user.LoginFragmentModule
import com.peacedude.lassod_tailor_app.di.fragmentmodules.user.SignupFragmentModule
import com.peacedude.lassod_tailor_app.di.fragmentmodules.user.VerificationFragmentModule
import com.peacedude.lassod_tailor_app.di.networkmodules.auth.AuthRequestModule
import com.peacedude.lassod_tailor_app.di.networkmodules.user.UserRequestsModule
import com.peacedude.lassod_tailor_app.di.viewmodelmodules.auth.AuthViewModelModule
import com.peacedude.lassod_tailor_app.di.viewmodelmodules.factory.ViewModelFactoryModule
import com.peacedude.lassod_tailor_app.di.viewmodelmodules.user.UserViewModelModule
import com.peacedude.lassod_tailor_app.ui.MainActivity
import com.peacedude.lassod_tailor_app.ui.ProfileActivity
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
            AuthViewModelModule::class
        ]
    )
    abstract fun provideProfileActivity(): ProfileActivity
}