package com.peacedude.lassod_tailor_app.helpers

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.liveData
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.model.error.ErrorModel
import com.peacedude.lassod_tailor_app.model.response.ServicesResponseWrapper
import com.peacedude.lassod_tailor_app.ui.adapters.ViewPagerAdapter
import com.squareup.moshi.Moshi
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


fun setupCategorySpinner(context: Context, spinner: Spinner, textArrayResId:Int) {
    val categorySpinnerAdapter = ArrayAdapter.createFromResource(
        context,
        textArrayResId,
        R.layout.spinner_colored_text_layout
    )
    categorySpinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout)

    spinner.adapter = categorySpinnerAdapter
}

fun setViewPager(activity: FragmentActivity, size:Int, getFragments:(Int)-> Fragment):ViewPagerAdapter {
    return ViewPagerAdapter(activity, size) { position->
        getFragments(position)
    }
}

fun connectViewPagerToTabLayout(adapter: ViewPagerAdapter, viewPager2: ViewPager2?, tabLayout: TabLayout, setTabTitle:(TabLayout.Tab, Int)->Unit){
    viewPager2?.adapter = adapter
    val tabLayoutMediator =
        viewPager2?.let {
            TabLayoutMediator(tabLayout, it,
                TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                    setTabTitle(tab, position)
                }).apply {
                attach()
            }
        }

}
fun setTabLayoutColour(tabLayout: TabLayout){
    tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(tabLayout.context, R.color.colorPrimary))
}

suspend fun <T> safeApiCall(dispatcher: CoroutineDispatcher, apiCall: suspend () -> T)=liveData<ServicesResponseWrapper<T>> {

    ServicesResponseWrapper.Loading(
        null,
        "Loading..."
    )
    try {
        ServicesResponseWrapper.Success(apiCall.invoke())
    } catch (throwable: Throwable) {
        Log.i("title", "Errornew ${throwable.message}")
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

fun Drawable.changeBackgroundColor(context: Context, color:Int){
    this.colorFilter = PorterDuffColorFilter(
        ContextCompat.getColor( context, color),
        PorterDuff.Mode.SRC_IN
    )
}




