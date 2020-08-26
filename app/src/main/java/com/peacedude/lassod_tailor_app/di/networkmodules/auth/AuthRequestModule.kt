package com.peacedude.lassod_tailor_app.di.networkmodules.auth

import com.peacedude.lassod_tailor_app.data.repositories.auth.AuthRepository
import com.peacedude.lassod_tailor_app.data.repositories.user.UserRequestRepository
import com.peacedude.lassod_tailor_app.network.auth.AuthRequestInterface
import com.peacedude.lassod_tailor_app.network.user.UserRequestInterface
import com.peacedude.lassod_tailor_app.services.auth.AuthServices
import com.peacedude.lassod_tailor_app.services.user.UserServices
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

/**
 * A class to provide retrofit instance for user api services
 *
 */
@Module
class AuthRequestModule {
    @Provides
    fun provideAuthServices(retrofit: Retrofit): AuthServices {
        return retrofit.create(AuthServices::class.java)
    }

    /**
     * A function provides an instance of the user repository
     *@param authApiRequests is a retrofit instance
     */
    @Provides
    fun provideUserCall(authServices: AuthServices): AuthRequestInterface {
        return AuthRepository(
            authServices
        )
    }

}