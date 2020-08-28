package com.peacedude.lassod_tailor_app.utils.storage

import androidx.security.crypto.EncryptedSharedPreferences
import com.google.gson.Gson
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.network.storage.StorageRequest
import javax.inject.Inject

class EncryptedSharedPrefManager @Inject constructor(val sharedPref: EncryptedSharedPreferences):StorageRequest {
    private val editor = sharedPref.edit()
    val gson: Gson = Gson()
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

    override fun getUserData(user: String): User? {
        return gson.fromJson(user, User::class.java)
    }

    override fun checkUser(key: String): User? {
        when {
            sharedPref.contains(key) ->{
                val result = sharedPref.getString(key, "")
                return result?.let { getUserData(it) }
            }
        }
        return null
    }

    override fun <T> clearByKey(key: String): Boolean {
        val data = sharedPref.contains(key)
        if (data){
            sharedPref.edit().apply {
                remove(key)
                apply()
            }
            return data
        }
        return false
    }
}