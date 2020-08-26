package com.peacedude.lassod_tailor_app.ui

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import dagger.android.support.DaggerAppCompatActivity
import java.lang.Math.abs
import java.util.*
import kotlin.collections.ArrayList

abstract class BaseActivity : DaggerAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    //
    }

    override fun onResume() {
        super.onResume()
    }
}

//class ListClass():ContractInterface{
//    override fun getList(list: ArrayList<String>?): ArrayList<String>? {
//        if (list != null){
//            Result.success(list)
//        }
//        else{
//            Result.failure<String>(Throwable("List is null"))
//        }
//
//        return list
//    }
//
//}
//
//interface ContractInterface {
//    fun getList(list: ArrayList<String>?):ArrayList<String>?
//}


