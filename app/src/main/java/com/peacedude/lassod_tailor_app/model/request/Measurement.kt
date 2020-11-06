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
    val values: HashMap<String, String>
):Serializable, ParentData

data class Values (
    val ankle: String,
    val backWidth: Any? = null,
    val bottom: Any? = null,
    val cap: Any? = null,
    val fullLength: Any? = null,
    val highLength: Any? = null,
    val lap: Any? = null,
    val neckWidth: Any? = null,
    val roundSleeve: Any? = null,
    val armpit: Any? = null,
    val band: Any? = null,
    val chest: Any? = null,
    val halfLength: Any? = null,
    val hip: Any? = null,
    val longSleeve: Any? = null,
    val shortSleeve: Any? = null,
    val thigh: Any? = null,
    val shoulderToWaist: Any? = null,
    val dart: Any? = null,
    val handFit: Any? = null,
    val knee: Any? = null,
    val napeToWaist: Any? = null,
    val roundBody: Any? = null,
    val shoulder: Any? = null,
    val waist: Any? = null
):Serializable

data class ClientMeasurement(var client:MeasurementValues):Serializable, ParentData