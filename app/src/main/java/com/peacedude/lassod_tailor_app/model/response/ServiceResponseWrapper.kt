package com.peacedude.lassod_tailor_app.model.response

import com.peacedude.lassod_tailor_app.model.parent.ParentData

/**
 * A sealed class to control response from API
 */
sealed class ServicesResponseWrapper<T>(
    val data: T? = null,
    val message: String? = null,
    val code:Int? = null
):ParentData {
    /**
     * A success class wrapper
     */
    class Success<T>(data: T?) : ServicesResponseWrapper<T>(data)

    /**
     * A loading class wrapper
     */
    class Loading<T>(data: T? = null, message: String) : ServicesResponseWrapper<T>(data, message)
    class Logout<T>(message: String, code: Int?=null, data: T? = null) : ServicesResponseWrapper<T>(data, message, code)
    /**
     * An error class wrapper
     */
    class Error<T>(message: String?, code:Int?= null, data: T? = null) : ServicesResponseWrapper<T>(data, message, code)
}