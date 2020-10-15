package com.peacedude.lassod_tailor_app.utils.storage

import androidx.security.crypto.EncryptedSharedPreferences
import com.google.gson.Gson
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.network.storage.StorageRequest
import javax.inject.Inject
import kotlin.reflect.KClass
const val LASTFRAGMENT = "lastFragment"
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
    override fun saveLastFragment(id: Int?) {
        editor.apply {
            if (id != null) {
                putInt(LASTFRAGMENT, id)
            }
            apply()
        }
    }

    override fun getLastFragmentId(): Int {
        return sharedPref.getInt(LASTFRAGMENT, 0)
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

//abstract class Hello(val sharedPref: EncryptedSharedPreferences){
//
//}
//
//class share(val sharedPrefs: EncryptedSharedPreferences):Hello(sharedPrefs){
//
//}