package com.peacedude.lassod_tailor_app.di.viewmodelmodules.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Provider

@Module
internal object ViewModelFactoryModule{
    @Provides
    fun viewModelFactory(creators: MutableMap<Class<out ViewModel>, Provider<ViewModel>>): ViewModelProvider.Factory{
        return ViewModelFactory(creators)
    }
}