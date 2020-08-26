package com.peacedude.lassod_tailor_app.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.SavedStateHandle
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.peacedude.lassod_tailor_app.di.activitymodules.MainActivityModule
import com.peacedude.lassod_tailor_app.di.activitymodules.ProfileActivityModule
import com.peacedude.lassod_tailor_app.di.viewmodelmodules.factory.GeneralViewModelModule
import com.peacedude.lassod_tailor_app.di.viewmodelmodules.factory.ViewModelFactoryModule
import com.peacedude.lassod_tailor_app.network.storage.StorageRequest
import com.peacedude.lassod_tailor_app.ui.BaseActivity
import com.peacedude.lassod_tailor_app.utils.BASE_URL_STAGING
import com.peacedude.lassod_tailor_app.utils.storage.SharedPrefManager
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [ActivityStaticModule::class, MainActivityModule::class, ProfileActivityModule::class])
abstract class ActivityBuilderModule {
    @ContributesAndroidInjector(
        modules = [
            ViewModelFactoryModule::class,
            GeneralViewModelModule::class
        ]
    )
    abstract fun baseActivity(): BaseActivity
}

@Module
open class ActivityStaticModule {
    /**
     * A function to provide context
     *
     */

    @Singleton
    @Provides
    fun provideCallAdapterFactory(): RxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create()

    @Singleton
    @Provides
    fun provideGsonConverterFcatory(): GsonConverterFactory = GsonConverterFactory.create()

    /**
     * A function to provide retrofit instance
     *
     */
    @Singleton
    @Provides
    open fun provideRetrofitInstance(
        gson: GsonConverterFactory,
        callAdapter: RxJava2CallAdapterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_STAGING)
            .addCallAdapterFactory(callAdapter)
            .addConverterFactory(gson)
            .build()
    }

    @Singleton
    @Provides
    fun getContexts(application: Application): Context {
        return application.applicationContext
    }


    @Singleton
    @Provides
    fun provideSharedPreference(context: Context): SharedPreferences {
        return context.getSharedPreferences("lassod", Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Singleton
    @Provides
    fun provideStorage(sharedPrefs: SharedPreferences, gson: Gson): StorageRequest {
        return SharedPrefManager(sharedPrefs, gson)
    }

    @Singleton
    @Provides
    fun provideSavedStateHandle()= SavedStateHandle()

}