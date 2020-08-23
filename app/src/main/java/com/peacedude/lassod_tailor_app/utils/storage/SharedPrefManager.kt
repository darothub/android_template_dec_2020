package com.peacedude.lassod_tailor_app.utils.storage

import android.content.SharedPreferences
import com.google.gson.Gson
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.network.storage.StorageRequest
import javax.inject.Inject

class SharedPrefManager @Inject constructor(val sharedPrefs: SharedPreferences, val gson: Gson):
    StorageRequest {
    override fun clearData() {
        sharedPrefs.edit()
            .apply {
                clear()
                apply()
            }
    }

    override fun <T> saveData(user: T?, key: String): ArrayList<String> {
        val result:ArrayList<String>
        val userJson = gson.toJson(user)
        result = arrayListOf(userJson)
        sharedPrefs.edit()
            .apply {
                putString(key, userJson)
                apply()
            }
        return result
    }

    override fun <T> keepData(user: T?, key: String) {
        val userJson = gson.toJson(user)
        sharedPrefs.edit()
            .apply {
                putString(key, userJson)
                apply() }

    }


    override fun getUserData(user:String): User? {
        return gson.fromJson(user, User::class.java)
    }

    override fun checkUser(key: String): User? {
        when {
            sharedPrefs.contains(key) ->{
                val result = sharedPrefs.getString(key, "")
                return result?.let { getUserData(it) }
            }
        }
        return null
    }

    override fun <T> clearByKey(key: String): Boolean {
        val data = sharedPrefs.contains(key)
        if (data){
            sharedPrefs.edit().apply {
                remove(key)
                apply()
            }
            return data
        }
        return false
    }


}