package com.peacedude.lassod_tailor_app.di.networkmodules.user

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.peacedude.lassod_tailor_app.data.repositories.user.UserRequestRepository
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.GeneralViewModel
import com.peacedude.lassod_tailor_app.network.storage.StorageRequest
import com.peacedude.lassod_tailor_app.network.user.UserRequestInterface
import com.peacedude.lassod_tailor_app.network.user.ViewModelInterface
import com.peacedude.lassod_tailor_app.services.user.UserServices
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

/**
 * A class to provide retrofit instance for user api services
 *
 */
@Module
class ViewModelInterfaceModule {

    /**
     * A function provides an instance of the viewmodel
     *@param retrofit is a retrofit instance
     * @param storageRequest is a shared pref  instance
     */
    @Provides
    fun provideViewModel(retrofit: Retrofit, storageRequest: StorageRequest, context: Context, mGoogleSignInClient: GoogleSignInClient): ViewModelInterface {
        return GeneralViewModel(
            retrofit, storageRequest, context,
            mGoogleSignInClient
        )
    }

}