package com.peacedude.lassod_tailor_app.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.SavedStateHandle
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.di.activitymodules.*
import com.peacedude.lassod_tailor_app.di.networkmodules.auth.AuthRequestModule
import com.peacedude.lassod_tailor_app.di.networkmodules.user.ViewModelInterfaceModule
import com.peacedude.lassod_tailor_app.di.viewmodelmodules.auth.AuthViewModelModule
import com.peacedude.lassod_tailor_app.di.viewmodelmodules.factory.GeneralViewModelModule
import com.peacedude.lassod_tailor_app.di.viewmodelmodules.factory.ViewModelFactoryModule
import com.peacedude.lassod_tailor_app.network.storage.StorageRequest
import com.peacedude.lassod_tailor_app.ui.BaseActivity
import com.peacedude.lassod_tailor_app.utils.BASE_URL_STAGING
import com.peacedude.lassod_tailor_app.utils.storage.EncryptedSharedPrefManager
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(
    includes = [
        ActivityStaticModule::class,
        MainActivityModule::class,
        DashboardActivityModule::class,
        ProfileActivityModule::class,
        ClientActivityModule::class,
        ResourceActivityModule::class
    ]
)
abstract class ActivityBuilderModule {
    @ContributesAndroidInjector(
        modules = [
            ViewModelFactoryModule::class,
            GeneralViewModelModule::class,
            ViewModelInterfaceModule::class,
            AuthViewModelModule::class,
            AuthRequestModule::class
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
    fun provideGsonConverterFcatory(): GsonConverterFactory =
        GsonConverterFactory.create(GsonBuilder().setLenient().create())

    /**
     * A function to provide okHttp instance
     *
     */
    @Singleton
    @Provides
    open fun provideokHttpInstance(): okhttp3.OkHttpClient {
        return okhttp3.OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .build()
    }

    /**
     * A function to provide retrofit instance
     *
     */
    @Singleton
    @Provides
    open fun provideRetrofitInstance(
        gson: GsonConverterFactory,
        callAdapter: RxJava2CallAdapterFactory,
        client: okhttp3.OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_STAGING)
            .addCallAdapterFactory(callAdapter)
            .addConverterFactory(gson)
            .client(client)
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
    fun provideMasterKey(context: Context): MasterKey {
        return MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
    }

    @Singleton
    @Provides
    fun providePrefKeyEncryptionScheme(): EncryptedSharedPreferences.PrefKeyEncryptionScheme =
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV


    @Singleton
    @Provides
    fun providePrefValueEncryptionScheme(): EncryptedSharedPreferences.PrefValueEncryptionScheme =
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM


    @Singleton
    @Provides
    fun provideEncryptedSharedPreference(
        context: Context,
        masterKey: MasterKey,
        prefKey: EncryptedSharedPreferences.PrefKeyEncryptionScheme,
        prefValue: EncryptedSharedPreferences.PrefValueEncryptionScheme
    ): EncryptedSharedPreferences {
        return EncryptedSharedPreferences.create(
            context,
            "LassodFile",
            masterKey,
            prefKey,
            prefValue
        ) as EncryptedSharedPreferences
    }

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Singleton
    @Provides
    fun provideStorage(sharedPrefs: EncryptedSharedPreferences): StorageRequest {
        return EncryptedSharedPrefManager(sharedPrefs)
    }

    @Singleton
    @Provides
    fun provideSavedStateHandle() = SavedStateHandle()

    @Singleton
    @Provides
    fun provideGoogleSignInOptions(context: Context): GoogleSignInOptions =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(
            context.getString(
                R.string.client_id
            )
        ).requestEmail().build()

    @Singleton
    @Provides
    fun provideGoogleSigninClient(context: Context, gso: GoogleSignInOptions): GoogleSignInClient =
        GoogleSignIn.getClient(context, gso);


}