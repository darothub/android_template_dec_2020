package com.peacedude.lassod_tailor_app.helpers

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.ColorRes
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.model.error.ErrorModel
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import com.peacedude.lassod_tailor_app.model.response.ServicesResponseWrapper
import com.peacedude.lassod_tailor_app.network.storage.StorageRequest
import com.peacedude.lassod_tailor_app.ui.MainActivity
import com.peacedude.lassod_tailor_app.utils.loggedInUserKey
import com.squareup.moshi.Moshi
import kotlinx.android.synthetic.main.fragment_signup.*
import kotlinx.coroutines.CoroutineDispatcher
import retrofit2.HttpException
import java.io.IOException

inline fun buttonTransactions(funct1:()->Unit, funct2:()->Unit){
    funct1()
    funct2()
}
/**
 * Get any name
 *
 * @return
 */
fun Any.getName(): String {
    return this::class.qualifiedName!!
}

fun logout(storageRequest:StorageRequest, context: Context) {
    val currentUser = storageRequest.checkUser(loggedInUserKey)
//    Log.i("return", "res $res")
    context.startActivity(Intent(context, MainActivity::class.java))
}

fun setupCategorySpinner(context: Context, spinner: Spinner, textArrayResId:Int) {
    val categorySpinnerAdapter = ArrayAdapter.createFromResource(
        context,
        textArrayResId,
        R.layout.spinner_colored_text_layout
    )
    categorySpinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout)

    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(p0: AdapterView<*>?) {}

        override fun onItemSelected(
            parentView: AdapterView<*>,
            selectedItemView: View?,
            position: Int,
            id: Long
        ) {

        }
    }
    spinner.adapter = categorySpinnerAdapter
}


suspend fun <T> safeApiCall(dispatcher: CoroutineDispatcher, apiCall: suspend () -> T)=liveData<ServicesResponseWrapper<T>> {

    ServicesResponseWrapper.Loading(
        null,
        "Loading..."
    )
    try {
        ServicesResponseWrapper.Success(apiCall.invoke())
    } catch (throwable: Throwable) {
        Log.i("title", "Errornew ${throwable?.message}")
        when (throwable) {
            is IOException -> ServicesResponseWrapper.Error<T>(throwable.message)
            is HttpException -> {
                val code = throwable.code()
                val errorResponse = convertErrorBody(throwable)
                ServicesResponseWrapper.Error(errorResponse?.errors?.get(0), code, errorResponse) as ServicesResponseWrapper<T>
//                        Log.i(title, "Errornew ${errorResponse?.errors?.get(0)}")
            }
            else -> {
                ServicesResponseWrapper.Error<T>(null, null)
            }
        }
    }


}

private fun convertErrorBody(throwable: HttpException): ErrorModel? {
    return try {
        throwable.response()?.errorBody()?.source()?.let {
            val moshiAdapter = Moshi.Builder().build().adapter(ErrorModel::class.java)
            moshiAdapter.fromJson(it)
        }
    } catch (exception: Exception) {
        null
    }
}



