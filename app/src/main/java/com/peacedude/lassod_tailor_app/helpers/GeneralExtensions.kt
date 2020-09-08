package com.peacedude.lassod_tailor_app.helpers

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.ColorRes
import androidx.core.content.ContentProviderCompat.requireContext
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.network.storage.StorageRequest
import com.peacedude.lassod_tailor_app.ui.MainActivity
import com.peacedude.lassod_tailor_app.utils.loggedInUserKey
import kotlinx.android.synthetic.main.fragment_signup.*

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



