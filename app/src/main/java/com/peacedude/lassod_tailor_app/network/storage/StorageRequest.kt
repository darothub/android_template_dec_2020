package com.peacedude.lassod_tailor_app.network.storage

import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import com.peacedude.lassod_tailor_app.model.request.User
import kotlin.reflect.KClass

interface StorageRequest {
    fun saveLastFragment(id:Int?)
    fun getLastFragmentId():Int
    fun saveLastLoginForm(emailorphone:String)
    fun getLastLoginForm():String?

    var sharedPrefer: EncryptedSharedPreferences
    var editor:SharedPreferences.Editor
    var gson:Gson
    fun clearData()
    fun <T>saveData(data: T?, key:String):ArrayList<String>{
        return TODO()
    }
    fun <T>keepData(data: T?, key:String){
        return TODO()
    }
    fun clearByKey(key: String):Boolean {
        return TODO()
    }

}

inline fun<reified T> StorageRequest.getData(data:String):T? = this.gson.fromJson(data, object :TypeToken<T>(){}.type)

inline fun<reified T> StorageRequest.checkData(key:String):T?{
    when {
        this.sharedPrefer.contains(key) ->{
            val result = sharedPrefer.getString(key, "")
            return result?.let { this.getData(it) }
        }
        else-> return null
    }

}
