package com.peacedude.lassod_tailor_app.sharedpref

import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.network.storage.StorageRequest

class FakeSharedPrefManager(val hashMap: HashMap<String, String>):StorageRequest {
    override fun clearData() {
        hashMap.clear()
    }

    override fun <T> saveData(user: T?, key: String): ArrayList<String> {
        var result = arrayListOf<String>()
        hashMap.put(key, user.toString())
        result.add(user.toString())
        return result
    }

    override fun <T> keepData(user: T?, key: String) {
        TODO("Not yet implemented")
    }

    override fun getUserData(user: String): User? {
        TODO("Not yet implemented")
    }

//    override fun checkUser(key: User): User? {
//        TODO("Not yet implemented")
//    }

//    override fun <T> clearByKey(key: String): Boolean {
//        if(hashMap.contains(key)){
//            hashMap.remove(key)
//            return true
//        }
//        return false
//    }
}