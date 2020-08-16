package com.peacedude.lassod_tailor_app.di.viewmodelmodules.user

import androidx.lifecycle.ViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.user.UserViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.viewmodelfactory.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class UserViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel::class)
    abstract fun bindUserViewModel(registerUserViewModel: UserViewModel): ViewModel
}

