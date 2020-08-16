package com.peacedude.lassod_tailor_app.di.fragmentmodules.user

import com.peacedude.lassod_tailor_app.data.viewmodel.user.UserViewModel
import com.peacedude.lassod_tailor_app.di.networkmodules.user.UserRequestsModule
import com.peacedude.lassod_tailor_app.di.viewmodelmodules.factory.ViewModelFactoryModule
import com.peacedude.lassod_tailor_app.di.viewmodelmodules.user.UserViewModelModule
import com.peacedude.lassod_tailor_app.network.user.UserRequestInterface
import com.peacedude.lassod_tailor_app.ui.MainActivity
import com.peacedude.lassod_tailor_app.ui.SignupFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class MainActivityModule {
    /**
     * A abstract function to provide for MainActivity from DaggerGraph
     */

    @ContributesAndroidInjector(  modules = [
        SignupFragmentModule::class,
        UserRequestsModule::class,
        UserViewModelModule::class,
        ViewModelFactoryModule::class
    ])
    abstract fun provideMainActivity(): MainActivity
}