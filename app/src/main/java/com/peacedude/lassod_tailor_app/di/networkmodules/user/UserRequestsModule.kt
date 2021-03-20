package com.peacedude.lassod_tailor_app.di.networkmodules.user

import com.peacedude.lassod_tailor_app.data.repositories.user.UserRequestRepository
import com.peacedude.lassod_tailor_app.network.user.UserRequestInterface
import com.peacedude.lassod_tailor_app.services.user.UserServices
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

/**
 * A class to provide retrofit instance for user api services
 *
 */
@Module
class UserRequestsModule {
    @Provides
    fun provideUserServices(retrofit: Retrofit): UserServices {
        return retrofit.create(UserServices::class.java)
    }

    /**
     * A function provides an instance of the user repository
     *@param userApiRequests is a retrofit instance
     */
    @Provides
    fun provideUserCall(userServices: UserServices): UserRequestInterface {
        return UserRequestRepository(
            userServices
        )
    }
}
