package com.peacedude.lassod_tailor_app.data.viewmodel.factory

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.peacedude.lassod_tailor_app.helpers.getName
import com.peacedude.lassod_tailor_app.model.error.ErrorModel
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import com.peacedude.lassod_tailor_app.model.response.ServicesResponseWrapper
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

open class GeneralViewModel @Inject constructor(open var retrofit: Retrofit): ViewModel() {

    open val title: String by lazy {
        this.getName()
    }
    fun onFailureResponse(
        responseLiveData: MutableLiveData<ServicesResponseWrapper<ParentData>>,
        t: Throwable
    ) {
        Log.i(title, "Throwable ${t.localizedMessage}")
        responseLiveData.postValue(ServicesResponseWrapper.Error(t.localizedMessage, 0, null))
    }
    fun onResponseTask(response: Response<ParentData>, responseLiveData: MutableLiveData<ServicesResponseWrapper<ParentData>>){
        val res = response.body()
        val statusCode = response.code()
        Log.i(title, "${response.code()}")
        Log.i(title, "errorbody ${response.raw()}")
        val a = object : Annotation{}
        val converter = retrofit.responseBodyConverter<ErrorModel>(
            ErrorModel::class.java, arrayOf(a))
        val error = converter.convert(response.errorBody())
        var errorString = ""
        when(statusCode) {

            401 -> {
                try {
                    errorConverter(error, errorString, error?.errors, responseLiveData, response)
                }
                catch (e:Exception){
                    Log.i(title, e.message.toString())
                    responseLiveData.postValue(ServicesResponseWrapper.Error(e.message, statusCode))
                }

            }
            in 400..501 ->{
                try{
                    errorConverter(error, errorString, error?.errors, responseLiveData, response)
                }
                catch (e:Exception){
                    Log.i(title, "Hello" + " " + e.message.toString())
                    responseLiveData.postValue(ServicesResponseWrapper.Error(e.message, statusCode))
                }


            }
            else -> {
                try {
                    Log.i(title, "success ${response.errorBody()}")
                    responseLiveData.postValue(ServicesResponseWrapper.Success(res))
                }catch (e:java.lang.Exception){
                    Log.i(title, e.message.toString())
                }


            }
        }

    }

    fun errorConverter(
        error: ErrorModel?,
        errorString: String,
        errors: List<String>?,
        responseLiveData: MutableLiveData<ServicesResponseWrapper<ParentData>>,
        response: Response<ParentData>
    ) {
        var errorString1 = errorString
        if (error?.errors?.size!! > 1) {
            errorString1 = "${errors?.get(0)}, ${errors?.get(1)}"
        } else {
            errorString1 = errors?.get(0).toString()
        }
        Log.i(title, "message $errorString1")
        responseLiveData.postValue(ServicesResponseWrapper.Logout(errorString1, response.code()))
    }
}