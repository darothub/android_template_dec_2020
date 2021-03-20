package com.peacedude.lassod_tailor_app.utils.storage

import androidx.security.crypto.EncryptedSharedPreferences
import com.google.gson.Gson
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.network.storage.StorageRequest
import javax.inject.Inject
const val LASTFRAGMENT = "lastFragment"
const val LOGINFORM = "lastLoginWith"
class EncryptedSharedPrefManager @Inject internal constructor(val sharedPref: EncryptedSharedPreferences) : StorageRequest {

    override var sharedPrefer: EncryptedSharedPreferences = sharedPref
    override var editor = sharedPrefer.edit()
    override var gson: Gson = Gson()
    override fun clearData() {
        editor.apply {
            clear()
            apply()
        }
    }
    override fun saveLastFragment(credential: String, id: Int?) {
        editor.apply {
            if (id != null) {
                putInt(credential, id)
            }
            apply()
        }
    }

    override fun getLastFragmentId(credential: String): Int {
        return sharedPref.getInt(credential, R.id.profileFragment)
    }

    override fun saveLastLoginForm(emailorphone: String) {
        editor.apply {
            putString(LOGINFORM, emailorphone)
            apply()
        }
    }

    override fun getLastLoginForm(): String? {
        return sharedPref.getString(LOGINFORM, "phone")
    }

    override fun <T> saveData(user: T?, key: String): ArrayList<String> {
        val result: ArrayList<String>
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

    override fun clearByKey(key: String): Boolean {
        val data = sharedPrefer.contains(key)
        if (data) {
            sharedPrefer.edit().apply {
                remove(key)
                apply()
            }
            return data
        }
        return false
    }
}


fun main(){
    hell<String, Boolean>("Ball", true)
}

fun <T, X> hell(t:T, x:X){
    println("$t $x")
}