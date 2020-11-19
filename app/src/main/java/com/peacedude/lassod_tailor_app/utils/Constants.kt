package com.peacedude.lassod_tailor_app.utils

import com.peacedude.lassod_tailor_app.services.auth.PayStackServices
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL_STAGING= "https://obioma-staging.herokuapp.com/api/v1/"
const val BASE_URL  = ""
const val loggedInUserKey = "loggedInUser"
const val bearer = "Bearer"
const val profileDataKey = "profileData"
const val newClientKey = "newclientkey"
const val BASE_URL_ANYTHING = "http://httpbin.org"
const val PAYSTACK_BASE = "https://api.paystack.co/transaction/"

object PayStackRetrofit{
    fun retrofitInstance():PayStackServices{
        return Retrofit.Builder()
            .baseUrl(PAYSTACK_BASE)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PayStackServices::class.java)
    }
}
