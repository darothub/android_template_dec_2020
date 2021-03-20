package com.peacedude.lassod_tailor_app.model.response

import com.google.gson.annotations.SerializedName
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import java.io.Serializable

data class VideoList(
    val video: List<VideoResource>
) : Serializable, ParentData

data class VideoResource(
    val id: String,
    @SerializedName("artisanId")
    val tailorID: String?,
    val title: String,
    @SerializedName("videoUrl")
    val videoURL: String? = null,
    val description: String,
    val createdAt: String,
    val updatedAt: String
) : Serializable, ParentData {
    @SerializedName("duration")
    var duration: String? = ""
}
