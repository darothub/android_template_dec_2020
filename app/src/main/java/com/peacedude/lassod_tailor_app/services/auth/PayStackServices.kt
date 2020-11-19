package com.peacedude.lassod_tailor_app.services.auth

import com.peacedude.lassod_tailor_app.model.response.AddressData
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface PayStackServices{

    @POST("initialize")
    @FormUrlEncoded
    suspend fun addDeliveryAddress(
        @Header("Authorization") header: String?,
        @Field("email") email:String?,
        @Field("amount") amount :String?
    ): UserResponse<AddressData>

}
