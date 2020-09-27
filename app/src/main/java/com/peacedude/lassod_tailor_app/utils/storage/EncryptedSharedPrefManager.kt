package com.peacedude.lassod_tailor_app.utils.storage

import androidx.security.crypto.EncryptedSharedPreferences
import com.google.gson.Gson
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.network.storage.StorageRequest
import javax.inject.Inject
import kotlin.reflect.KClass

class EncryptedSharedPrefManager @Inject internal constructor(val sharedPref: EncryptedSharedPreferences):StorageRequest {
    override var sharedPrefer: EncryptedSharedPreferences = sharedPref
    override var editor = sharedPrefer.edit()
    override var gson: Gson = Gson()
    override fun clearData() {
        editor.apply {
            clear()
            apply()
        }
    }

    override fun <T> saveData(user: T?, key: String): ArrayList<String> {
        val result:ArrayList<String>
        val userJson = gson.toJson(user)
        result = arrayListOf(userJson)
        editor
            .apply {
                putString(key, userJson)
                apply()
            }
        return result
    }

    override fun <T> keepData(user: T?, key: String) {
        val userJson = gson.toJson(user)
        editor
            .apply {
                putString(key, userJson)
                apply()
            }
    }

//    override fun <T : Any> getUserData(data: String, klass: KClass<T>): T? {
//        if(klass.objectInstance != null){
//            return gson.fromJson(data, klass.objectInstance!!::class.java)
//        }
//        else{
//            return null
//        }
//    }
//
//
//    override fun <T:Any>checkUser(key: String, klass: KClass<T>): T? {
//        when {
//            sharedPrefer.contains(key) ->{
//                val result = sharedPrefer.getString(key, "")
//                return result?.let { getUserData(it, klass) }
//            }
//        }
//        return null
//    }

    override fun clearByKey(key: String): Boolean {
        val data = sharedPrefer.contains(key)
        if (data){
            sharedPrefer.edit().apply {
                remove(key)
                apply()
            }
            return data
        }
        return false
    }
}