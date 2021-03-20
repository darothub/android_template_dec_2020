package com.peacedude.lassod_tailor_app.model.response

import com.peacedude.lassod_tailor_app.model.parent.ParentData
import java.io.Serializable

data class MeasurementTypeList(
    val measurementTypes: List<MeasurementType>
) : Serializable, ParentData

data class MeasurementType(
    val id: Long,
    val name: String,
    val form: List<Form>,
    val createdAt: String,
    val updatedAt: String
) : Serializable, ParentData

data class Form(
    val label: String,
    val key: String
) : Serializable
