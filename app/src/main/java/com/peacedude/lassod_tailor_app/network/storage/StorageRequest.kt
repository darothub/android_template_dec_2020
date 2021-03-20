package com.peacedude.lassod_tailor_app.network.storage

import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

interface StorageRequest {
    fun saveLastFragment(credential: String, id: Int?)
    fun getLastFragmentId(credential: String): Int
    fun saveLastLoginForm(emailorphone: String)
    fun getLastLoginForm(): String?

    var sharedPrefer: EncryptedSharedPreferences
    var editor: SharedPreferences.Editor
    var gson: Gson
    fun clearData()
    fun <T> saveData(data: T?, key: String): ArrayList<String> = TODO()
    fun <T> keepData(data: T?, key: String): Unit = TODO()
    fun clearByKey(key: String): Boolean = TODO()
}

inline fun <reified T> StorageRequest.getData(data: String): T? = this.gson.fromJson(data, object : TypeToken<T>() {}.type)

inline fun <reified T> StorageRequest.checkData(key: String): T? {
    return when {
        this.sharedPrefer.contains(key) -> {
            val result = sharedPrefer.getString(key, "")
            result?.let { this.getData<T>(it) }
        }

        else -> null
    }
}
