package com.peacedude.lassod_tailor_app.network.storage

import com.peacedude.lassod_tailor_app.model.request.User

interface StorageRequest {
    fun clearData()
    fun <T>saveData(user: T?, key:String):ArrayList<String>{
        return TODO()
    }
    fun <T>keepData(user: T?, key:String){
        return TODO()
    }
    fun getUserData(user:String):User?{
        return TODO()
    }
    fun checkUser(key: String): User?{
        return TODO()
    }
    fun clearByKey(key: String):Boolean {
        return TODO()
    }
}