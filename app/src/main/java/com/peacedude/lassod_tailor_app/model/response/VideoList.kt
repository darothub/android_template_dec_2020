package com.peacedude.lassod_tailor_app.model.response

import com.peacedude.lassod_tailor_app.model.parent.ParentData
import java.io.Serializable

data class VideoList (
    val video: List<VideoResource>
):Serializable, ParentData

data class VideoResource (
    val id: String,
    val tailorID: String,
    val title: String,
    val videoURL: String,
    val description: String,
    val createdAt: String,
    val updatedAt: String,
    val placeHolder:Int?=0
):Serializable, ParentData{

}