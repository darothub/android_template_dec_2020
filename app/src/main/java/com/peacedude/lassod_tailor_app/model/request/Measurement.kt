package com.peacedude.lassod_tailor_app.model.request

import com.google.gson.annotations.SerializedName
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import java.io.Serializable
import java.util.HashMap

data class Measurement (val name:String, var value:String):Serializable, ParentData {}
data class MeasurementValues(
    val name: String,
    val type: String,
    @SerializedName("clientId")
    val clientID: String,
    val values: Any

):Serializable, ParentData{
    var id:String?=null
    var gender:String?=""
}

data class Values (
    val ankle: String,
    val backWidth: Any? = "",
    val bottom: Any? = "",
    val cap: Any? = "",
    val fullLength: Any? = "",
    val highLength: Any? = "",
    val lap: Any? = "",
    val neckWidth: Any? = "",
    val roundSleeve: Any? = "",
    val armpit: Any? = "",
    val band: Any? = "",
    val chest: Any? = "",
    val halfLength: Any? = "",
    val hip: Any? = "",
    val longSleeve: Any? = "",
    val shortSleeve: Any? = "",
    val thigh: Any? = "",
    val shoulderToWaist: Any? = "",
    val dart: Any? = "",
    val handFit: Any? = "",
    val knee: Any? = "",
    val napeToWaist: Any? = "",
    val roundBody: Any? = "",
    val shoulder: Any? = "",
    val waist: Any? = ""
):Serializable

data class Valuess(val map:Map<String?, String?>){
    val ankle by map
    val backWidth by map
    val bottom by map
    val cap by map
    val fullLength by map
    val highLength by map
    val lap by map
    val neckWidth by map
    val roundSleeve by map
    val armpit by map
    val band by map
    val chest by map
    val halfLength by map
    val hip by map
    val longSleeve by map
    val shortSleeve by map
    val thigh by map
    val shoulderToWaist by map
    val dart by map
    val handFit by map
    val knee by map
    val napeToWaist by map
    val roundBody by map
    val shoulder by map
    val waist by map
}

data class ClientMeasurement(var client:MeasurementValues):Serializable, ParentData
data class ListOfMeasurement(var measurement:List<MeasurementValues>):Serializable, ParentData