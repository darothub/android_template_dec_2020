package com.peacedude.lassod_tailor_app.di.viewmodelmodules.factory

import androidx.lifecycle.ViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.GeneralViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.viewmodelfactory.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class GeneralViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(GeneralViewModel::class)
    abstract fun bindGeneralViewModel(generalViewModel: GeneralViewModel): ViewModel
}