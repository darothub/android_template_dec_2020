package com.peacedude.lassod_tailor_app.di.activitymodules

import com.peacedude.lassod_tailor_app.di.fragmentmodules.auth.ForgotPasswordFragmentModule
import com.peacedude.lassod_tailor_app.di.fragmentmodules.auth.SignupChoicesFragmentModule
import com.peacedude.lassod_tailor_app.di.fragmentmodules.user.*
import com.peacedude.lassod_tailor_app.di.networkmodules.auth.AuthRequestModule
import com.peacedude.lassod_tailor_app.di.networkmodules.user.UserRequestsModule
import com.peacedude.lassod_tailor_app.di.viewmodelmodules.auth.AuthViewModelModule
import com.peacedude.lassod_tailor_app.di.viewmodelmodules.factory.ViewModelFactoryModule
import com.peacedude.lassod_tailor_app.di.viewmodelmodules.user.UserViewModelModule
import com.peacedude.lassod_tailor_app.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class MainActivityModule {
    /**
     * A abstract function to provide for MainActivity from DaggerGraph
     */

    @ContributesAndroidInjector(
        modules = [
            AuthViewModelModule::class,
            AuthRequestModule::class,
            SignupFragmentModule::class,
            UserRequestsModule::class,
            UserViewModelModule::class,
            LoginFragmentModule::class,
            VerificationFragmentModule::class,
            HomeFragmentModule::class,
            SignupChoicesFragmentModule::class,
            EmailSignupFragmentModule::class,
            PhoneSignupFragmentModule::class,
            ForgotPasswordFragmentModule::class
        ]
    )
    abstract fun provideMainActivity(): MainActivity
}